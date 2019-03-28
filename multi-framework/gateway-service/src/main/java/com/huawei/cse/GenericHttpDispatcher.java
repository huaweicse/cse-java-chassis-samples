package com.huawei.cse;

import java.util.Map;

import org.apache.servicecomb.edge.core.AbstractEdgeDispatcher;
import org.apache.servicecomb.foundation.vertx.VertxUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.impl.headers.VertxHttpHeaders;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class GenericHttpDispatcher extends AbstractEdgeDispatcher {
  private static Logger LOGGER = LoggerFactory.getLogger(GenericHttpDispatcher.class);

  private static Vertx vertx = VertxUtils.getOrCreateVertxByName("transport", null);

  private static HttpClient httpClient = vertx.createHttpClient(new HttpClientOptions());

  public GenericHttpDispatcher() {
  }

  @Override
  public int getOrder() {
    return 10001;
  }

  @Override
  public void init(Router router) {
    // Dispatcher patterns. This dispatcher only forward requests to user.
    String regex = "/user/(.*)";
    router.routeWithRegex(regex).failureHandler(this::onFailure).handler(this::onRequest);
  }

  protected void onRequest(RoutingContext context) {
    Map<String, String> pathParams = context.pathParams();

    String path = pathParams.get("param0");

    HttpClientRequest clietRequest =
        httpClient.request(context.request().method(),
            // hard coded ip/port here. can use configurations. 
            9093,
            "localhost",
            "/" + path + "?" + context.request().query(),
            clientResponse -> {
              context.response().setStatusCode(clientResponse.statusCode());
              VertxHttpHeaders headers = new VertxHttpHeaders();
              clientResponse.headers().forEach(entry -> {
                headers.add(entry.getKey(), entry.getValue());
              });
              context.response().headers().setAll(headers);
              clientResponse.handler(data -> {
                context.response().write(data);
              });
              clientResponse.endHandler((v) -> {
                context.response().end();
              });
            });


    clietRequest.headers().setAll(context.request().headers());
    clietRequest.exceptionHandler(e -> {
      LOGGER.error("", e);
    });
    context.request().handler(data -> {
      clietRequest.write(data);
    });
    context.request().endHandler((v) -> {
      clietRequest.end();
    });
  }
}
