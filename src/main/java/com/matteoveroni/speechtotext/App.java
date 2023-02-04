package com.matteoveroni.speechtotext;

import com.matteoveroni.speechtotext.di.DaggerAppComponent;
import com.matteoveroni.speechtotext.router.Router;
import io.javalin.Javalin;
import lombok.extern.slf4j.Slf4j;
import javax.inject.Inject;

@Slf4j
public class App {

    public static void main(String[] args) {
        App app = DaggerAppComponent.create().buildApp();
        app.start();
    }

    private final Javalin server;
    private final Router router;

    @Inject
    public App(Javalin server, Router router) {
        this.server = server;
        this.router = router;
    }

    private void start() {
        log.info("App started");
        router.setupRoutes();
        server.start(12000);
        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
    }

}