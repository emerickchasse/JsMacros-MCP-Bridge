---
name: jsmacros-minecraft-mcp
description: "JsMacros Reloaded 2.0.3 reference for Minecraft client scripting: Player, World, Chat, Client, Hud, KeyBind, Inventory, InteractionManager, screenshots, events, callbacks, movement, block/entity inspection, plugin verification, and live client automation through execute_mc_jsmacros()."
license: MIT
compatibility: "Minecraft Java 26.1.2, Fabric, JsMacros Reloaded 2.0.3, JavaScript."
metadata:
  skillsmith_version: "1.0"
  generated_for: "coding-agents"
---

# JsMacros Reloaded 2.0.3 — Minecraft 26.1.2

## Environment

```text
Minecraft Java: 26.1.2
Loader: Fabric
Mod: JsMacros Reloaded
JsMacros version: 2.0.3
Guest language: JavaScript
Executor exposed by project: execute_mc_jsmacros()
Skill path: .claude/skills/jsmacros-minecraft-26-1-2
Main searchable reference:
  .claude/skills/jsmacros-minecraft-26-1-2/references/JSMACROS-REFERENCE.md
```

JsMacros injects libraries into the JavaScript global context:

```text
Chat
Client
FS
GlobalVars
Hud
JavaWrapper
JsMacros
KeyBind
Player
Reflection
Request
Time
World
```

Global context also includes:

```text
event
file
context
```

## grep + sed

One-file search target:

```bash
REF=.claude/skills/jsmacros-minecraft-26-1-2/references/JSMACROS-REFERENCE.md
```

Screenshot / render thread:

```bash
grep -n -E 'takeScreenshot|runOnMainThread|RenderSystem|Screenshot\.grab' "$REF"
```

Interaction:

```bash
grep -n -E 'InteractionManager|interactBlock|interactEntity|interactItem|breakBlock|attack' "$REF"
```

Inventory:

```bash
grep -n -E 'Inventory|findItem|swapHotbar|setSelectedHotbarSlotIndex|getMap|getSlots' "$REF"
```

World / entities:

```bash
grep -n -E 'rayTraceBlock|rayTraceEntity|World\.getBlock|World\.getEntities|findBlocksMatching' "$REF"
```

Events / callbacks / threading:

```bash
grep -n -E 'JsMacros\.on|JsMacros\.once|JavaWrapper|waitTick|main thread|callback' "$REF"
```

NBT / versions:

```bash
grep -n -E 'getNBT|registry|enchant|2\.0\.3|26\.1\.2|26\.2' "$REF"
```

Literal runtime error:

```bash
grep -n -F 'RenderSystem called from wrong thread' "$REF"
```

Read a matching area:

```bash
sed -n 'START,ENDp' "$REF"
```

Example:

```bash
sed -n '180,250p' "$REF"
```

## Source tree

Official repository/tag:

```text
https://github.com/grepsedawk/JSMacros
tag: v2.0.3
```

Local source checkout:

```bash
git clone --depth 1 --branch v2.0.3 \
  https://github.com/grepsedawk/JSMacros.git \
  /tmp/jsmacros-v2.0.3
```

Search source:

```bash
grep -RIn "takeScreenshot" /tmp/jsmacros-v2.0.3/src
grep -RIn "runOnMainThread" /tmp/jsmacros-v2.0.3/src
grep -RIn "interactBlock" /tmp/jsmacros-v2.0.3/src
grep -RIn "findItem" /tmp/jsmacros-v2.0.3/src
```

High-value source files:

```text
src/client/java/xyz/wagyourtail/jsmacros/client/api/library/impl/FPlayer.java
src/client/java/xyz/wagyourtail/jsmacros/client/api/library/impl/FClient.java
src/client/java/xyz/wagyourtail/jsmacros/client/api/library/impl/FChat.java
src/client/java/xyz/wagyourtail/jsmacros/client/api/library/impl/FWorld.java
src/client/java/xyz/wagyourtail/jsmacros/client/api/library/impl/FHud.java
src/client/java/xyz/wagyourtail/jsmacros/client/api/library/impl/FKeyBind.java
src/client/java/xyz/wagyourtail/jsmacros/client/api/helper/InteractionManagerHelper.java
src/client/java/xyz/wagyourtail/jsmacros/client/api/classes/inventory/Inventory.java
src/client/java/xyz/wagyourtail/jsmacros/client/api/helper/world/BlockDataHelper.java
src/client/java/xyz/wagyourtail/jsmacros/client/api/helper/world/entity/ClientPlayerEntityHelper.java
```

## Core calls

