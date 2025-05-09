use std::sync::Arc;

use crate::{config::KyoyuConfig, connection::Connection, packet::{auth::AuthenticationC2S, C2SPackets}};
use tokio::{net::{tcp::{OwnedReadHalf, OwnedWriteHalf}, TcpStream}, sync::Mutex};

#[derive(Debug)]
pub struct ClientConnectionState {
    pub client: Arc<Mutex<KyoyuClient>>,
}

#[derive(Debug)]
/// Kyoyu クライアント構造体。サーバー設定と接続アドレスを保持。
pub struct KyoyuClient {
    config: KyoyuConfig,
    address: String,
    connection: Option<Arc<Mutex<Connection<OwnedReadHalf, OwnedWriteHalf, ClientConnectionState>>>>,
}

impl KyoyuClient {
    /// クライアントを新しく生成する
    ///
    /// # 引数
    /// * `config` - サーバー設定
    ///
    /// # 戻り値
    /// `Self` 型の新しいクライアント
    pub async fn new(config: KyoyuConfig) -> Self {
        let address = config
            .bind_address()
            .clone()
            .unwrap_or("0.0.0.0:4512".to_string());
        Self {
            config,
            address,
            connection: None,
        }
    }

    /// サーバーに接続し、接続処理を開始する
    pub async fn spawn(self) {
        let client = Arc::new(Mutex::new(self));
        let stream = TcpStream::connect(client.lock().await.address.clone())
            .await
            .expect("failed to connect to server");
        let (reader, writer) = stream.into_split();
        let state = Arc::new(Mutex::new(ClientConnectionState {
            client: Arc::clone(&client),
        }));
        let connection = Arc::new(Mutex::new(Connection::new(reader, writer, &state)));
        client.lock().await.connection = Some(Arc::clone(&connection));
        if !connection.lock().await.handshake().await {
            return;
        }
        {
            let auth_packet = AuthenticationC2S::new(client.lock().await.config.clone());
            connection.lock().await.send(&C2SPackets::Authentication(auth_packet)).await.unwrap();
        }
        connection.lock().await.process_client().await;
    }
}
