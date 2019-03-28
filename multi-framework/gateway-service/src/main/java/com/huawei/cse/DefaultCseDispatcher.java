package com.huawei.cse;

import java.util.Map;

import org.apache.servicecomb.edge.core.AbstractEdgeDispatcher;
import org.apache.servicecomb.edge.core.EdgeInvocation;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CookieHandler;

public class DefaultCseDispatcher extends AbstractEdgeDispatcher {
  @Override
  public void init(Router router) {
    // Dispatcher patterns. This dispatcher only forward requests to store.
    router.routeWithRegex("/store/(.*)").handler(CookieHandler.create());
    router.routeWithRegex("/store/(.*)").handler(createBodyHandler());
    router.routeWithRegex("/store/(.*)").failureHandler(this::onFailure).handler(this::onRequest);
  }

  protected void onRequest(RoutingContext context) {
    Map<String, String> pathParams = context.pathParams();
    String microserviceName = "store";
    String path = "/" + pathParams.get("param0");

    EdgeInvocation edgeInvocation = new EdgeInvocation();
    edgeInvocation.init(microserviceName, context, path, httpServerFilters);
    edgeInvocation.edgeInvoke();
  }

  @Override
  public int getOrder() {
    return 10000;
  }
}
