# Sources

Accessed: 2026-08-15

| Source | URL | Facts extracted | Confidence |
|---|---|---|---|
| Reloaded repository | https://github.com/grepsedawk/JSMacros | Project/fork, source tree, Fabric target | official |
| Release v2.0.3 | https://github.com/grepsedawk/JSMacros/releases/tag/v2.0.3 | Minecraft 26.1.2 pairing, 2.0.3 release facts | official |
| JsMacros docs | https://jsmacros.wagyourtail.xyz/ | scripting model and API docs | official |
| Libraries and Globals | https://jsmacros.wagyourtail.xyz/libraries.html | injected global libraries | official |
| FPlayer v2.0.3 | https://raw.githubusercontent.com/grepsedawk/JSMacros/v2.0.3/src/client/java/xyz/wagyourtail/jsmacros/client/api/library/impl/FPlayer.java | player, ray trace, screenshot, input, reach | official |
| FClient v2.0.3 | https://raw.githubusercontent.com/grepsedawk/JSMacros/v2.0.3/src/client/java/xyz/wagyourtail/jsmacros/client/api/library/impl/FClient.java | main-thread dispatch, mcVersion, connection, client API | official |
| FChat v2.0.3 | https://raw.githubusercontent.com/grepsedawk/JSMacros/v2.0.3/src/client/java/xyz/wagyourtail/jsmacros/client/api/library/impl/FChat.java | log, say, chat UI, text helpers | official |
| FWorld v2.0.3 | https://raw.githubusercontent.com/grepsedawk/JSMacros/v2.0.3/src/client/java/xyz/wagyourtail/jsmacros/client/api/library/impl/FWorld.java | blocks, entities, world queries | official |
| FHud v2.0.3 | https://raw.githubusercontent.com/grepsedawk/JSMacros/v2.0.3/src/client/java/xyz/wagyourtail/jsmacros/client/api/library/impl/FHud.java | screens and HUD | official |
| FKeyBind v2.0.3 | https://raw.githubusercontent.com/grepsedawk/JSMacros/v2.0.3/src/client/java/xyz/wagyourtail/jsmacros/client/api/library/impl/FKeyBind.java | key state and keybind behavior | official |
| InteractionManagerHelper v2.0.3 | https://raw.githubusercontent.com/grepsedawk/JSMacros/v2.0.3/src/client/java/xyz/wagyourtail/jsmacros/client/api/helper/InteractionManagerHelper.java | attack, break, interaction API | official |
| Inventory v2.0.3 | https://raw.githubusercontent.com/grepsedawk/JSMacros/v2.0.3/src/client/java/xyz/wagyourtail/jsmacros/client/api/classes/inventory/Inventory.java | slot maps, item search, clicks, hotbar | official |
| BlockDataHelper v2.0.3 | https://raw.githubusercontent.com/grepsedawk/JSMacros/v2.0.3/src/client/java/xyz/wagyourtail/jsmacros/client/api/helper/world/BlockDataHelper.java | block ID, coordinates, NBT | official |
| ClientPlayerEntityHelper v2.0.3 | https://raw.githubusercontent.com/grepsedawk/JSMacros/v2.0.3/src/client/java/xyz/wagyourtail/jsmacros/client/api/helper/world/entity/ClientPlayerEntityHelper.java | player position, rotation, lookAt | official |

| FGlobalVars v2.0.3 | https://raw.githubusercontent.com/grepsedawk/JSMacros/v2.0.3/src/core/java/xyz/wagyourtail/jsmacros/core/library/impl/FGlobalVars.java | ConcurrentHashMap global storage, putString/getString | official |

## Target runtime observations

Environment:

```text
Minecraft Java 26.1.2
JsMacros Reloaded 2.0.3
executor: execute_mc_jsmacros()
date observed: 2026-08-15
```

Verified:

```text
Player.takeScreenshot(folder, callback)
=> works

Player.takeScreenshot(folder, file, callback)
=> java.lang.ArithmeticException: / by zero

direct executor-thread screenshot
=> java.lang.IllegalStateException: Rendersystem called from wrong thread

Client.runOnMainThread(callback, true, 8000)
=> working screenshot dispatch

folder="screenshots"
=> <macroFolder>/screenshots/screenshots/<generated-name>.png

missing nested screenshot directory
=> callback: Couldn't save screenshot: <full path>
=> no thrown exception observed

callback -> local JS variable
=> stale/null value observed

callback -> GlobalVars.putString
script -> GlobalVars.getString
=> callback result transferred successfully

KeyBind.pressKeyBind("key.screenshot")
=> pressed state observed
=> no screenshot file observed

Chat.getTextWidth(U+E01C)
=> 6 before resource pack
=> 191 after resource pack
```

The source confirms the two screenshot overloads, the literal `0` passed by the named-file overload, main-thread dispatch implementation, and `GlobalVars` concurrent shared storage. The exact runtime failures/results above come from the target client execution trace.
