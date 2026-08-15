package xyz.wagyourtail.jsmacros.fabric.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import xyz.wagyourtail.jsmacros.fabric.client.mcp.McpConfigScreen;

public class ModMenuEntry implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return McpConfigScreen::create;
    }
}
