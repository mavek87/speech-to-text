package org.example.router;

import io.javalin.Javalin;
import io.javalin.http.Header;
import lombok.extern.slf4j.Slf4j;
import org.example.WebsocketConnections;
import org.example.handlers.SpeechToTextApiResource;
import org.example.terminal.TerminalEmulator;
import java.util.UUID;
import static io.javalin.apibuilder.ApiBuilder.before;
import static io.javalin.apibuilder.ApiBuilder.post;

@Slf4j
public class Router {

    public static final String ROOT = "/";
    public static final String API = ROOT + "api";

    public static final String SPEECH_TO_TEXT_RESOURCE = API + "/speech-to-text";

    private final Javalin server;
    private final WebsocketConnections websocketConnections;
    private final TerminalEmulator terminalEmulator;

    public Router(Javalin server, WebsocketConnections websocketConnections, TerminalEmulator terminalEmulator) {
        this.server = server;
        this.websocketConnections = websocketConnections;
        this.terminalEmulator = terminalEmulator;
    }

    public void setupRoutes() {
        server.routes(() -> {
            server.options(SPEECH_TO_TEXT_RESOURCE, ctx -> ctx.result("OK"));

            // Aggiunto header Access control allow origin per preflight request dal browser
            before("*", ctx -> {
                String crossHeaders = ctx.header("Access-Control-Request-Headers");
                if (crossHeaders != null && !crossHeaders.isEmpty()) {
                    ctx.header("Access-Control-Request-Headers", crossHeaders);
                }

                ctx.header(Header.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
                ctx.header(Header.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PATCH, DELETE, OPTIONS");
                ctx.header(Header.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
                ctx.header(Header.ACCESS_CONTROL_ALLOW_HEADERS, "origin, content-type, accept, authorization, timestamp_request");

                log.info("crossHeaders: {}", crossHeaders);
            });

            post(SPEECH_TO_TEXT_RESOURCE, new SpeechToTextApiResource(terminalEmulator));
        });

//        server.ws("/websocket/transcript", ws -> {
//            ws.onConnect(ctx -> {
//                UUID clientId = websocketConnections.addUserConnection(ctx);
//                ctx.send("{\"id\": \"" + clientId + "\"}");
//            });
//            ws.onClose(ctx -> {
//                ctx.send("{\"onClose\": \"onClose\"}");
//            });
//            ws.onMessage(ctx -> {
//                new Thread(() -> {
//                    try {
//                        Thread.sleep(2000);
//                        ctx.send("{\"message\": \"" + ctx.message() + "\"}");
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }).start();
//                ctx.send("{\"message\": \"" + ctx.message() + "\"}");
//            });
//        });
    }
}
