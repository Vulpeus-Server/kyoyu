# Kyoyu

[![License](https://img.shields.io/github/license/Vulpeus-Server/kyoyu.svg)](http://www.gnu.org/licenses/lgpl-3.0.html)
[![Issues](https://img.shields.io/github/issues/Vulpeus-Server/kyoyu.svg)](https://github.com/Vulpeus-Server/litematic-syncer/issues)
[![workflow](https://github.com/Vulpeus-Server/kyoyu/actions/workflows/gradle.yml/badge.svg)](https://github.com/Vulpeus-Server/litematic-syncer/actions/workflows/gradle.yml)
[![Modrinth](https://img.shields.io/modrinth/dt/VozTPxB4?label=Modrinth%20Downloads)](https://modrinth.com/mod/litematic-syncer)

## Versions

### server
| version | platforms                      | dependency         |
|---------|--------------------------------|--------------------|
| 1.14.4  | fabric, forge                  | `>=1.14 <=1.14.4`  |
| 1.15.2  | fabric, forge                  | `>=1.15 <=1.15.2`  |
| 1.16.5  | fabric, forge                  | `>=1.16 <=1.16.5`  |
| 1.17.1  | fabric, forge                  | `>=1.17 <=1.17.1`  |
| 1.18.2  | fabric, forge                  | `>=1.18 <=1.18.2`  |
| 1.19.4  | fabric, forge, paper           | `>=1.19 <=1.19.4`  |
| 1.20.4  | fabric, forge, paper           | `>=1.20 <=1.20.4`  |
| 1.20.6  | fabric, forge, neoforge, paper | `>1.20.4 <=1.20.6` |
| 1.21.3  | fabric, forge, neoforge, paper | `>=1.21 <=1.21.3`  |

### client
| version | platforms     | dependency         |
|---------|---------------|--------------------|
| 1.14.4  | fabric        | `>=1.14 <=1.14.4`  |
| 1.15.2  | fabric        | `>=1.15 <=1.15.2`  |
| 1.16.5  | fabric, forge | `>=1.16 <=1.16.5`  |
| 1.17.1  | fabric        | `>=1.17 <=1.17.1`  |
| 1.18.2  | fabric        | `>=1.18 <=1.18.2`  |
| 1.19.4  | fabric        | `>=1.19 <=1.19.4`  |
| 1.20.4  | fabric        | `>=1.20 <=1.20.4`  |
| 1.20.6  | fabric        | `>1.20.4 <=1.20.6` |
| 1.21.3  | fabric        | `>=1.21 <=1.21.3`  |


## Features (WIP)

- [ ] Error Handling
- [ ] Code Format and Review
- [x] Client only depend (malilib, litematica)
- network
- [x] Handshake Packet
- [x] SyncLitematic Packet
- [x] SyncLitematicConfig Packet
- [x] RequestLitematic Packet
- [x] RequestLitematicsList Packet
- [x] ResponceLitematicsList Packet
- [x] RemoveLitematicConfig Packet
- Client
- [x] Share button on Placements menu
- [x] Synced Litematics List UI
    - [x] Download
    - [x] Load
    - [x] Material List
    - [x] Remove
- [x] Re-Lock (modify)
- [ ] Unload
- [ ] Relogin compatibility
- [x] on Placement Update Event
- Server
- [x] Save SyncLitematicConfig
- [x] Restore SyncLitematicConfig
- [ ] Plugin Support?
- Data Types
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