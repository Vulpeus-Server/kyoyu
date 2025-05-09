mod client;
mod config;
mod connection;
mod packet;
mod player;
mod server;

use clap::Parser;
use client::KyoyuClient;
#[cfg(not(unix))]
use tokio::signal::ctrl_c;
#[cfg(unix)]
use tokio::signal::unix::{SignalKind, signal};

use config::KyoyuConfig;
use server::KyoyuServer;

#[derive(Parser, Debug)]
#[command(version, about, long_about = None)]
struct Args {
    #[arg(short, long)]
    client: bool,
}

#[tokio::main]
async fn main() {
    let args = Args::parse();

    let config = KyoyuConfig::from_file("config.json").unwrap();

    tokio::spawn(async {
        setup_sighandler()
            .await
            .expect("Unable to setup signal handlers");
    });

    if args.client {
        let client = KyoyuClient::new(config).await;
        println!("starting client");
        let _ = client.spawn().await;
    } else {
        let server = KyoyuServer::new(config).await.unwrap();
        println!("starting server");
        let _ = server.spawn().await;
    }
}

fn handle_interrupt() {
    eprintln!("Received interrupt signal; stopping...");
    std::process::exit(0);
}

#[cfg(not(unix))]
/// Non-UNIX Ctrl-C handling
async fn setup_sighandler() -> std::io::Result<()> {
    if ctrl_c().await.is_ok() {
        handle_interrupt();
    }

    Ok(())
}

#[cfg(unix)]
/// Unix signal handling
async fn setup_sighandler() -> std::io::Result<()> {
    if signal(SignalKind::interrupt())?.recv().await.is_some() {
        handle_interrupt();
    }

    if signal(SignalKind::hangup())?.recv().await.is_some() {
        handle_interrupt();
    }

    if signal(SignalKind::terminate())?.recv().await.is_some() {
        handle_interrupt();
    }

    Ok(())
}

