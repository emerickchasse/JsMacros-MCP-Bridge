package xyz.wagyourtail.jsmacros.fabric.client.mcp;

import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.HttpServletStreamableServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import xyz.wagyourtail.jsmacros.client.JsMacros;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class McpServerManager {
    public static final String TOOL_NAME = "execute_mc_jsmacros";

    private static final ExecutorService LIFECYCLE = Executors.newSingleThreadExecutor(task -> {
        Thread thread = new Thread(task, "JsMacros-MCP-Lifecycle");
        thread.setDaemon(true);
        return thread;
    });

    private static Tomcat tomcat;
    private static HttpServletStreamableServerTransportProvider transport;
    private static McpSyncServer mcpServer;

    private McpServerManager() {
    }

    public static void start() {
        LIFECYCLE.execute(McpServerManager::startInternal);
    }

    public static void restart() {
        LIFECYCLE.execute(() -> {
            stopInternal();
            startInternal();
        });
    }

    public static void shutdown() {
        stopInternal();
        LIFECYCLE.shutdownNow();
    }

    private static synchronized void startInternal() {
        McpConfig config = McpConfig.get().copy();
        if (!config.enabled || tomcat != null) return;

        try {
            transport = HttpServletStreamableServerTransportProvider.builder()
                .mcpEndpoint(config.endpoint)
                .build();

            McpSchema.Tool tool = McpSchema.Tool.builder(TOOL_NAME, Map.of(
                    "type", "object",
                    "properties", Map.of(
                        "script", Map.of(
                            "type", "string",
                            "description", "JavaScript function body executed by JsMacros. Use return to send a JSON-serializable value back."
                        ),
                        "timeout_ms", Map.of(
                            "type", "integer",
                            "minimum", 1000,
                            "maximum", 600000,
                            "description", "Optional execution timeout in milliseconds."
                        )
                    ),
                    "required", List.of("script"),
                    "additionalProperties", false
                ))
                .description("Execute JavaScript inside the running Minecraft client through JsMacros. The script can use every JsMacros library, including Client, Player, World, Hud and inventory/GUI APIs.")
                .build();

            McpServerFeatures.SyncToolSpecification toolSpecification = McpServerFeatures.SyncToolSpecification
                .builder()
                .tool(tool)
                .callHandler((exchange, request) -> {
                    Object rawScript = request.arguments().get("script");
                    int timeoutSeconds = timeoutSeconds(request.arguments().get("timeout_ms"), config.requestTimeoutSeconds);
                    McpScriptExecutor.ExecutionResult result = McpScriptExecutor.execute(
                        rawScript instanceof String value ? value : null,
                        timeoutSeconds
                    );
                    return McpSchema.CallToolResult.builder()
                        .addTextContent(result.text())
                        .isError(!result.success())
                        .build();
                })
                .build();

            String version = FabricLoader.getInstance()
                .getModContainer("jsmacros")
                .map(container -> container.getMetadata().getVersion().getFriendlyString())
                .orElse("unknown");

            mcpServer = McpServer.sync(transport)
                .serverInfo("jsmacros-mcp", version)
                .capabilities(McpSchema.ServerCapabilities.builder().tools(true).build())
                .tools(toolSpecification)
                .build();

            Path baseDir = FabricLoader.getInstance().getGameDir().resolve(".jsmacros-mcp-tomcat");
            Files.createDirectories(baseDir);

            tomcat = new Tomcat();
            tomcat.setBaseDir(baseDir.toString());
            tomcat.setPort(config.port);
            tomcat.getConnector().setProperty("address", config.host);
            tomcat.getConnector().setAsyncTimeout(config.requestTimeoutSeconds * 1000L);

            Context context = tomcat.addContext("", baseDir.toString());
            Wrapper wrapper = context.createWrapper();
            wrapper.setName("jsmacrosMcpServlet");
            wrapper.setServlet(transport);
            wrapper.setLoadOnStartup(1);
            wrapper.setAsyncSupported(true);
            context.addChild(wrapper);
            context.addServletMappingDecoded("/*", "jsmacrosMcpServlet");

            if (!config.apiKey.isBlank()) {
                addApiKeyFilter(context, config.apiKey);
            }

            tomcat.start();
            JsMacros.LOGGER.info("JsMacros MCP listening at {}", config.url());
        } catch (Throwable error) {
            JsMacros.LOGGER.error("Could not start JsMacros MCP", error);
            stopInternal();
        }
    }

    private static int timeoutSeconds(Object rawTimeout, int defaultSeconds) {
        if (!(rawTimeout instanceof Number number)) return defaultSeconds;
        long millis = Math.max(1000L, Math.min(600000L, number.longValue()));
        return (int) Math.ceil(millis / 1000.0);
    }

    private static void addApiKeyFilter(Context context, String apiKey) {
        FilterDef definition = new FilterDef();
        definition.setFilterName("jsmacrosMcpApiKey");
        definition.setFilter(new ApiKeyFilter(apiKey));
        definition.setAsyncSupported("true");
        context.addFilterDef(definition);

        FilterMap mapping = new FilterMap();
        mapping.setFilterName("jsmacrosMcpApiKey");
        mapping.addURLPattern("/*");
        context.addFilterMap(mapping);
    }

    private static synchronized void stopInternal() {
        if (mcpServer != null) {
            try {
                mcpServer.close();
            } catch (Exception ignored) {
            }
            mcpServer = null;
        }
        if (transport != null) {
            try {
                transport.closeGracefully().block();
            } catch (Exception ignored) {
            }
            transport = null;
        }
        if (tomcat != null) {
            try {
                tomcat.stop();
                tomcat.destroy();
            } catch (LifecycleException error) {
                JsMacros.LOGGER.warn("Could not stop JsMacros MCP cleanly", error);
            }
            tomcat = null;
        }
    }

    private static final class ApiKeyFilter implements Filter {
        private final String expected;

        private ApiKeyFilter(String expected) {
            this.expected = expected;
        }

        @Override
        public void init(FilterConfig filterConfig) {
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String authorization = httpRequest.getHeader("Authorization");
            String headerKey = httpRequest.getHeader("X-API-Key");
            if (("Bearer " + expected).equals(authorization) || expected.equals(headerKey)) {
                chain.doFilter(request, response);
                return;
            }

            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("{\"error\":\"Invalid or missing API key\"}");
        }

        @Override
        public void destroy() {
        }
    }
}
