package io.github.vfalkenberg.snopserbank.vertx.verticle;

import io.github.vfalkenberg.snopserbank.vertx.service.jdbc.JDBCService;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.reactivex.core.AbstractVerticle;

/**
 * @author Виктор Фалькенберг
 */
public class JDBCVerticle extends AbstractVerticle {
    private JDBCClient client;
    private JDBCService jdbcService;

    @Override
    public void start() {
        client = JDBCClient.createShared(vertx, new JsonObject()
                .put("url", config().getString("jdbc-db-url"))
                .put("driver_class", config().getString("jdbc-db-driver"))
        );

    }

    private void setUpInitialData(Handler<Void> done) {
        client.getConnection(res -> {
            if (res.failed()) {
                throw new RuntimeException(res.cause());
            }

            final SQLConnection conn = res.result();

            conn.execute("CREATE TABLE IF NOT EXISTS products(id INT IDENTITY, name VARCHAR(255), price FLOAT, weight INT)", ddl -> {
                if (ddl.failed()) {
                    throw new RuntimeException(ddl.cause());
                }

                conn.execute("INSERT INTO products (name, price, weight) VALUES ('Egg Whisk', 3.99, 150), ('Tea Cosy', 5.99, 100), ('Spatula', 1.00, 80)", fixtures -> {
                    if (fixtures.failed()) {
                        throw new RuntimeException(fixtures.cause());
                    }

                    done.handle(null);
                });
            });
        });
    }
}
