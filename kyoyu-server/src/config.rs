use std::{fs, path::Path};

use serde::{Deserialize, Serialize};

#[derive(Debug, Clone, Deserialize, Serialize)]
/// 設定ファイルから読み込まれるサーバー設定構造体
pub struct KyoyuConfig {
    bind_address: Option<String>,
}

impl KyoyuConfig {
    /// 指定されたパスから設定ファイルを読み込み、`KyoyuConfig` を返す
    ///
    /// # 引数
    /// * `path` - 設定ファイルのパス
    ///
    /// # 戻り値
    /// `Result<KyoyuConfig, KyoyuConfigError>` 型。成功時は `KyoyuConfig`、失敗時はエラー。
    pub fn from_file<P: AsRef<Path>>(path: P) -> Result<Self, serde_json::Error> {
        if let Ok(input) = fs::read_to_string(path) {
            serde_json::from_str(&input)
        } else {
            Ok(Self::default())
        }
    }

    /// バインドアドレスを返す
    pub fn bind_address(&self) -> String {
        self.bind_address
            .clone()
            .unwrap_or("0.0.0.0:4512".to_string())
    }
}

impl Default for KyoyuConfig {
    fn default() -> Self {
        Self {
            bind_address: Some("0.0.0.0:4512".to_string()),
        }
    }
}
