package com.matteoveroni.speechtotext.di;

import com.matteoveroni.speechtotext.handlers.SpeechToTextApiResource;
import com.matteoveroni.speechtotext.router.Router;
import dagger.Module;
import dagger.Provides;
import io.javalin.Javalin;
import io.javalin.json.JsonMapper;
import io.javalin.plugin.bundled.CorsPluginConfig;
import javax.inject.Singleton;
import java.time.Duration;

@Module
public class ServerModule {

    @Singleton
    @Provides
    public static Javalin providesJavalin(JsonMapper gsonMapper) {
        return Javalin.create(config -> {
                    config.jetty.wsFactoryConfig(jettyWebSocketServletFactory -> {
                        jettyWebSocketServletFactory.setIdleTimeout(Duration.ofSeconds(60));
                    });
                    config.jsonMapper(gsonMapper);
                    config.plugins.enableCors(cors -> cors.add(CorsPluginConfig::anyHost));
                }
        );
    }

    @Singleton
    @Provides
    public static Router providesRouter(Javalin server, SpeechToTextApiResource speechToTextApiResource) {
        return new Router(server, speechToTextApiResource);
    }
}
