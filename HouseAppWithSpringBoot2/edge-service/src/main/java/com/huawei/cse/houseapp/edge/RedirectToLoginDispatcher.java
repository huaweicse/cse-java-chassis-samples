package com.huawei.cse.houseapp.edge;

import org.apache.servicecomb.edge.core.AbstractEdgeDispatcher;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CookieHandler;

//优先级最低的转发器，将请求重定向到登录页面。
public class RedirectToLoginDispatcher extends AbstractEdgeDispatcher {
  @Override
  public int getOrder() {
    return 13;
  }

  @Override
  public void init(Router router) {
    router.routeWithRegex("/").handler(CookieHandler.create());
    router.routeWithRegex("/").handler(createBodyHandler());
    router.routeWithRegex("/").failureHandler(this::onFailure).handler(this::onRequest);
  }

  protected void onRequest(RoutingContext context) {
    context.response().setStatusCode(302);
    context.response().putHeader("Location", "/ui/customer-website/login.html");
    context.response().end();
  }
}
