package com.huawei.cse.porter.gateway;

import org.apache.servicecomb.transport.rest.vertx.VertxHttpDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

public class WebpageDispatcher implements VertxHttpDispatcher {
  private static final Logger LOGGER = LoggerFactory.getLogger(WebpageDispatcher.class);
  
  
  @Override
  public int getOrder() {
    return 0;
  }

  @Override
  public void init(Router router) {
    String regex = "/ui/([^\\/]+)/(.*)";
    StaticHandler webpageHandler = StaticHandler.create();
    webpageHandler.setWebRoot("/static");
    router.routeWithRegex(regex).failureHandler((context) -> {
      LOGGER.error("", context.failure());
    }).handler(webpageHandler);
  }

}
