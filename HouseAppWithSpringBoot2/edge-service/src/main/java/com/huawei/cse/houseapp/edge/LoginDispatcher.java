package com.huawei.cse.houseapp.edge;

import org.apache.servicecomb.edge.core.AbstractEdgeDispatcher;
import org.apache.servicecomb.edge.core.EdgeInvocation;
import org.apache.servicecomb.swagger.invocation.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CookieHandler;

public class LoginDispatcher extends AbstractEdgeDispatcher {
  private static Logger LOGGER = LoggerFactory.getLogger(LoginDispatcher.class);

  private static final String LOGIN_URI = "/api/user-service/login2";

  private static final String USER_SERVICE = "user-service";

  private static final String LOGIN_PATH = "/login2";

  @Override
  public int getOrder() {
    return 10;
  }

  @Override
  public void init(Router router) {
    router.routeWithRegex(LOGIN_URI).handler(CookieHandler.create());
    router.routeWithRegex(LOGIN_URI).handler(createBodyHandler());
    router.routeWithRegex(LOGIN_URI).failureHandler(this::onFailure).handler(this::onRequest);
  }

  protected void onRequest(RoutingContext context) {
    EdgeInvocation invoker = new EdgeInvocation() {
      protected void sendResponse(Response response) {
        try {
          if (response.isSuccessed() && (response.getResult() != null && (long) response.getResult() != -1)) {
            Cookie cookie = Cookie.cookie("session-id", String.valueOf((long) response.getResult()));
            cookie.setPath("/");
            cookie.setSecure(false);
            context.addCookie(cookie);
            context.response().end();
            LOGGER.info("user login seccuessfully");
          } else {
            Cookie cookie = Cookie.cookie("session-id", String.valueOf(-1));
            cookie.setPath("/");
            cookie.setSecure(false);
            context.addCookie(cookie);
            context.response().setStatusCode(403);
            context.response().end();
            LOGGER.info("user login failed");
          }
        } catch (Exception e) {
          LOGGER.error("", e);
          context.response().end();
        }
      }
    };
    invoker.init(USER_SERVICE, context, LOGIN_PATH, httpServerFilters);
    invoker.edgeInvoke();
  }
}
