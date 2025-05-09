use std::hash::Hash;

#[derive(Debug, Clone, PartialEq, Eq)]
/// プレイヤー情報を保持する構造体。主に UUID に基づく識別に利用。
pub struct KyoyuPlayer {
    uuid: uuid::Uuid,
}

impl KyoyuPlayer {
    /// 新しいプレイヤーを作成する
    pub fn new(uuid: uuid::Uuid) -> Self {
        Self { uuid }
    }
}

impl Hash for KyoyuPlayer {
    /// HashMap, HashSet 用
    fn hash<H: std::hash::Hasher>(&self, state: &mut H) {
        self.uuid.hash(state)
    }
}
