use std::sync::Arc;

use serde::{Deserialize, Serialize};
use tokio::sync::Mutex;

use crate::{client::ClientConnectionState, config::KyoyuConfig, player::KyoyuPlayer, server::ServerConnectionState};

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
    async fn handle(&self, _state: Arc<Mutex<ServerConnectionState>>) -> bool {
        let player = KyoyuPlayer::new(self.uuid);
        eprintln!("on Auth: {:?}", player);
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
    async fn handle(&self, _state: Arc<Mutex<ClientConnectionState>>) -> bool {
        false
    }
}
