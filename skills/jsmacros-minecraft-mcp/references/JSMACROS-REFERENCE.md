# JsMacros Reloaded 2.0.3 — Searchable Reference

```text
Target: Minecraft 26.1.2 / Fabric / JsMacros Reloaded 2.0.3 / JavaScript
Installed skill: .claude/skills/jsmacros-minecraft-26-1-2
```

```bash
REF=.claude/skills/jsmacros-minecraft-26-1-2/references/JSMACROS-REFERENCE.md

grep -n -E 'takeScreenshot|ArithmeticException|runOnMainThread|RenderSystem|Rendersystem' "$REF"
grep -n -E 'GlobalVars|putString|getString|callback' "$REF"
grep -n -E 'screenshots/screenshots|Couldn.t save screenshot|macroFolder' "$REF"
grep -n -E 'InteractionManager|interactBlock|breakBlock|attack' "$REF"
grep -n -E 'Inventory|findItem|swapHotbar|getMap|getSlots' "$REF"
grep -n -E 'getNBT|registry|2\.0\.3|26\.1\.2|26\.2' "$REF"
```

```bash
sed -n 'START,ENDp' "$REF"
```


# ===== API SIGNATURES =====

# API Signatures

Target: JsMacros Reloaded 2.0.3, Minecraft 26.1.2.

## Globals

```text
Chat Client FS GlobalVars Hud JavaWrapper JsMacros KeyBind Player Reflection Request Time World
event file context
```

## Client

```text
Client.getMinecraft()
Client.getRegistryManager()
Client.getGameOptions()
Client.mcVersion(): String
Client.getFPS(): String
Client.waitTick(): void
Client.waitTick(ticks: int): void
Client.runOnMainThread(runnable): void
Client.runOnMainThread(runnable, watchdogMaxTime: long): void
Client.runOnMainThread(runnable, await: boolean, watchdogMaxTime: long): void
Client.connect(ip: String): void
Client.connect(ip: String, port: int): void
Client.loadWorld(folderName: String): void
Client.isModLoaded(modId: String): boolean
Client.getClipboard(): String
Client.setClipboard(text: String): void
```

`runOnMainThread` source behavior:

```text
mc.isSameThread() => runnable.run()
joined-thread stack => IllegalThreadStateException
other thread => mc.execute(runnable)
await=true => caller waits on semaphore
```

## Player

```text
Player.getPlayer(): ClientPlayerEntityHelper | null
Player.openInventory(): Inventory
Player.getInteractionManager(): InteractionManagerHelper | null
Player.interactions(): InteractionManagerHelper | null
Player.getGameMode(): String
Player.setGameMode(gameMode: String): void
Player.getReach(): double
Player.rayTraceBlock(distance: double, fluid: boolean): BlockDataHelper | null
Player.detailedRayTraceBlock(distance: double, fluid: boolean)
Player.rayTraceEntity(distance: int): EntityHelper | null
Player.takeScreenshot(folder: String, callback): void
  target-runtime status: verified working

Player.takeScreenshot(folder: String, file: String, callback): void
  target-runtime status: verified ArithmeticException: / by zero

Player.takePanorama(folder: String, width: int, height: int, callback): void
Player.createPlayerInput()
Player.createPlayerInput(movementForward, movementSideways, yaw)
Player.createPlayerInput(movementForward, yaw, jumping, sprinting)
Player.createPlayerInput(movementForward, movementSideways, yaw, pitch, jumping, sneaking, sprinting)
Player.getCurrentPlayerInput()
Player.addInput(input): void
Player.addInputs(inputs): void
Player.clearInputs(): void
Player.moveForward(yaw): void
Player.moveBackward(yaw): void
Player.moveStrafeLeft(yaw): void
Player.moveStrafeRight(yaw): void
```

## ClientPlayerEntityHelper

