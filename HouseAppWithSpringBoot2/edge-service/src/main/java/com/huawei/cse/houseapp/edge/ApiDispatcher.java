package com.huawei.cse.houseapp.edge;

import org.apache.servicecomb.edge.core.DefaultEdgeDispatcher;

import com.netflix.config.DynamicPropertyFactory;

import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.RoutingContext;

public class ApiDispatcher extends DefaultEdgeDispatcher {
  private static final String KEY_ENABLED = "servicecomb.http.dispatcher.edge.api.enabled";

  @Override
  public int getOrder() {
    return 11;
  }

  @Override
  public boolean enabled() {
    return DynamicPropertyFactory.getInstance().getBooleanProperty(KEY_ENABLED, false).get();
  }

  @Override
  protected void onRequest(RoutingContext context) {
    Cookie sessionId = context.getCookie("session-id");
    if (sessionId == null || !isUserNameCorrect(sessionId.getValue())) {
      context.response().setStatusCode(302);
      context.response().putHeader("Location", "/ui/customer-website/login.html");
      context.response().end();
      return;
    }
    context.request().headers().add("userId", sessionId.getValue());

    super.onRequest(context);
  }

  private boolean isUserNameCorrect(String userName) {
    if (userName == null) {
      return false;
    }
    long u;
    try {
      u = Long.parseLong(userName);
    } catch (NumberFormatException e) {
      return false;
    }
    if (u < 0) {
      return false;
    } else {
      return true;
    }
  }
}
