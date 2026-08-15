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