```text
p.getX(): double
p.getY(): double
p.getZ(): double
p.getPos()
p.getBlockPos()
p.getEyePos()
p.getYaw(): float
p.getPitch(): float
p.getName()
p.getType()
p.getNBT()
p.setPos(...)
p.addPos(...)
p.lookAt(direction: String)
p.lookAt(yaw: float, pitch: float)
p.lookAt(x: double, y: double, z: double)
```

## InteractionManagerHelper

```text
im.setTarget(x, y, z)
im.setTarget(x, y, z, direction)
im.setTarget(pos)
im.setTarget(entity)
im.getTarget()
im.getTargetedBlock()
im.getTargetedEntity()
im.setTargetMissed()
im.hasTargetOverride(): boolean
im.clearTargetOverride(): void

im.attack()
im.attack(await: boolean)
im.attack(entity)
im.attack(entity, await)
im.attack(x, y, z, direction)
im.attack(x, y, z, direction, await)

im.breakBlock()
im.breakBlock(x, y, z)
im.breakBlock(pos)
im.breakBlockAsync(callback)
im.isBreakingBlock(): boolean
im.cancelBreakBlock(): void

im.interact()
im.interact(await)
im.interactEntity(entity, offHand)
im.interactEntity(entity, offHand, await)
im.interactItem(offHand)
im.interactItem(offHand, await)
im.interactBlock(x, y, z, direction, offHand, await)
```

## World

```text
World.getPlayers()
World.getPlayerEntry(name)
World.getBlock(x, y, z): BlockDataHelper
World.getBlock(pos): BlockDataHelper
World.getChunk(...)
World.getWorldScanner()
World.findBlocksMatching(...)
World.getEntities()
World.iterateSphere(...)
World.getScoreboards()
World.getDimension(): String
World.getBiome(): String
World.getTime(): long
World.isChunkLoaded(...)
```

## BlockDataHelper

```text
block.getX(): int
block.getY(): int
block.getZ(): int
block.getId(): String
block.getName()
block.getNBT()
block.getBlockStateHelper()
```

## EntityHelper

```text
entity.getX(): double
entity.getY(): double
entity.getZ(): double
entity.getPos()
entity.getBlockPos()
entity.getEyePos()
entity.getYaw(): float
entity.getPitch(): float
entity.getName()
entity.getType()
entity.getNBT()
entity.rayTraceBlock(...)
entity.rayTraceEntity(distance)
```

## Inventory

```text
inv.click(slot)
inv.click(slot, mouseButton)
inv.dragClick(slots, mouseButton)
inv.dropSlot(slot)
inv.contains(item)
inv.findFreeInventorySlot()
inv.findFreeHotbarSlot()
inv.findFreeSlot(...)
inv.getItemCount()
inv.getItems()
inv.findItem(item)
inv.getSlots(...)
inv.getSelectedHotbarSlotIndex(): int
inv.setSelectedHotbarSlotIndex(index)
inv.getHeld()
inv.getSlot(slot)
inv.getTotalSlots(): int
inv.getMap()
inv.getLocation()
inv.quick(slot)
inv.quickAll(...)
inv.close()
inv.closeAndDrop()
inv.swapHotbar(slot, hotbarSlot)
```

## KeyBind

```text
KeyBind.getKeyBindings()
KeyBind.setKeyBind(bind, key)
KeyBind.key(keyName, state)
KeyBind.pressKey(keyName)
KeyBind.releaseKey(keyName)
KeyBind.keyBind(binding, state)
KeyBind.pressKeyBind(binding)
KeyBind.releaseKeyBind(binding)
KeyBind.getPressedKeys()
```

`keyBind(binding, true)` calls Minecraft key mapping click logic and marks the key down.

## Hud

```text
Hud.createScreen(...)
Hud.openScreen(screen)
Hud.getOpenScreen()
Hud.getOpenScreenName()
Hud.isContainer()
Hud.createDraw2D()
Hud.createDraw3D()
```

Draw objects expose registration methods:

```text
draw.register()
draw.unregister()
```

## Chat

