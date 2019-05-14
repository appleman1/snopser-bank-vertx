package io.github.vfalkenberg.snopserbank.vertx;

import io.github.vfalkenberg.snopserbank.vertx.verticle.MainVerticle;
import io.vertx.core.Vertx;

/**
 * @author Виктор Фалькенберг
 */
public class Application {
    public static void main(String[] args) {
        final Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MainVerticle());
    }
}
