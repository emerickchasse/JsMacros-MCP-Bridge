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