```text
Chat.log(message)
Chat.log(message, await)
Chat.logf(format, ...args)
Chat.say(message)
Chat.say(message, await)
Chat.sayf(format, ...args)
Chat.open(message)
Chat.title(title, subtitle, fadeIn, remain, fadeOut)
Chat.actionbar(message)
Chat.toast(title, description)
Chat.getHistory()
Chat.getTextWidth(text)
Chat.getCommandManager()
```

`Chat.say("/command")` reaches Minecraft command sending logic.

## JsMacros / callbacks

```text
JsMacros.on(eventName, callback)
JsMacros.on(eventName, filter, callback)
JsMacros.once(eventName, callback)
JsMacros.off(listener)
JsMacros.off(eventName, listener)
JsMacros.waitForEvent(...)
JsMacros.runScript(...)
```

JavaScript function wrapper:

```js
JavaWrapper.methodToJava(() => {
  // callback body
})
```


## GlobalVars

```text
GlobalVars.putString(name: String, str: String): String
GlobalVars.getString(name: String): String | null
GlobalVars.putInt(name: String, value: int): int
GlobalVars.getInt(name: String): Integer | null
GlobalVars.putDouble(name: String, value: double): double
GlobalVars.getDouble(name: String): Double | null
GlobalVars.putBoolean(name: String, value: boolean): boolean
GlobalVars.getBoolean(name: String): Boolean | null
GlobalVars.putObject(name: String, value: Object): Object
GlobalVars.getObject(name: String): Object
GlobalVars.remove(name: String): void
```

Implementation storage:

```java
public Map<String, Object> globalRaw = new ConcurrentHashMap<>();
```

Observed screenshot callback transport:

```js
GlobalVars.putString("cb", "pending");

// callback:
GlobalVars.putString("cb", String(result));

// script thread:
String(GlobalVars.getString("cb"))
```

# ===== PATTERNS =====

# Patterns

Target: JsMacros Reloaded 2.0.3 / Minecraft 26.1.2.

## Player + target snapshot

```js
const p = Player.getPlayer();
const reach = p ? Player.getReach() : 0;
const b = p ? Player.rayTraceBlock(reach, false) : null;
const e = p ? Player.rayTraceEntity(Math.floor(reach)) : null;

return {
  player: p ? {
    x: p.getX(), y: p.getY(), z: p.getZ(),
    yaw: p.getYaw(), pitch: p.getPitch()
  } : null,
  block: b ? {
    id: String(b.getId()),
    x: b.getX(), y: b.getY(), z: b.getZ()
  } : null,
  entity: e ? {
    type: String(e.getType()),
    name: String(e.getName()),
    x: e.getX(), y: e.getY(), z: e.getZ()
  } : null
};
```

## Exact block

```js
const b = World.getBlock(100, 64, -30);
return {
  id: String(b.getId()),
  name: String(b.getName()),
  x: b.getX(),
  y: b.getY(),
  z: b.getZ(),
  nbt: b.getNBT() == null ? null : String(b.getNBT())
};
```

## Screenshot from executor script thread — verified sequence

Output directory from the verified run:

```text
<macroFolder>/screenshots/screenshots/
```

Preparation:

```bash
mkdir -p \
  "/c/Users/Emerick/AppData/Roaming/.minecraft/config/jsMacros/Macros/screenshots/screenshots"
```

JsMacros:

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

Observed result:

```text
TextHelper:{"text": "Saved screenshot as 2026-08-15_04.25.07.png"}
```

Observed file:

```text
C:\Users\Emerick\AppData\Roaming\.minecraft\config\jsMacros\Macros\
screenshots\screenshots\2026-08-15_04.25.07.png
```

Observed overload matrix:

```text
(folder, callback)       => screenshot saved
(folder, file, callback) => ArithmeticException: / by zero
```

Observed missing-directory result:

```text
Couldn't save screenshot: <full path>
```

That message arrived through the callback.

## Command + client-visible state

