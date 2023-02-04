package com.matteoveroni.speechtotext;

import io.javalin.websocket.WsContext;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class WebsocketConnections {

//    private static final Map<WsContext, String> userUsernameMap = new ConcurrentHashMap<>();

    private static final Map<UUID, WsContext> userConnections = new ConcurrentHashMap<>();

    public UUID addUserConnection(WsContext wsContext) {
        UUID id = UUID.randomUUID();
        userConnections.put(id, wsContext);
        return id;
    }

    public void removeUserConnection(UUID id) {
        userConnections.remove(id);
    }

    public WsContext getUserConnection(UUID id) {
        return userConnections.get(id);
    }

}
