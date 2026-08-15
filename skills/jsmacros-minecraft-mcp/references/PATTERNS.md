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
