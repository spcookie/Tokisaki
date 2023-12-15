package io.micro.starter.config;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Produces;

public class WebClientConfig {
    @Inject
    Vertx vertx;

    @Produces
    @Singleton
    public WebClient webClient() {
        WebClientOptions options = new WebClientOptions().setConnectTimeout(1000 * 900);
        return WebClient.create(vertx, options);
    }
}
