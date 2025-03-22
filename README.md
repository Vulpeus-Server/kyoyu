# Kyoyu

[![License](https://img.shields.io/github/license/Vulpeus-Server/kyoyu)](http://www.gnu.org/licenses/lgpl-3.0.html)
[![Modrinth](https://img.shields.io/modrinth/dt/VozTPxB4?label=Modrinth%20Downloads)](https://modrinth.com/mod/VozTPxB4)
[![Discord](https://img.shields.io/discord/1331335879469563914?logo=Discord)](https://discord.gg/RcqXRsVcSr)

[![GitHub Workflow](https://github.com/Vulpeus-Server/kyoyu/actions/workflows/gradle.yml/badge.svg)](https://github.com/Vulpeus-Server/kyoyu/actions/workflows/gradle.yml)
[![GitHub Issues](https://img.shields.io/github/issues/Vulpeus-Server/kyoyu)](https://github.com/Vulpeus-Server/kyoyu/issues)
[![GitHub Created At](https://img.shields.io/github/created-at/Vulpeus-Server/kyoyu)](https://github.com/Vulpeus-Server/kyoyu)

> 共有 -> **Kyōyū [kʲo̞ːjɯ̟ː]**<br>
> ≒ Sharing

***[Discord server is now open!](https://discord.gg/RcqXRsVcSr)***

This mod shares .litematic files with other players.<br>
Litematica and Forgematica support. Inspired by Syncmatica.

This project is still a work in progress, so feel free to send your feature requests or report any issues on [GitHub](https://github.com/Vulpeus-Server/kyoyu/issues)! <br>
Just keep in mind that not everything may be implemented 😄

<strong><font color=#ff223a>Do not report Kyoyu issues to Syncmatica!</font></strong>

<video src="https://github.com/user-attachments/assets/74d1d7f8-9d13-4886-aa26-96ae8849e093" controls="true"></video>

## Usage (WIP)

### Client Side

1. Install `Fabric`, `Forge`, or `NeoForge` to the instance.
2. Add the Kyoyu Mod and its dependency mods to the Mods folder.
3. Launch!

<b><font color=#F9AA00>It is possible to join a server without Kyoyu installed using a client with Kyoyu, but you will not be able to use Kyoyu's features!</font></b>

### Server Side

1. Install `Fabric`, `Forge`, `NeoForge`, or `Paper` to the server.
2. Add the Kyoyu Mod and its dependency mods to the Mods folder.
3. Start the server!

<b><font color=#F9AA00>It is possible to join a server with Kyoyu installed using a Vanilla client or an instance without Kyoyu, but you will not be able to use Kyoyu's features!</font></b>

#### Config

[Documentation](https://github.com/Vulpeus-Server/kyoyu/blob/main/docs/config.md)

## Dependencies

| Loader        | Client                          | Server     |
|---------------|---------------------------------|------------|
| **Fabric**    | Malilib, Litematica, Fabric API | Fabric API |
| **Forge**     | MaFgLib, Forgematica            |            |
| **NeoForge**  | MaFgLib, Forgematica            |            |

## Versions

| Version         | Client |        |       |｜| Server |        |        |       |
|----------------:|:------:|:------:|:-----:|--|:------:|:------:|:------:|:-----:|
|                 | **Fabric** <br><font size="-1"><small>Litematica</small></font> | **Forge** <br><font size="-1"><small><a href="https://modrinth.com/mod/forgematica"  style="color: white;" target="_blank">Forgematica</a></small></font> | **NeoForge** <br><font size="-1"><small><a href="https://modrinth.com/mod/forgematica"  style="color: white;" target="_blank">Forgematica</a></small></font> |｜| **Fabric** | **Forge** | **NeoForge** | **Paper** |
|   1.14 - 1.14.4 | ✅    | ❌    | ❌    |｜| ✅    | ✅    | ❌    | ❌    |
|   1.15 - 1.15.2 | ✅    | ❌    | ❌    |｜| ✅    | ✅    | ❌    | ❌    |
|   1.16 - 1.16.5 | ✅    | ✅    | ❌    |｜| ✅    | ✅    | ❌    | ❌    |
|   1.17 - 1.17.1 | ✅    | ✅    | ❌    |｜| ✅    | ✅    | ❌    | ❌    |
|   1.18 - 1.18.2 | ✅    | ✅    | ❌    |｜| ✅    | ✅    | ❌    | ❌    |
|   1.19 - 1.19.2 | ✅    | ✅    | ❌    |｜| ✅    | ✅    | ❌    | ❌    |
| 1.19.3 - 1.19.4 | ✅    | ✅    | ❌    |｜| ✅    | ✅    | ❌    | ⚠️<a><sup>[3]</sup></a>|
|   1.20 - 1.20.1 | ✅    | ✅    | ❌    |｜| ✅    | ✅    | ❌    | ⚠️<a><sup>[3]</sup></a>|
|          1.20.2 | ✅    | ❌    | ❌    |｜| ✅    | ✅    | ❌    | ⚠️<a><sup>[3]</sup></a>|
| 1.20.3 - 1.20.4 | ✅    | ❌    | ⚠️<a><sup>[1]</sup></a>    |｜| ✅    | ❌    | ⚠️<a><sup>[1]</sup></a>| ⚠️<a><sup>[3]</sup></a>|
| 1.20.5 - 1.20.6 | ✅    | ❌    | ⚠️<a><sup>[2]</sup></a>    |｜| ✅    | ❌    | ⚠️<a><sup>[1]</sup></a>    | ⚠️<a><sup>[3]</sup></a>|
|   1.21 - 1.21.1 | ✅    | ❌    | ⚠️<a><sup>[2]</sup></a>    |｜| ✅    | ❌    | ⚠️<a><sup>[1]</sup></a>    | ⚠️<a><sup>[3]</sup></a>|
| 1.21.2 - 1.21.3 | ✅    | ❌    | ⚠️<a><sup>[2]</sup></a>    |｜| ✅    | ❌    | ⚠️<a><sup>[1]</sup></a>    | ⚠️<a><sup>[3]</sup></a>|
|          1.21.4 | ✅    | ❌    | ⚠️<a><sup>[2]</sup></a>    |｜| ✅    | ❌    | ⚠️<a><sup>[1]</sup></a>    | ⚠️<a><sup>[3]</sup></a>|

<a>[1]</a>: Doesn't work<br>
<a>[2]</a>: Cannot join the server<br>
<a>[3]</a>: It launches, but cannot share the schematic<br>

## Roadmap (WIP)

- [ ] [legacy litematica (Ornithe)](https://github.com/maruohon/litematica/tree/ornithe/1.12.2) support
- [x] [Forgematica](https://modrinth.com/mod/forgematica) support
- [ ] [Paper](https://papermc.io/) plugin support (retry)
- [ ] Client only depend (malilib, litematica)
- [ ] GUI improvements
- [x] Merged jars

## Thanks

**Inspirations 🔖**<br>
⛓️ [Syncmatica](https://github.com/End-Tech/syncmatica) by nnnik, kpzip, and sakura-ryoko<br>

**Uses ⭐**<br>
🍤 [Unitemplate](https://github.com/topi-banana/unitemplate/tree/stonecutter) by topi-banana<br>
🪨 [Stonecutter](https://stonecutter.kikugie.dev/) by kikugie<br>
🏗️ [Architectury Loom](https://github.com/architectury/architectury-loom) by architectury<br>
🇾 [yamlang](https://github.com/Fallen-Breath/yamlang) by Fallen-Breath <br>
🇬 [Gson](https://github.com/google/gson) by google<br>

<small>Last README Update `2025/03/02 14:48` UTC+9
