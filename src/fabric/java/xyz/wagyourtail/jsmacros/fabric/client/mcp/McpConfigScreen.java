package xyz.wagyourtail.jsmacros.fabric.client.mcp;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class McpConfigScreen {
    private McpConfigScreen() {
    }

    public static Screen create(Screen parent) {
        McpConfig draft = McpConfig.get().copy();
        ConfigBuilder builder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(Component.translatable("jsmacros.mcp.config.title"));
        ConfigEntryBuilder entries = builder.entryBuilder();
        ConfigCategory category = builder.getOrCreateCategory(Component.translatable("jsmacros.mcp.config.category"));

        category.addEntry(entries.startBooleanToggle(Component.translatable("jsmacros.mcp.config.enabled"), draft.enabled)
            .setDefaultValue(true)
            .setSaveConsumer(value -> draft.enabled = value)
            .build());

        category.addEntry(entries.startStrField(Component.translatable("jsmacros.mcp.config.bind_address"), draft.host)
            .setDefaultValue(McpConfig.DEFAULT_HOST)
            .setTooltip(Component.translatable("jsmacros.mcp.config.bind_address.tooltip"))
            .setSaveConsumer(value -> draft.host = value)
            .build());

        category.addEntry(entries.startIntField(Component.translatable("jsmacros.mcp.config.port"), draft.port)
            .setDefaultValue(McpConfig.DEFAULT_PORT)
            .setMin(1)
            .setMax(65535)
            .setSaveConsumer(value -> draft.port = value)
            .build());

        category.addEntry(entries.startStrField(Component.translatable("jsmacros.mcp.config.endpoint"), draft.endpoint)
            .setDefaultValue(McpConfig.DEFAULT_ENDPOINT)
            .setSaveConsumer(value -> draft.endpoint = value)
            .build());

        category.addEntry(entries.startStrField(Component.translatable("jsmacros.mcp.config.api_key"), draft.apiKey)
            .setDefaultValue("")
            .setTooltip(Component.translatable("jsmacros.mcp.config.api_key.tooltip"))
            .setSaveConsumer(value -> draft.apiKey = value)
            .build());

        category.addEntry(entries.startIntField(Component.translatable("jsmacros.mcp.config.timeout"), draft.requestTimeoutSeconds)
            .setDefaultValue(McpConfig.DEFAULT_TIMEOUT_SECONDS)
            .setMin(1)
            .setMax(600)
            .setSaveConsumer(value -> draft.requestTimeoutSeconds = value)
            .build());

        builder.setSavingRunnable(() -> {
            McpConfig.replace(draft);
            McpServerManager.restart();
        });
        return builder.build();
    }
}