```js
const p0 = Player.getPlayer();
const before = p0 ? { x:p0.getX(), y:p0.getY(), z:p0.getZ() } : null;

Chat.say("/hub", true);
Client.waitTick(20);

const p1 = Player.getPlayer();
const after = p1 ? { x:p1.getX(), y:p1.getY(), z:p1.getZ() } : null;

return { before, after };
```

## Break block + read-back

```js
const x = 100, y = 64, z = -30;
const before = String(World.getBlock(x,y,z).getId());

const p = Player.getPlayer();
p.lookAt(x + 0.5, y + 0.5, z + 0.5);
Client.waitTick(1);

Player.getInteractionManager().breakBlock(x,y,z);
Client.waitTick(3);

const after = String(World.getBlock(x,y,z).getId());
return { before, after, x, y, z };
```

## Inventory lookup + hotbar

```js
const inv = Player.openInventory();
const slots = inv.findItem("minecraft:stone");

return {
  slots: Array.from(slots),
  map: String(inv.getMap()),
  selected: inv.getSelectedHotbarSlotIndex()
};
```

Move first matching item to hotbar index 0:

```js
const inv = Player.openInventory();
const slots = inv.findItem("minecraft:stone");

if (slots.length > 0) {
  inv.swapHotbar(slots[0], 0);
  inv.setSelectedHotbarSlotIndex(0);
}

return {
  found: slots.length,
  selected: inv.getSelectedHotbarSlotIndex()
};
```

## Block-face interaction

```js
Player.getInteractionManager().interactBlock(
  100, 64, -30,
  "up",
  false,
  true
);
```

## Movement key state

```js
KeyBind.keyBind("key.forward", true);
Client.waitTick(10);
KeyBind.keyBind("key.forward", false);
```

## Tick listener

```js
const listener = JsMacros.on(
  "Tick",
  JavaWrapper.methodToJava(() => {
    // callback
  })
);

// later:
JsMacros.off(listener);
```

## Draw2D lifecycle

```js
const d2d = Hud.createDraw2D();
d2d.register();

// later:
d2d.unregister();
```

## Width probe for resource-pack glyph

```js
return {
  glyph: Chat.getTextWidth(""),
  asciiA: Chat.getTextWidth("A")
};
```

Client text measurement can distinguish a resource-pack glyph from a fallback/missing glyph when their rendered widths differ.

# ===== RUNTIME FACTS =====

# Runtime Facts and Edge Behavior

## JavaScript runtime

JsMacros JavaScript runs as a guest language embedded in Java. JsMacros libraries are injected globals.

Official docs describe the JavaScript environment as distinct from Node.js. Java interop is available through the guest runtime and JsMacros helpers.

## Player presence

`Player.getPlayer()` returns `null` when `mc.player` is null.

Source: `FPlayer.java`.

## Screenshot overload behavior

Source:

```text
2-argument overload:
Screenshot.grab(baseDir, renderTarget, callback)

3-argument overload:
Screenshot.grab(baseDir, file, renderTarget, 0, callback)
```

Target-runtime observations:

```text
2-argument overload: successful capture
3-argument overload: java.lang.ArithmeticException: / by zero
```

## Screenshot path

`FPlayer` supplies this base:

```text
new File(runner.config.macroFolder, folder)
```

Target runtime with:

```js
Player.takeScreenshot("screenshots", callback)
```

produced:

```text
<macroFolder>/screenshots/screenshots/<timestamp>.png
```

Observed concrete path:

```text
C:\Users\Emerick\AppData\Roaming\.minecraft\config\jsMacros\Macros\
screenshots\screenshots\2026-08-15_04.25.07.png
```

The second `screenshots` segment came from the downstream screenshot helper.

The directory tree existed before the successful capture.

Missing directory callback text:

```text
Couldn't save screenshot: <full path>
```

No exception was observed for that failure path.


## `runOnMainThread`

Source branches:

