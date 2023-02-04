package com.matteoveroni.speechtotext.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import io.javalin.json.JsonMapper;
import org.jetbrains.annotations.NotNull;
import javax.inject.Singleton;
import java.lang.reflect.Type;

@Module
public class JsonModule {

    @Singleton
    @Provides
    public static Gson providesGson() {
        return new GsonBuilder().create();
    }

    @Singleton
    @Provides
    public static JsonMapper providesGsonMapper (Gson gson) {
        return new JsonMapper() {
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
    }
}
