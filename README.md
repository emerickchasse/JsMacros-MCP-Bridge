# JsMacros MCP Bridge

[![Minecraft](https://img.shields.io/badge/Minecraft-26.1.2-62B47A)](https://www.minecraft.net/)
[![Fabric](https://img.shields.io/badge/Loader-Fabric-DBD0B4)](https://fabricmc.net/)
[![MCP](https://img.shields.io/badge/MCP-Streamable_HTTP-7C3AED)](https://modelcontextprotocol.io/)
[![License](https://img.shields.io/badge/License-MPL--2.0-blue)](LICENSE)

**A programmable MCP bridge embedded directly in the Minecraft client.**

JsMacros MCP Bridge exposes the complete [JsMacros](https://jsmacros.wagyourtail.xyz/) JavaScript runtime to Claude Code and other MCP clients through the official MCP Java SDK.

It deliberately exposes one tool:

```text
execute_mc_jsmacros(script, timeout_ms?)
```

Instead of maintaining one MCP tool per Minecraft action, the agent composes actions as code. Scripts can inspect and control the real client, including modded screens, inventories, entities, chat, events, and the normal JsMacros APIs.

> [!IMPORTANT]
> This is a complete JsMacros distribution with the MCP bridge built in. Remove the original JsMacros JAR before installing it; both use the `jsmacros` mod ID and cannot be installed together.

## Requirements

- Minecraft 26.1.2
- Fabric Loader 0.19.3 or newer
- Fabric API for Minecraft 26.1.2
- Cloth Config 26.1.154 or newer
- Mod Menu 18.x (recommended for configuration)
- Java 25 for Minecraft

## Install

1. Download the Fabric JAR from [GitHub Releases](https://github.com/emerickchasse/JsMacros-MCP-Bridge/releases).
2. Remove any existing `jsmacros-*.jar` from the Minecraft `mods` directory.
3. Copy `jsmacros-mcp-26.1.2-*-fabric.jar` into `mods`.
4. Install Fabric API, Cloth Config, and optionally Mod Menu.
5. Start Minecraft. The bridge starts automatically at `http://127.0.0.1:25580/mcp`.

## Connect Claude Code

Run this in the project where Claude Code should use Minecraft:

```bash
claude mcp add --transport http jsmacros http://127.0.0.1:25580/mcp
claude mcp list
```

Minecraft must remain open while the MCP client is connected.

To verify the port from PowerShell:

```powershell
Test-NetConnection 127.0.0.1 -Port 25580
```

## Tool API

### `execute_mc_jsmacros`

| Argument | Type | Required | Description |
| --- | --- | --- | --- |
| `script` | string | Yes | JavaScript function body executed inside JsMacros |
| `timeout_ms` | integer | No | Timeout from 1,000 to 600,000 ms |

Use `return` to send a JSON-serializable result back to the MCP client:

```js
const player = Player.getPlayer();
const screen = Hud.getOpenScreen();

return {
  player: player ? player.getName().getString() : null,
  screen: screen ? screen.getClass().getName() : null,
  position: player ? {
    x: player.getX(),
    y: player.getY(),
    z: player.getZ()
  } : null
};
```

Normal JsMacros globals such as `Client`, `Player`, `World`, `Hud`, `Chat`, `Reflection`, and inventory/GUI helpers are available.

## Configuration

Open **Mods → JsMacros MCP Bridge → Configure**. Settings follow the selected Minecraft language; English and French translations are included.

| Setting | Default |
| --- | --- |
| Enabled | `true` |
| Bind address | `127.0.0.1` |
| Port | `25580` |
| Endpoint | `/mcp` |
| API key | empty |
| Default timeout | 30 seconds |

The configuration is stored in `config/jsmacros-mcp.json`. Saving it from Mod Menu restarts the embedded MCP endpoint automatically.

If an API key is configured, pass it to Claude Code:

```bash
claude mcp add --transport http jsmacros http://127.0.0.1:25580/mcp \
  --header "Authorization: Bearer YOUR_KEY"
```

## Build from source

Gradle runs on Java 21 and compiles the Minecraft 26.1.2 target with a Java 25 toolchain:

```bash
./gradlew remapFabricJar
```

The distributable JAR is written to `build/libs/`.

## Architecture

- Fabric loads JsMacros and the bridge in the Minecraft client.
- Embedded Tomcat exposes the official MCP Java SDK Streamable HTTP servlet.
- The MCP server registers exactly one tool.
- Tool calls execute through JsMacros' own JavaScript runtime and return JSON-compatible values.

## Upstream and license

This project is based on [grepsedawk/JSMacros](https://github.com/grepsedawk/JSMacros), itself a fork of [WagYourTail/JsMacros](https://github.com/wagyourtail/JsMacros). The Minecraft 26.1 port includes work by Pablete1234 and rendering work by Jack Manning.

Licensed under [MPL-2.0](LICENSE).