```text
mc.isSameThread()        => runnable.run()
joined thread stack      => IllegalThreadStateException
other script thread      => mc.execute(...)
await=true               => semaphore wait until runnable finishes
```

Exact exception string in source:

```text
Attempted to wait on main thread while currently joined to main!
```

## `waitTick`

`Client.waitTick(...)` synchronizes with client ticks. Thread context affects whether a blocking wait is possible.

## Interaction manager

`Player.getInteractionManager()` wraps `mc.gameMode`. It returns `null` when `mc.gameMode` is null.

## Game mode

`Player.setGameMode(String)` calls:

```text
mc.gameMode.setLocalMode(...)
```

This is local interaction-manager state in the client implementation.

## Inventory

`Inventory.create()` maps the currently open Minecraft screen to inventory helper subclasses.

`findItem(...)` returns matching slot indexes.

`swapHotbar(slot, hotbarSlot)` maps an inventory slot into a selected hotbar position.

Container slot numbering comes from the current `AbstractContainerMenu`.

## KeyBind

`KeyBind.keyBind(binding, true)` iterates `mc.options.keyMappings`, calls `KeyMapping.click(...)`, then `key.setDown(true)`.

`pressKeyBind(binding)` is a thin call to `keyBind(binding, true)`.

## Java helper serialization

JsMacros calls return Java helper objects, Java collections, text helpers, IDs, arrays, and primitives. Primitive extraction produces compact JSON:

```js
{
  id: String(block.getId()),
  x: block.getX(),
  y: block.getY(),
  z: block.getZ()
}
```

## World query scale

`World.getEntities()` can expose many entities. World scanners can inspect chunks/regions. Result size follows scan scope.

## Version line

Release `v2.0.3` is titled:

```text
JsMacros Reloaded 2.0.3 for Minecraft 26.1.2
```

Reloaded 2.1.x is the Minecraft 26.2 line.

## Item NBT

Reloaded 2.0.3 release notes include an item `getNBT()` fix involving registry references.


## Screenshot callback marshalling

Observed executor behavior:

```text
callback assignment into script-local JS variable
=> script later read the original/null value

callback GlobalVars.putString(...)
=> script GlobalVars.getString(...) read the callback result
```

Source storage:

```text
ConcurrentHashMap<String,Object>
```

Working callback bridge:

```js
GlobalVars.putString("cb", "pending");

JavaWrapper.methodToJava((r) => {
  GlobalVars.putString("cb", String(r));
});
```

## Screenshot keybind observation

Observed:

```js
KeyBind.pressKeyBind("key.screenshot")
```

Result:

```text
pressed state reported
no screenshot file found
```

## Resource-pack glyph measurement

Observed:

```js
Chat.getTextWidth(String.fromCharCode(0xE01C))
```

Values:

```text
6   missing/fallback glyph
191 loaded resource-pack glyph
```

The render-sensitive width measurement was routed through the main-thread path in the working investigation.

# ===== TROUBLESHOOTING DATA =====

# Troubleshooting Data

## `RenderSystem called from wrong thread`

Observed direct executor-thread screenshot:

```text
java.lang.IllegalStateException: Rendersystem called from wrong thread
```

Observed working dispatch:

```js
Client.runOnMainThread(JavaWrapper.methodToJava(() => {
  Player.takeScreenshot("screenshots",
    JavaWrapper.methodToJava((r) => {
      GlobalVars.putString("cb", String(r));
    }));
}), true, 8000);
```

Source symbols:

```text
FPlayer.takeScreenshot
Screenshot.grab
mc.getMainRenderTarget
FClient.runOnMainThread
```


## `Attempted to wait on main thread while currently joined to main!`

Thrown by `Client.runOnMainThread(..., await, ...)` when the JsMacros profile reports a joined main-thread stack.

Source:

```text
FClient.java
```

## `Player.getPlayer()` => null

Source condition:

```java
if (mc.player == null) {
    return null;
}
```

Useful context values:

