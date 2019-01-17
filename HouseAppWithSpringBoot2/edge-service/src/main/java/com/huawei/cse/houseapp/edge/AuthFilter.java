package com.huawei.cse.houseapp.edge;

import javax.servlet.http.Cookie;
import javax.ws.rs.core.Response.Status;

import org.apache.servicecomb.common.rest.filter.HttpServerFilter;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.foundation.vertx.http.HttpServletRequestEx;
import org.apache.servicecomb.swagger.invocation.Response;

/**
 * 认证：对于API接口，需要进行认证。UI静态页面不提供认证。
 *
 */
public class AuthFilter implements HttpServerFilter {

  @Override
  public int getOrder() {
    // before args is extracted
    return -200;
  }

  @Override
  public Response afterReceiveRequest(Invocation invocation, HttpServletRequestEx requestEx) {
    if (invocation.getOperationMeta().getMicroserviceQualifiedName().equals("user-service.user.login2")) {
      // 登录接口，不需要认证
      return null;
    }
    Cookie[] cookies = requestEx.getCookies();
    if (cookies != null) {
      Cookie session = null;
      for (Cookie c : cookies) {
        if (c.getName().equals("session-id")) {
          session = c;
          break;
        }
      }

      if (session != null) {
        // 商用系统需要验证session的合法性，这里只是示例，简化了实现方案
        String id = session.getValue();
        invocation.addContext("user-id", id);
        return null;
      }
    }
    return Response.create(Status.FORBIDDEN, "Not Authenticated.");
  }

}
