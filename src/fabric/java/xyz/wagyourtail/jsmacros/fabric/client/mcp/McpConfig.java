package xyz.wagyourtail.jsmacros.fabric.client.mcp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import xyz.wagyourtail.jsmacros.client.JsMacros;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class McpConfig {
    public static final String DEFAULT_HOST = "127.0.0.1";
    public static final int DEFAULT_PORT = 25580;
    public static final String DEFAULT_ENDPOINT = "/mcp";
    public static final int DEFAULT_TIMEOUT_SECONDS = 30;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("jsmacros-mcp.json");
    private static McpConfig instance;

    public boolean enabled = true;
    public String host = DEFAULT_HOST;
    public int port = DEFAULT_PORT;
    public String endpoint = DEFAULT_ENDPOINT;
    public String apiKey = "";
    public int requestTimeoutSeconds = DEFAULT_TIMEOUT_SECONDS;

    public static synchronized McpConfig get() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    public static synchronized void replace(McpConfig config) {
        config.normalize();
        instance = config;
        save();
    }

    public McpConfig copy() {
        McpConfig copy = new McpConfig();
        copy.enabled = enabled;
        copy.host = host;
        copy.port = port;
        copy.endpoint = endpoint;
        copy.apiKey = apiKey;
        copy.requestTimeoutSeconds = requestTimeoutSeconds;
        return copy;
    }

    public String url() {
        return "http://" + host + ":" + port + endpoint;
    }

    public static synchronized void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(get(), writer);
            }
        } catch (IOException e) {
            JsMacros.LOGGER.error("Could not save JsMacros MCP config", e);
        }
    }

    private static McpConfig load() {
        if (Files.isRegularFile(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                McpConfig config = GSON.fromJson(reader, McpConfig.class);
                if (config != null) {
                    config.normalize();
                    return config;
                }
            } catch (Exception e) {
                JsMacros.LOGGER.error("Could not read JsMacros MCP config; using defaults", e);
            }
        }

        McpConfig config = new McpConfig();
        instance = config;
        save();
        return config;
    }

    private void normalize() {
        if (host == null || host.isBlank()) host = DEFAULT_HOST;
        host = host.trim();
        port = Math.max(1, Math.min(65535, port));
        if (endpoint == null || endpoint.isBlank()) endpoint = DEFAULT_ENDPOINT;
        endpoint = endpoint.trim();
        if (!endpoint.startsWith("/")) endpoint = "/" + endpoint;
        apiKey = apiKey == null ? "" : apiKey.trim();
        requestTimeoutSeconds = Math.max(1, Math.min(600, requestTimeoutSeconds));
    }
}
