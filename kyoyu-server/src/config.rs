use std::{error, fmt, fs, io, path::Path};

use serde::{Deserialize, Serialize};

#[derive(Debug, Clone, Deserialize, Serialize)]
/// 設定ファイルから読み込まれるサーバー設定構造体
pub struct KyoyuConfig {
    bind_address: Option<String>,
}

#[derive(Debug)]
/// 設定ファイルの読み込み中に発生しうるエラーの列挙型
pub enum KyoyuConfigError {
    IOError(io::Error),
    SerializeError(serde_json::Error),
}
impl fmt::Display for KyoyuConfigError {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        match self {
            Self::IOError(e) => e.fmt(f),
            Self::SerializeError(e) => e.fmt(f),
        }
    }
}
impl error::Error for KyoyuConfigError {
    fn source(&self) -> Option<&(dyn error::Error + 'static)> {
        match self {
            Self::IOError(e) => Some(e),
            Self::SerializeError(e) => Some(e),
        }
    }
}

impl KyoyuConfig {
    /// 指定されたパスから設定ファイルを読み込み、`KyoyuConfig` を返す
    ///
    /// # 引数
    /// * `path` - 設定ファイルのパス
    ///
    /// # 戻り値
    /// `Result<KyoyuConfig, KyoyuConfigError>` 型。成功時は `KyoyuConfig`、失敗時はエラー。
    pub fn from_file<P: AsRef<Path>>(path: P) -> Result<Self, KyoyuConfigError> {
        let input = fs::read_to_string(path).map_err(KyoyuConfigError::IOError)?;
        serde_json::from_str(&input).map_err(KyoyuConfigError::SerializeError)
    }

    /// バインドアドレスを返す（設定されていない場合は `None`）
    pub fn bind_address(&self) -> &Option<String> {
        &self.bind_address
    }
}
