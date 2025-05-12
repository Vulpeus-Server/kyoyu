use std::sync::Arc;

use kyoyu_server::MAGIC;
use serde::{Deserialize, Serialize};
use tokio::{
    io::{AsyncRead, AsyncReadExt, AsyncWrite, AsyncWriteExt},
    sync::Mutex,
};

use crate::{
    client::ClientConnectionState,
    packet::{C2SPackets, PacketHandler, S2CPackets},
    server::ServerConnectionState,
};

#[derive(Debug)]
/// MessagePack 形式でシリアライズされたパケットの読み取りを行う構造体
pub struct PacketReader<T>(T);

#[derive(Debug)]
/// MessagePack 形式でシリアライズされたパケットの書き込みを行う構造体
pub struct PacketWriter<T>(T);

impl<T: AsyncRead + Unpin> PacketReader<T> {
    /// パケットの長さを読み込み、そのバイト数分読み込んでデシリアライズ
    pub async fn read<P: for<'de> Deserialize<'de>>(&mut self) -> Option<P> {
        let len = match self.0.read_u32().await {
            Err(_) => return None,
            Ok(0) => return None,
            Ok(n) => n,
        };

        let mut buf = vec![0; len as usize];
        if self.0.read_exact(&mut buf).await.is_err() {
            return None;
        }

        rmp_serde::from_slice::<'_, P>(&buf).ok()
    }
}

impl<T: AsyncWrite + Unpin> PacketWriter<T> {
    /// パケットを書き込む。先頭に長さ（u32）を付与して送信する。
    ///
    /// # 引数
    /// * `packet` - シリアライズ可能な送信パケット
    pub async fn write<P: Serialize>(
        &mut self,
        packet: &P,
    ) -> Result<(), Box<dyn std::error::Error>> {
        let data = rmp_serde::to_vec(packet)?;
        self.0.write_u32(data.len() as u32).await?;
        self.0.write_all(&data).await?;
        self.0.flush().await?;
        Ok(())
    }
}

#[derive(Debug)]
/// クライアントとサーバー間のコネクションを抽象化する構造体
pub struct Connection<R, W, T> {
    reader: Arc<Mutex<PacketReader<R>>>,
    writer: Arc<Mutex<PacketWriter<W>>>,
    state: Arc<Mutex<T>>,
}

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
            reader: Arc::new(Mutex::new(PacketReader(reader))),
            writer: Arc::new(Mutex::new(PacketWriter(writer))),
            state: Arc::clone(state),
        }
    }

    /// Packet Reader を取得する
    pub fn reader(&self) -> Arc<Mutex<PacketReader<R>>> {
        Arc::clone(&self.reader)
    }

    /// Packet Writer を取得する
    pub fn writer(&self) -> Arc<Mutex<PacketWriter<W>>> {
        Arc::clone(&self.writer)
    }

    /// State を取得する
    pub fn state(&self) -> Arc<Mutex<T>> {
        Arc::clone(&self.state)
    }
}

impl<R: AsyncRead + Unpin, W> Connection<R, W, ServerConnectionState> {
    /// クライアントからのパケットを受け取り、処理する
    ///
    /// パケットごとに `handle` を呼び出し、終了条件を満たすとループを抜ける。
    pub async fn process_server(&self) {
        while let Some(packet) = self.reader.lock().await.read::<C2SPackets>().await {
            if packet.handle(self.state()).await {
                break;
            }
        }
    }
}

impl<R: AsyncRead + Unpin, W> Connection<R, W, ClientConnectionState> {
    /// サーバーからのパケットを受け取り、処理する
    ///
    /// パケットごとに `handle` を呼び出し、終了条件を満たすとループを抜ける。
    pub async fn process_client(&self) {
        while let Some(packet) = self.reader.lock().await.read::<S2CPackets>().await {
            if packet.handle(self.state()).await {
                break;
            }
        }
    }
}

impl<R, W: AsyncWrite + Unpin, T> Connection<R, W, T> {
    /// パケットを送信する
    ///
    /// # 引数
    /// * `packet` - シリアライズ可能なパケット
    pub async fn send<P: Serialize>(&self, packet: &P) -> Result<(), Box<dyn std::error::Error>> {
        self.writer.lock().await.write(packet).await
    }
}

impl<R: AsyncRead + Unpin, W: AsyncWrite + Unpin, T> Connection<R, W, T> {
    /// サーバー／クライアント間のハンドシェイク処理。
    ///
    /// `MAGIC` 値が一致すれば `true` を返す。
    /// 通信確立時にプロトコルの一致を確認する目的で利用される。
    pub async fn handshake(&self) -> bool {
        if let Err(e) = self.writer.lock().await.0.write_u64(MAGIC).await {
            eprintln!("handshake write failed: {e}");
            return false;
        }
        match self.reader.lock().await.0.read_u64().await {
            Ok(magic) => {
                let valid = magic == MAGIC;
                dbg!(valid);
                valid
            }
            Err(e) => {
                eprintln!("handshake read failed: {e}");
                false
            }
        }
    }
}

impl<R, W, T> Clone for Connection<R, W, T> {
    /// コネクションを複製する。
    /// `Arc` による共有参照の複製となるため、実体は共有される。
    fn clone(&self) -> Self {
        Self {
            reader: self.reader(),
            writer: self.writer(),
            state: self.state(),
        }
    }
}
