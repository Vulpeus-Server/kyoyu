pub mod auth;

use std::sync::Arc;

use serde::{Deserialize, Serialize};
use tokio::sync::Mutex;

use crate::{client::ClientConnectionState, server::ServerConnectionState};

pub trait PacketHandler<T> {
    async fn handle(&self, state: Arc<Mutex<T>>) -> bool;
}

#[derive(Debug, Deserialize, Serialize)]
/// Client -> Server のパケットの列挙型
pub enum C2SPackets {
    Authentication(auth::AuthenticationC2S),
}

#[derive(Debug, Deserialize, Serialize)]
/// Server -> Client のパケットの列挙型
pub enum S2CPackets {
    Authentication(auth::AuthenticationS2C),
}

impl PacketHandler<ServerConnectionState> for C2SPackets {
    async fn handle(&self, state: Arc<Mutex<ServerConnectionState>>) -> bool {
        println!("{:?}", self);
        match self {
            C2SPackets::Authentication(p) => p.handle(state).await,
        }
    }
}

impl PacketHandler<ClientConnectionState> for S2CPackets {
    async fn handle(&self, state: Arc<Mutex<ClientConnectionState>>) -> bool {
        match self {
            S2CPackets::Authentication(p) => p.handle(state).await,
        }
    }
}

