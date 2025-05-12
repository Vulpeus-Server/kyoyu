use std::{collections::HashMap, sync::Arc};

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
    pub connection: Option<Connection<OwnedReadHalf, OwnedWriteHalf, Self>>,
}

#[derive(Debug)]
/// Kyoyu サーバー本体。設定と TCP リスナーを保持する。
pub struct KyoyuServer {
    config: KyoyuConfig,
    address: String,
    connections:
        HashMap<Arc<KyoyuPlayer>, Connection<OwnedReadHalf, OwnedWriteHalf, ServerConnectionState>>,
}

impl KyoyuServer {
    /// 設定に基づいてサーバーを初期化する
    ///
    /// # 引数
    /// * `config` - サーバー設定
    ///
    /// # 戻り値
    /// `Result<KyoyuServer, std::io::Error>`
    pub async fn new(config: KyoyuConfig) -> Self {
        let address = config.bind_address();
        Self {
            config,
            address,
            connections: HashMap::new(),
        }
    }

    /// プレイヤーの参加
    pub fn player_join(
        &mut self,
        player: &Arc<KyoyuPlayer>,
        connection: Connection<OwnedReadHalf, OwnedWriteHalf, ServerConnectionState>,
    ) {
        self.connections.insert(Arc::clone(player), connection);
    }

    #[allow(dead_code)]
    /// サーバー設定への参照を返す
    pub fn config(&self) -> &KyoyuConfig {
        &self.config
    }

    /// サーバーを起動し、クライアント接続を非同期に処理する
    pub async fn spawn(self) {
        let listener = TcpListener::bind(self.address.clone()).await.unwrap();
        let server = Arc::new(Mutex::new(self));
        loop {
            let (tcp_stream, _) = listener.accept().await.unwrap();
            let (reader, writer) = tcp_stream.into_split();

            // 接続状態を初期化する
            let state = Arc::new(Mutex::new(ServerConnectionState {
                server: Arc::clone(&server),
                player: None,
                connection: None,
            }));
            let connection = Connection::new(reader, writer, &state);

            // ハンドシェイク
            if !connection.handshake().await {
                continue;
            }
            state.lock().await.connection = Some(connection.clone());

            tokio::spawn(async move {
                eprintln!("connection spawn");
                connection.process_server().await;

                let locked_state = state.lock().await;
                if let Some(player) = &locked_state.player {
                    locked_state.server.lock().await.connections.remove(player);
                    eprintln!("connection dropped {:?}", player);
                }
            });
        }
    }
}
