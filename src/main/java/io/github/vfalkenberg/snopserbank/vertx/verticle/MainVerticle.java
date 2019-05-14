package io.github.vfalkenberg.snopserbank.vertx.verticle;

import io.vertx.core.Future;
import io.vertx.reactivex.CompletableHelper;
import io.vertx.reactivex.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Виктор Фалькенберг
 */

public class MainVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCVerticle.class);

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        final JDBCVerticle jdbcVerticle = new JDBCVerticle();
        final RestVerticle restVerticle = new RestVerticle();
        vertx.rxDeployVerticle(jdbcVerticle)
                .ignoreElement()
                .andThen(vertx.rxDeployVerticle(restVerticle))
                .ignoreElement()
                .doOnError(err -> {
                    LOGGER.error("Error when deploying service", err);
                    vertx.close();
                })
                .subscribe(CompletableHelper.toObserver(startFuture));
    }


}
