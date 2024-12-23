# Kyoyu

[![License](https://img.shields.io/github/license/Vulpeus-Server/kyoyu)](http://www.gnu.org/licenses/lgpl-3.0.html)
[![Modrinth](https://img.shields.io/modrinth/dt/VozTPxB4?label=Modrinth%20Downloads)](https://modrinth.com/mod/VozTPxB4)
[![Discord](https://img.shields.io/discord/1157213775791935539?logo=Discord)](https://discord.gg/tjayanzYMf)

[![GitHub Workflow](https://github.com/Vulpeus-Server/kyoyu/actions/workflows/gradle.yml/badge.svg)](https://github.com/Vulpeus-Server/kyoyu/actions/workflows/gradle.yml)
[![GitHub Issues](https://img.shields.io/github/issues/Vulpeus-Server/kyoyu)](https://github.com/Vulpeus-Server/kyoyu/issues)
[![GitHub Created At](https://img.shields.io/github/created-at/Vulpeus-Server/kyoyu)](https://github.com/Vulpeus-Server/kyoyu)

A Minecraft mod inspired by Syncmatica.<br>
Share litematics with other players on the server.

<strong> Do not report Kyoyu issues to Syncmatica </strong>

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
| 1.16 - 1.16.5    | ✅        | ✅<sup>[1]</sup>|｜| ✅        | ✅        | 🚫          | 🚫       |
| 1.17 - 1.17.1    | ✅        | 🚫        |｜| ✅        | ✅        | 🚫          | 🚫       |
| 1.18 - 1.18.2    | ✅        | 🚫        |｜| ✅        | ✅        | 🚫          | 🚫       |
| 1.19 - 1.19.4    | ✅        | 🚫        |｜| ✅        | ✅        | 🚫          | ⚠️       |
| 1.20.1 - 1.20.4  | ✅        | 🚫        |｜| ✅        | ✅        | 🚫          | ⚠️       |
| 1.20.5 - 1.20.6  | ✅        | 🚫        |｜| ✅        | ✅        | ✅          | ⚠️       |
| 1.21 - 1.21.3    | ✅        | 🚫        |｜| ✅        | ✅        | ✅          | ⚠️       |
||<img src="https://raw.githubusercontent.com/FabricMC/fabric/refs/heads/1.21.4/src/main/resources/assets/fabric/icon.png" width="18px" alt="Fabric">|<img src="https://raw.githubusercontent.com/MinecraftForge/MinecraftForge/refs/heads/1.21.x/icon.ico" width="18px" alt="Forge">||<img src="https://raw.githubusercontent.com/FabricMC/fabric/refs/heads/1.21.4/src/main/resources/assets/fabric/icon.png" width="18px" alt="Fabric">|<img src="https://raw.githubusercontent.com/MinecraftForge/MinecraftForge/refs/heads/1.21.x/icon.ico" width="18px" alt="Forge">|<img src="https://raw.githubusercontent.com/neoforged/NeoForge/ac7a3bc021d604509763dd8d310aefc8fc4a4039/.idea/icon.svg" width="18px" alt="NeoForge">|<img src="https://forums.papermc.io/data/assets/logo/paperlogo512.png" width="18px" alt="Paper">|

<small>[1] This is only compatible with <a href="https://modrinth.com/mod/litematica/versions?g=1.16.5&l=forge" target="_blank">builds published by Litematica</a></small>

## Features (WIP)

### 　

- [x] Error Handling
- [x] Code Format and Review
- [x] Client only depend (malilib, litematica)
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

- [x] Share button on Placements menu
- [x] Synced Litematics List UI
  - [x] Download
  - [x] Load
  - [x] Material List
  - [x] Remove
- [ ] Re-Lock (modify)
- [x] Unload
- [x] Relogin compatibility
- [x] on Placement Update Event

#### Server

- [x] Save SyncLitematicConfig
- [x] Restore SyncLitematicConfig

#### Data Types

- [x] Pos
- [x] Mirror
- [x] Rotation
- [ ] Ignore Entities
- [ ] Subregion
  - [x] Pos
  - [x] Mirror
  - [x] Rotation
  - [ ] Ignore Entities
  - [ ] Placement ON/OFF

## Thanks

**Inspirations 🔖**<br>
⛓️ [Syncmatica](https://github.com/End-Tech/syncmatica) by nnnik, kpzip, and sakura-ryoko<br>

**Uses ⭐**<br>
🪨 [Stonecutter](https://stonecutter.kikugie.dev/) by kikugie<br>
🍤 [Unitemplate](https://github.com/topi-banana/unitemplate/tree/stonecutter) by topi
