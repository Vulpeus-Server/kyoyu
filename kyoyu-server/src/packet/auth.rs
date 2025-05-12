use std::sync::Arc;

use serde::{Deserialize, Serialize};
use tokio::sync::Mutex;

use crate::{
    client::ClientConnectionState, config::KyoyuConfig, packet::S2CPackets, player::KyoyuPlayer,
    server::ServerConnectionState,
};

use super::PacketHandler;

#[derive(Debug, Clone, Deserialize, Serialize)]
/// 認証構造体。バージョン、設定、UUID を含む。
pub struct AuthenticationC2S {
    version: String,
    config: KyoyuConfig,
    uuid: uuid::Uuid,
}

impl AuthenticationC2S {
    pub fn new(config: KyoyuConfig) -> Self {
        Self {
            version: env!("CARGO_PKG_VERSION").to_string(),
            config,
            uuid: uuid::Uuid::from_u64_pair(0, 0),
        }
    }
}

impl PacketHandler<ServerConnectionState> for AuthenticationC2S {
    async fn handle(&self, state: Arc<Mutex<ServerConnectionState>>) -> bool {
        let player = Arc::new(KyoyuPlayer::new(self.uuid));

        let conn = {
            let mut locked_state = state.lock().await;

            locked_state.player = Some(Arc::clone(&player));
            if let Some(conn) = &locked_state.connection {
                conn.clone()
            } else {
                return false;
            }
        };

        let packet = S2CPackets::Authentication(AuthenticationS2C::new(self.config.clone()));
        let _ = conn.send(&packet).await;

        state
            .lock()
            .await
            .server
            .lock()
            .await
            .connections_insert(&player, conn.clone());
        false
    }
}

#[derive(Debug, Clone, Deserialize, Serialize)]
pub struct AuthenticationS2C {
    version: String,
    config: KyoyuConfig,
}

impl AuthenticationS2C {
    pub fn new(config: KyoyuConfig) -> Self {
        Self {
            version: env!("CARGO_PKG_VERSION").to_string(),
            config,
        }
    }
}

impl PacketHandler<ClientConnectionState> for AuthenticationS2C {
    async fn handle(&self, state: Arc<Mutex<ClientConnectionState>>) -> bool {
        {
            let locked_state = state.lock().await;
            locked_state
                .client
                .lock()
                .await
                .connection_set(locked_state.connection.clone().unwrap());
        }
        eprintln!("connected!");
        false
    }
}
