# Kyoyu

[![License](https://img.shields.io/github/license/Vulpeus-Server/kyoyu)](http://www.gnu.org/licenses/lgpl-3.0.html)
[![Modrinth](https://img.shields.io/modrinth/dt/VozTPxB4?label=Modrinth%20Downloads)](https://modrinth.com/mod/VozTPxB4)
[![Discord](https://img.shields.io/discord/1157213775791935539?logo=Discord)](https://discord.gg/tjayanzYMf)

[![GitHub Workflow](https://github.com/Vulpeus-Server/kyoyu/actions/workflows/gradle.yml/badge.svg)](https://github.com/Vulpeus-Server/kyoyu/actions/workflows/gradle.yml)
[![GitHub Issues](https://img.shields.io/github/issues/Vulpeus-Server/kyoyu)](https://github.com/Vulpeus-Server/kyoyu/issues)
[![GitHub Created At](https://img.shields.io/github/created-at/Vulpeus-Server/kyoyu)](https://github.com/Vulpeus-Server/kyoyu)

> 共有 -> **Kyōyū [kʲo̞ːjɯ̟ː]**<br>
> ≒ Sharing

This mod shares .litematic files with other players.<br>
Litematica support. Inspired by Syncmatica.

<strong><font color=#ff223a>Do not report Kyoyu issues to Syncmatica!</font></strong>

## Usage (WIP)

### Client Side

1. Install `Fabric` or (`Forge`)<a><sup>[1]</sup></a> to the instance.
2. Add the Kyoyu Mod and its dependency mods to the Mods folder.
3. ! This process will no longer be necessary in future updates !<br>
   Create a `kyoyu` folder in the `schematic` folder. (`~/[instance]/schematic/kyoyu`)
4. Launch!

<a>[1]</a>: 1.16.5 Only

<b><font color=#F9AA00>It is possible to join a server without Kyoyu installed using a client with Kyoyu, but you will not be able to use Kyoyu's features!</font></b>

### Server Side

1. Install `Fabric`, `Forge`, `NeoForge`, or `Paper` to the server.
2. Add the Kyoyu Mod and its dependency mods to the Mods folder.
3. ! This process will no longer be necessary in future updates !<br>
   Create a `kyoyu` folder in the server folder and create a `files` folder in the `kyoyu` folder. (`~/[server]/kyoyu/files`)
4. Start the server!

<b><font color=#F9AA00>It is possible to join a server with Kyoyu installed using a Vanilla client or an instance without Kyoyu, but you will not be able to use Kyoyu's features!</font></b>

## Dependencies

| Loader        | Client                          | Server     |
|---------------|---------------------------------|------------|
| **Fabric**    | Malilib, Litematica, Fabric API | Fabric API |
| **Forge**     | Malilib, Litematica             |            |

## Versions

| Version          | Client     |           |｜| Server     |           |              |           |
|------------------|------------|-----------|--|------------|-----------|--------------|-----------|
|                  | **Fabric** | **Forge** |｜| **Fabric** | **Forge** | **NeoForge** | **Paper** |
| 1.14 - 1.14.4    | ✅        | 🚫        |｜| ✅        | ✅        | 🚫          | 🚫       |
| 1.15 - 1.15.2    | ✅        | 🚫        |｜| ✅        | ✅        | 🚫          | 🚫       |
| 1.16 - 1.16.5    | ✅        | ✅<a><sup>[2]</sup></a>|｜| ✅        | ✅        | 🚫          | 🚫       |
| 1.17 - 1.17.1    | ✅        | 🚫        |｜| ✅        | ✅        | 🚫          | 🚫       |
| 1.18 - 1.18.2    | ✅        | 🚫        |｜| ✅        | ✅        | 🚫          | 🚫       |
| 1.19 - 1.19.4    | ✅        | 🚫        |｜| ✅        | ✅        | 🚫          | ⚠️       |
| 1.20.1 - 1.20.4  | ✅        | 🚫        |｜| ✅        | ✅        | 🚫          | ⚠️       |
| 1.20.5 - 1.20.6  | ✅        | 🚫        |｜| ✅        | ✅        | ✅          | ⚠️       |
| 1.21 - 1.21.3    | ✅        | 🚫        |｜| ✅        | ✅        | ✅          | ⚠️       |
| 1.21.4           | ✅        | 🚫        |｜| ✅        | ✅        | ✅          | 🚫       |
||<img src="https://raw.githubusercontent.com/FabricMC/fabric/refs/heads/1.21.4/src/main/resources/assets/fabric/icon.png" width="18px" alt="Fabric">|<img src="https://raw.githubusercontent.com/MinecraftForge/MinecraftForge/refs/heads/1.21.x/icon.ico" width="18px" alt="Forge">||<img src="https://raw.githubusercontent.com/FabricMC/fabric/refs/heads/1.21.4/src/main/resources/assets/fabric/icon.png" width="18px" alt="Fabric">|<img src="https://raw.githubusercontent.com/MinecraftForge/MinecraftForge/refs/heads/1.21.x/icon.ico" width="18px" alt="Forge">|<img src="https://raw.githubusercontent.com/neoforged/NeoForge/ac7a3bc021d604509763dd8d310aefc8fc4a4039/.idea/icon.svg" width="18px" alt="NeoForge">|<img src="https://forums.papermc.io/data/assets/logo/paperlogo512.png" width="18px" alt="Paper">|

<a>[2]</a>: This is only compatible with <a href="https://modrinth.com/mod/litematica/versions?g=1.16.5&l=forge" target="_blank">builds published by Litematica</a>

## Features (WIP)

### 　

- [x] Error Handling
- [x] Code Format and Review
- [ ] Client only depend (malilib, litematica)
- [ ] legacy litematica support?
- [ ] Forgematica support?
- [ ] Plugin support?

#### Network

- [x] Handshake Packet
- [x] FileRequest Packet
- [x] FileResponce Packet
- [x] LoadExplorer Packet
- [x] PlacementMeta Packet
- [x] RemovePlacement Packet

#### Client

- [x] Kyoyu button on placements menu
- [x] Kyoyu in-memory placement
- [x] Server Kyoyu placements explorer
  - [x] Download
  - [x] Load
  - [x] Material List
  - [x] Remove
- [x] Modify
- [x] Unload
- [x] Relogin compatibility
- [x] on Placement Update Event

#### Server

- [x] Save KyoyuPlacementMeta
- [x] Restore KyoyuPlacementMeta

#### Data Types

- [x] Pos
- [x] Mirror
- [x] Rotation
- [x] Ignore Entities
- [x] Placement ON/OFF
- [x] Subregion
  - [x] Pos
  - [x] Mirror
  - [x] Rotation
  - [x] Ignore Entities
  - [x] Placement ON/OFF

## Thanks

**Inspirations 🔖**<br>
⛓️ [Syncmatica](https://github.com/End-Tech/syncmatica) by nnnik, kpzip, and sakura-ryoko<br>

**Uses ⭐**<br>
🍤 [Unitemplate](https://github.com/topi-banana/unitemplate/tree/stonecutter) by topi-banana<br>
🪨 [Stonecutter](https://stonecutter.kikugie.dev/) by kikugie<br>
🏗️ [Architectury Loom](https://github.com/architectury/architectury-loom) by architectury<br>
🇾 [yamlang](https://github.com/Fallen-Breath/yamlang) by Fallen-Breath <br>
🇬 [Gson](https://github.com/google/gson) by google<br>

<small>Last README Update `2025/1/5 18:37` UTC+9</small>
