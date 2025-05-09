pub mod auth;

use serde::{Deserialize, Serialize};

pub trait ServerPacketHandler {
    async fn handle_server(&self) -> bool;
}
pub trait ClientPacketHandler {
    async fn handle_client(&self) -> bool;
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

impl ServerPacketHandler for C2SPackets {
    async fn handle_server(&self) -> bool {
        println!("{:?}", self);
        match self {
            C2SPackets::Authentication(p) => p.handle_server().await,
        }
    }
}

impl ClientPacketHandler for S2CPackets {
    async fn handle_client(&self) -> bool {
        match self {
            S2CPackets::Authentication(p) => p.handle_client().await,
        }
    }
}
