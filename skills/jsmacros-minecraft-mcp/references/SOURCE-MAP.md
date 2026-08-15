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
