use std::sync::Arc;

use crate::{
    config::KyoyuConfig,
    connection::Connection,
    packet::{C2SPackets, auth::AuthenticationC2S},
};
use tokio::{
    net::{
        TcpStream,
        tcp::{OwnedReadHalf, OwnedWriteHalf},
    },
    sync::Mutex,
};

#[derive(Debug)]
/// クライアントの接続状態を表す構造体
/// `KyoyuClient` とコネクション情報を保持する。
pub struct ClientConnectionState {
    pub client: Arc<Mutex<KyoyuClient>>,
    pub connection: Option<Connection<OwnedReadHalf, OwnedWriteHalf, Self>>,
}

#[derive(Debug)]
/// Kyoyu クライアント構造体。サーバー設定と接続アドレスを保持。
pub struct KyoyuClient {
    config: KyoyuConfig,
    address: String,
    connection: Option<Connection<OwnedReadHalf, OwnedWriteHalf, ClientConnectionState>>,
}

impl KyoyuClient {
    /// クライアントを新しく生成する
    ///
    /// # 引数
    /// * `config` - サーバー設定
    ///
    /// # 戻り値
    /// `Self` 型の新しいクライアント
    /// この関数ではアドレスの解決と構造体の初期化のみを行う。
    pub async fn new(config: KyoyuConfig) -> Self {
        Self {
            address: config.bind_address(),
            config,
            connection: None,
        }
    }

    /// 参加
    pub fn connection_set(
        &mut self,
        connection: Connection<OwnedReadHalf, OwnedWriteHalf, ClientConnectionState>,
    ) {
        self.connection = Some(connection);
    }

    /// 退出
    pub fn connection_unset(&mut self) {
        self.connection = None;
    }

    /// クライアント設定への参照を返す
    pub fn config(&self) -> &KyoyuConfig {
        &self.config
    }

    /// サーバーに接続し、接続処理を開始する
    ///
    /// この関数では、TCP接続、ハンドシェイク、認証パケット送信、
    /// およびクライアント用のパケット処理ループを開始する。
    pub async fn spawn(self) {
        let config = self.config().clone();

        let tcp_stream = match TcpStream::connect(self.address.clone()).await {
            Ok(s) => s,
            Err(e) => {
                eprintln!("failed to connect to server: {e}");
                return;
            }
        };
        let client = Arc::new(Mutex::new(self));

        let (reader, writer) = tcp_stream.into_split();

        // 接続状態を初期化する
        let state = Arc::new(Mutex::new(ClientConnectionState {
            client: Arc::clone(&client),
            connection: None,
        }));
        let connection = Connection::new(reader, writer, &state, &config);

        // ハンドシェイク
        if !connection.handshake().await {
            return;
        }

        state.lock().await.connection = Some(connection.clone());

        // 認証パケットを作成・送信する
        let auth_packet = {
            let locked = client.lock().await;
            AuthenticationC2S::new(locked.config.clone())
        };
        if let Err(e) = connection
            .send(&C2SPackets::Authentication(auth_packet))
            .await
        {
            eprintln!("failed to send authentication packet: {e}");
            return;
        }

        eprintln!("connection spawn");
        connection.process_client().await;

        let locked_state = state.lock().await;
        locked_state.client.lock().await.connection_unset();
    }
}