```js
return {
  player: Player.getPlayer() == null ? null : "present",
  screen: String(Hud.getOpenScreenName())
};
```

## Interaction changes no block state

Useful measurements:

```js
const p = Player.getPlayer();
return {
  pos: p ? [p.getX(), p.getY(), p.getZ()] : null,
  reach: p ? Player.getReach() : null,
  target: p ? String(Player.rayTraceBlock(Player.getReach(), false)) : null
};
```

Useful source symbols:

```text
InteractionManagerHelper.interactBlock
InteractionManagerHelper.breakBlock
Player.getReach
```

## Inventory slot uncertainty

Inventory structure:

```js
const inv = Player.openInventory();
return {
  map: String(inv.getMap()),
  total: inv.getTotalSlots(),
  items: String(inv.getItemCount())
};
```

Source:

```text
src/client/java/.../api/classes/inventory/Inventory.java
```

## Item `getNBT()` + registry references

Version fact:

```text
Reloaded 2.0.3 contains the registry-reference item getNBT fix.
```

Version probe:

```js
return {
  mc: Client.mcVersion()
};
```

## Helper object JSON output is sparse

Primitive projection example:

```js
const b = World.getBlock(x,y,z);
return {
  id: String(b.getId()),
  x: b.getX(),
  y: b.getY(),
  z: b.getZ()
};
```

## Screenshot file not under `.minecraft/screenshots`

`FPlayer.takeScreenshot` resolves the folder against:

```text
runner.config.macroFolder
```

Given:

```js
Player.takeScreenshot("screenshots", "gtc-menu.png", callback)
```

the source-derived path shape is:

```text
<macroFolder>/screenshots/gtc-menu.png
```


## `java.lang.ArithmeticException: / by zero` from named screenshot overload

Observed call shape:

```js
Player.takeScreenshot(folder, file, callback)
```

Observed exception:

```text
java.lang.ArithmeticException: / by zero
```

Source call in Reloaded 2.0.3:

```java
Screenshot.grab(
    new File(runner.config.macroFolder, folder),
    file,
    mc.getMainRenderTarget(),
    0,
    callback
);
```

Observed working call shape:

```js
Player.takeScreenshot(folder, callback)
```

## `Couldn't save screenshot: <path>`

Observed behavior:

```text
callback receives failure text
script call does not throw
```

Observed filesystem condition:

```text
target screenshot directory absent
```

Observed success after directory creation:

```bash
mkdir -p \
  "/c/Users/Emerick/AppData/Roaming/.minecraft/config/jsMacros/Macros/screenshots/screenshots"
```

## Screenshot callback value remains local/null

Observed:

```text
local JS variable assigned by screenshot callback
=> script thread read old/null value
```

Observed bridge:

```js
GlobalVars.putString("cb", "pending");

Client.runOnMainThread(JavaWrapper.methodToJava(() => {
  Player.takeScreenshot("screenshots",
    JavaWrapper.methodToJava((r) => {
      GlobalVars.putString("cb", String(r));
    }));
}), true, 8000);

Client.waitTick(25);

return {
  resultat: String(GlobalVars.getString("cb"))
};
```

## `key.screenshot` reports pressed with no file

Observed:

```js
KeyBind.pressKeyBind("key.screenshot")
```

Observed filesystem result:

```text
no screenshot file found
```

# ===== VERSION AND API CHANGES =====

# Version and API Changes

## Target pair

```text
Minecraft: 26.1.2
JsMacros Reloaded: 2.0.3
Fabric
```

Release title:

```text
JsMacros Reloaded 2.0.3 for Minecraft 26.1.2
```

## 2.1.x

Reloaded 2.1.x targets Minecraft 26.2.

## 2.0.3

Release notes include a fix for item `getNBT()` with registry references.

## 2.0.1

Ruby support is split into the separate `jsmacros-ruby` extension.

## Player interaction helpers

Current source exposes:

```js
Player.getInteractionManager()
Player.interactions()
```

Interaction methods live on `InteractionManagerHelper`.

