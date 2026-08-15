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
