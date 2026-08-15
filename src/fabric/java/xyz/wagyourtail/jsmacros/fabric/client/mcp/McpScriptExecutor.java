package xyz.wagyourtail.jsmacros.fabric.client.mcp;

import xyz.wagyourtail.jsmacros.client.JsMacrosClient;
import xyz.wagyourtail.jsmacros.core.event.impl.EventCustom;
import xyz.wagyourtail.jsmacros.core.language.EventContainer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public final class McpScriptExecutor {
    private static final String RESULT_KEY = "__jsmacros_mcp_result";

    private McpScriptExecutor() {
    }

    public static ExecutionResult execute(String script, int timeoutSeconds) {
        if (script == null || script.isBlank()) {
            return ExecutionResult.error("The script argument must not be empty.");
        }

        EventCustom event = new EventCustom(JsMacrosClient.clientCore, "McpExecute");
        CountDownLatch completed = new CountDownLatch(1);
        AtomicReference<Throwable> failure = new AtomicReference<>();

        String wrappedScript = """
            (() => {
                const __mcpValue = (() => {
            %s
                })();
                let __mcpJson;
                if (typeof __mcpValue === 'undefined') {
                    __mcpJson = '{"type":"undefined"}';
                } else {
                    try {
                        __mcpJson = JSON.stringify(__mcpValue);
                    } catch (__mcpSerializationError) {
                        __mcpJson = JSON.stringify({
                            type: typeof __mcpValue,
                            value: String(__mcpValue),
                            serializationError: String(__mcpSerializationError)
                        });
                    }
                }
                event.putString('%s', __mcpJson ?? 'null');
            })();
            """.formatted(script, RESULT_KEY);

        EventContainer<?> container = JsMacrosClient.clientCore.exec(
            "js",
            wrappedScript,
            null,
            event,
            completed::countDown,
            error -> {
                failure.set(error);
                completed.countDown();
            }
        );

        try {
            if (!completed.await(timeoutSeconds, TimeUnit.SECONDS)) {
                container.getCtx().closeContext();
                return ExecutionResult.error("JsMacros execution timed out after " + timeoutSeconds + " seconds.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ExecutionResult.error("MCP request interrupted while waiting for JsMacros.");
        }

        Throwable error = failure.get();
        if (error != null) {
            return ExecutionResult.error(formatThrowable(error));
        }

        String result = event.getString(RESULT_KEY);
        return ExecutionResult.success(result == null ? "null" : result);
    }

    private static String formatThrowable(Throwable throwable) {
        String message = throwable.getMessage();
        return throwable.getClass().getSimpleName() + (message == null || message.isBlank() ? "" : ": " + message);
    }

    public record ExecutionResult(boolean success, String text) {
        public static ExecutionResult success(String text) {
            return new ExecutionResult(true, text);
        }

        public static ExecutionResult error(String text) {
            return new ExecutionResult(false, text);
        }
    }
}
