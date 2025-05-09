use std::sync::Arc;

use tokio::{
    net::{
        TcpListener,
        tcp::{OwnedReadHalf, OwnedWriteHalf},
    },
    sync::Mutex,
};

use crate::{config::KyoyuConfig, connection::Connection, player::KyoyuPlayer};

#[derive(Debug)]
pub struct ServerConnectionState {
    pub server: Arc<Mutex<KyoyuServer>>,
    pub player: Option<Arc<KyoyuPlayer>>,
}

#[derive(Debug)]
/// Kyoyu サーバー本体。設定と TCP リスナーを保持する。
pub struct KyoyuServer {
    config: KyoyuConfig,
    listener: TcpListener,
    connections: Vec<Arc<Mutex<Connection<OwnedReadHalf, OwnedWriteHalf, ServerConnectionState>>>>,
}

impl KyoyuServer {
    /// 設定に基づいてサーバーを初期化する
    ///
    /// # 引数
    /// * `config` - サーバー設定
    ///
    /// # 戻り値
    /// `Result<KyoyuServer, std::io::Error>`
    pub async fn new(config: KyoyuConfig) -> Result<Self, std::io::Error> {
        let address = config
            .bind_address()
            .clone()
            .unwrap_or("0.0.0.0:4512".to_string());
        let listener = TcpListener::bind(address).await?;
        Ok(Self {
            config,
            listener,
            connections: Vec::new(),
        })
    }

    #[allow(dead_code)]
    /// サーバー設定への参照を返す
    pub fn config(&self) -> &KyoyuConfig {
        &self.config
    }

    #[allow(dead_code)]
    /// TCP リスナーへの参照を返す
    pub fn listener(&self) -> &TcpListener {
        &self.listener
    }

    /// サーバーを起動し、クライアント接続を非同期に処理する
    pub async fn spawn(self) -> Result<(), std::io::Error> {
        let server = Arc::new(Mutex::new(self));
        loop {
            let (tcp_stream, _) = server.lock().await.listener.accept().await?;
            let (reader, writer) = tcp_stream.into_split();
            let state = Arc::new(Mutex::new(ServerConnectionState {
                server: Arc::clone(&server),
                player: None,
            }));
            let connection = Arc::new(Mutex::new(Connection::new(reader, writer, &state)));
            if !connection.lock().await.handshake().await {
                continue;
            }
            server.lock().await.connections.push(Arc::clone(&connection));
            tokio::spawn(async move {
                connection.lock().await.process_server().await;
            });
        }
    }
}
