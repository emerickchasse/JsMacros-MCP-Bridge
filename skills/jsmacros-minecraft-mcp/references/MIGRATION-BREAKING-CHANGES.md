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