Deprecated player-side helpers in the source point toward `InteractionManagerHelper`.

## HUD registration

Draw objects expose:

```js
draw.register()
draw.unregister()
```

Static HUD registration helpers are marked deprecated in current source.

## Inventory swap

`Inventory.swap(slot1, slot2)` is marked deprecated in current source.

`Inventory.swapHotbar(slot, hotbarSlot)` is present for hotbar movement.

## `rayTraceEntity`

Zero-argument `Player.rayTraceEntity()` is marked deprecated.

Current distance form:

```js
Player.rayTraceEntity(distance)
```

## Source comparison

Tag checkout:

```bash
git clone --depth 1 --branch v2.0.3 \
  https://github.com/grepsedawk/JSMacros.git \
  /tmp/jsmacros-v2.0.3
```

Deprecation search:

```bash
grep -RIn "@Deprecated" /tmp/jsmacros-v2.0.3/src/client/java/xyz/wagyourtail/jsmacros/client/api
```

Symbol search:

```bash
grep -RIn "getInteractionManager\|swapHotbar\|register()" \
  /tmp/jsmacros-v2.0.3/src/client/java/xyz/wagyourtail/jsmacros/client/api
```

# ===== SOURCE MAP =====

# Source Map

Repository:

```text
https://github.com/grepsedawk/JSMacros
tag: v2.0.3
```

Clone:

```bash
git clone --depth 1 --branch v2.0.3 \
  https://github.com/grepsedawk/JSMacros.git \
  /tmp/jsmacros-v2.0.3
```

## Libraries

```text
Chat
src/client/java/xyz/wagyourtail/jsmacros/client/api/library/impl/FChat.java

Client
src/client/java/xyz/wagyourtail/jsmacros/client/api/library/impl/FClient.java

Player
src/client/java/xyz/wagyourtail/jsmacros/client/api/library/impl/FPlayer.java

World
src/client/java/xyz/wagyourtail/jsmacros/client/api/library/impl/FWorld.java

Hud
src/client/java/xyz/wagyourtail/jsmacros/client/api/library/impl/FHud.java

KeyBind
src/client/java/xyz/wagyourtail/jsmacros/client/api/library/impl/FKeyBind.java
```

## Helpers

```text
InteractionManagerHelper
src/client/java/xyz/wagyourtail/jsmacros/client/api/helper/InteractionManagerHelper.java

Inventory
src/client/java/xyz/wagyourtail/jsmacros/client/api/classes/inventory/Inventory.java

BlockDataHelper
src/client/java/xyz/wagyourtail/jsmacros/client/api/helper/world/BlockDataHelper.java

ClientPlayerEntityHelper
src/client/java/xyz/wagyourtail/jsmacros/client/api/helper/world/entity/ClientPlayerEntityHelper.java
```

## Search recipes

```bash
SRC=/tmp/jsmacros-v2.0.3

grep -RIn "public .*takeScreenshot" "$SRC/src"
grep -RIn "public .*runOnMainThread" "$SRC/src"
grep -RIn "public .*interactBlock" "$SRC/src"
grep -RIn "public .*breakBlock" "$SRC/src"
grep -RIn "public .*findItem" "$SRC/src"
grep -RIn "public .*swapHotbar" "$SRC/src"
grep -RIn "public .*getNBT" "$SRC/src"
grep -RIn "@Deprecated" "$SRC/src/client/java/xyz/wagyourtail/jsmacros/client/api"
```

Read exact implementation:

```bash
sed -n '200,245p' \
  "$SRC/src/client/java/xyz/wagyourtail/jsmacros/client/api/library/impl/FPlayer.java"

sed -n '88,150p' \
  "$SRC/src/client/java/xyz/wagyourtail/jsmacros/client/api/library/impl/FClient.java"
```

## Official docs

```text
https://jsmacros.wagyourtail.xyz/
https://jsmacros.wagyourtail.xyz/libraries.html
```
