package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.json.JsonMapper;
import io.javalin.plugin.bundled.CorsPluginConfig;
import lombok.extern.slf4j.Slf4j;
import org.example.router.Router;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Type;

@Slf4j
public class App {
    private final Gson gson = new GsonBuilder().create();
    private final JsonMapper gsonMapper = new JsonMapper() {
        @NotNull
        @Override
        public String toJsonString(@NotNull Object obj, @NotNull Type type) {
            return gson.toJson(obj, type);
        }

        @NotNull
        @Override
        public <T> T fromJsonString(@NotNull String json, @NotNull Type targetType) {
            return gson.fromJson(json, targetType);
        }
    };

    public static void main(String[] args) {
        log.info("Starting the app");
        new App().start();

    }

    private void start() {
        log.info("App started");
        Javalin server = buildServer();
        Router router = new Router(server);
        router.setupRoutes();
        server.start(12000);
        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
    }

    private Javalin buildServer() {
        return Javalin.create(config -> {
                    config.jsonMapper(gsonMapper);
                    config.plugins.enableCors(cors -> cors.add(CorsPluginConfig::anyHost));
                }
        );
    }
}