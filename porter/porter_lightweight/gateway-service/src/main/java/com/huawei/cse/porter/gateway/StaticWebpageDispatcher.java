package com.huawei.cse.porter.gateway;

import org.apache.servicecomb.transport.rest.vertx.VertxHttpDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.config.DynamicPropertyFactory;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

public class StaticWebpageDispatcher implements VertxHttpDispatcher {
  private static final Logger LOGGER = LoggerFactory.getLogger(StaticWebpageDispatcher.class);

  private static final String WEB_ROOT = DynamicPropertyFactory.getInstance()
      .getStringProperty("gateway.webroot", "/var/static")
      .get();

  @Override
  public int getOrder() {
    return Integer.MAX_VALUE;
  }

  @Override
  public void init(Router router) {
    String regex = "/ui/(.*)";
    StaticHandler webpageHandler = StaticHandler.create();
    webpageHandler.setWebRoot(WEB_ROOT);
    LOGGER.info("server static web page for WEB_ROOT={}", WEB_ROOT);
    router.routeWithRegex(regex).failureHandler((context) -> {
      LOGGER.error("", context.failure());
    }).handler(webpageHandler);
  }

}