```js
Client.mcVersion()

const p = Player.getPlayer()
p.getX()
p.getY()
p.getZ()
p.getYaw()
p.getPitch()
p.lookAt(x, y, z)

Player.getReach()
Player.rayTraceBlock(distance, fluid)
Player.rayTraceEntity(distance)

World.getBlock(x, y, z)
World.getEntities()
World.getDimension()
World.getBiome()
World.getTime()

Hud.getOpenScreen()
Hud.getOpenScreenName()
Hud.isContainer()

const inv = Player.openInventory()
inv.findItem("minecraft:stone")
inv.getItems()
inv.getItemCount()
inv.getMap()
inv.getSlots("main", "hotbar")
inv.setSelectedHotbarSlotIndex(0)
inv.swapHotbar(sourceSlot, hotbarIndex)

const im = Player.getInteractionManager()
im.attack(...)
im.breakBlock(...)
im.interact(...)
im.interactEntity(...)
im.interactItem(...)
im.interactBlock(...)

Chat.log(...)
Chat.say(...)
Client.waitTick(ticks)
```

## Screenshot — runtime verified on this 26.1.2 client

Source signatures in Reloaded 2.0.3:

```js
Player.takeScreenshot(folder, callback)
Player.takeScreenshot(folder, file, callback)
```

Observed compatibility on the target runtime:

```text
Player.takeScreenshot(folder, callback)
  => works

Player.takeScreenshot(folder, file, callback)
  => java.lang.ArithmeticException: / by zero

direct call from executor script thread
  => java.lang.IllegalStateException: Rendersystem called from wrong thread

Client.runOnMainThread(callback, true, 8000)
  => works for screenshot dispatch

callback -> local JS variable
  => value remained unchanged in observed executor run

callback -> GlobalVars.putString / script -> GlobalVars.getString
  => works

missing output directory
  => callback text: Couldn't save screenshot: <full path>
  => no thrown exception observed

KeyBind.pressKeyBind("key.screenshot")
  => returned pressed state
  => no screenshot file observed
```

`FPlayer.java` source for the generated-name overload:

```java
Screenshot.grab(
    new File(runner.config.macroFolder, folder),
    mc.getMainRenderTarget(),
    callback
);
```

`FPlayer.java` source for the named-file overload:

```java
Screenshot.grab(
    new File(runner.config.macroFolder, folder),
    file,
    mc.getMainRenderTarget(),
    0,
    callback
);
```

Target-runtime output path shape for the generated-name overload:

```text
<macroFolder>/<folder>/screenshots/<generated-name>.png
```

Observed with `folder = "screenshots"`:

```text
C:\Users\Emerick\AppData\Roaming\.minecraft\config\jsMacros\Macros\
  screenshots\screenshots\2026-08-15_04.25.07.png
```

The nested target directory existed before the successful capture.

Verified executor sequence:

```js
GlobalVars.putString("cb", "pending");

Client.runOnMainThread(JavaWrapper.methodToJava(() => {
  Player.takeScreenshot(
    "screenshots",
    JavaWrapper.methodToJava((r) => {
      GlobalVars.putString("cb", String(r));
    })
  );
}), true, 8000);

Client.waitTick(25);

return {
  resultat: String(GlobalVars.getString("cb"))
};
```

Observed callback result:

```text
TextHelper:{"text": "Saved screenshot as 2026-08-15_04.25.07.png"}
```

Filesystem preparation used in the verified run:

```bash
mkdir -p \
  "/c/Users/Emerick/AppData/Roaming/.minecraft/config/jsMacros/Macros/screenshots/screenshots"
```

`GlobalVars` source implementation:

```text
globalRaw: ConcurrentHashMap<String,Object>
putString(name, value)
getString(name)
```

### Resource-pack glyph probe

Observed before screenshot capture:

```js
Chat.getTextWidth(String.fromCharCode(0xE01C))
```

Target-runtime measurements:

```text
6   => missing/fallback glyph observed
191 => resource-pack glyph observed
```

The successful pack flow included:

```text
/iapack <player>
```

The text-width probe also touched render-sensitive client code in the observed runtime and was executed through the main-thread path.

## Structured client probe

```js
const p = Player.getPlayer();
const reach = p ? Player.getReach() : 0;
const block = p ? Player.rayTraceBlock(reach, false) : null;
const entity = p ? Player.rayTraceEntity(Math.floor(reach)) : null;

return {
  mcVersion: Client.mcVersion(),
  screen: String(Hud.getOpenScreenName()),
  player: p ? {
    x: p.getX(),
    y: p.getY(),
    z: p.getZ(),
    yaw: p.getYaw(),
    pitch: p.getPitch(),
    gameMode: String(Player.getGameMode())
  } : null,
  world: p ? {
    dimension: String(World.getDimension()),
    biome: String(World.getBiome()),
    time: Number(World.getTime())
  } : null,
  targetBlock: block ? {
    id: String(block.getId()),
    x: block.getX(),
    y: block.getY(),
    z: block.getZ()
  } : null,
  targetEntity: entity ? {
    type: String(entity.getType()),
    name: String(entity.getName()),
    x: entity.getX(),
    y: entity.getY(),
    z: entity.getZ()
  } : null
};
```

## Reference files

```text
references/JSMACROS-REFERENCE.md
references/API-SIGNATURES.md
references/PATTERNS.md
references/GOTCHAS.md
references/TROUBLESHOOTING.md
references/MIGRATION-BREAKING-CHANGES.md
references/SOURCE-MAP.md
references/SOURCES.md
```
