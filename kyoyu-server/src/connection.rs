use std::sync::Arc;

use kyoyu_server::MAGIC;
use serde::{Deserialize, Serialize};
use tokio::{io::{AsyncRead, AsyncReadExt, AsyncWrite, AsyncWriteExt}, sync::Mutex};

use crate::packet::{C2SPackets, ClientPacketHandler, S2CPackets, ServerPacketHandler};

#[derive(Debug)]
/// クライアントとサーバー間のコネクションを抽象化する構造体
pub struct Connection<R, W, T> {
    reader: R,
    writer: W,
    state: Arc<Mutex<T>>,
}

#[allow(dead_code)]
impl<R, W, T> Connection<R, W, T> {
    /// 新しいコネクションを生成する
    ///
    /// # 引数
    /// * `reader`, `writer` - ストリームの読み取り/書き込み半分
    /// * `state` - 共有状態
    ///
    /// # 戻り値
    /// 新しい Connection 構造体
    pub fn new(reader: R, writer: W, state: &Arc<Mutex<T>>) -> Self {
        Self {
            reader,
            writer,
            state: Arc::clone(state),
        }
    }
}

impl<R, W: AsyncWrite + Unpin, T> Connection<R, W, T> {
    /// パケットを送信する
    ///
    /// # 引数
    /// * `packet` - 送信するパケット
    ///
    /// # 戻り値
    /// IO 結果
    pub async fn send<P: Serialize>(&mut self, packet: &P) -> Result<(), std::io::Error> {
        let data = rmp_serde::to_vec(packet).unwrap();
        self.writer.write_u32(data.len() as u32).await?;
        self.writer.write_all(&data).await?;
        Ok(())
    }
}

impl<R: AsyncRead + Unpin, W, T> Connection<R, W, T> {
    /// 次のパケットを読み取る
    async fn read_next<P: for<'de> Deserialize<'de>>(&mut self) -> Result<P, ()> {
        let len = match self.reader.read_u32().await {
            Err(_) => return Err(()),
            Ok(0) => return Err(()),
            Ok(n) => n,
        };

        let mut buf = vec![0; len as usize];
        if self.reader.read_exact(&mut buf).await.is_err() {
            return Err(());
        }

        rmp_serde::from_slice::<'_, P>(&buf).map_err(|_| ())
    }

    pub async fn process_server(&mut self) {
        while let Ok(packet) = self.read_next::<C2SPackets>().await {
            if packet.handle_server().await {
                break;
            }
        }
    }
    pub async fn process_client(&mut self) {
        while let Ok(packet) = self.read_next::<S2CPackets>().await {
            if packet.handle_client().await {
                break;
            }
        }
    }
}

impl<R: AsyncRead + Unpin, W: AsyncWrite + Unpin, T> Connection<R, W, T> {
    /// magic のチェック
    pub async fn handshake(&mut self) -> bool {
        self.writer.write_u64(MAGIC).await.unwrap();
        self.writer.flush().await.unwrap();
        let magic = self.reader.read_u64().await.unwrap();
        dbg!(magic == MAGIC)
    }
}

