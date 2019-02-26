package com.huawei.cse.porter.gateway;

import java.util.concurrent.CompletableFuture;

import org.apache.servicecomb.core.Handler;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.foundation.common.utils.JsonUtils;
import org.apache.servicecomb.provider.pojo.Invoker;
import org.apache.servicecomb.swagger.invocation.AsyncResponse;
import org.apache.servicecomb.swagger.invocation.Response;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;

import com.huawei.cse.porter.user.api.SessionInfo;


public class AuthHandler implements Handler {
  private Session session = Invoker.createProxy("user-service", "user", Session.class);

  @Override
  public void handle(Invocation invocation, AsyncResponse asyncResponse) throws Exception {
    if (invocation.getMicroserviceName().equals("user-service")
        && (invocation.getOperationName().equals("login")
            || (invocation.getOperationName().equals("getSession")))) {
      // login： 直接返回认证结果。  开发者需要在JS里面设置cookie。 
      invocation.next(asyncResponse);
    } else {
      // check session
      String sessionId = invocation.getContext("session-id");
      if (sessionId == null) {
        throw new InvocationException(403, "", "session is not valid.");
      }

      // 在网关执行的Hanlder逻辑，是reactive模式的，不能使用阻塞调用。
      CompletableFuture<SessionInfo> sessionInfoFuture = session.getSession(sessionId);
      sessionInfoFuture.whenComplete((sessionInfo, e) -> {
        if (sessionInfoFuture.isCompletedExceptionally()) {
          asyncResponse.complete(Response.failResp(new InvocationException(403, "", "session is not valid.")));
        } else {
          if (sessionInfo == null) {
            asyncResponse.complete(Response.failResp(new InvocationException(403, "", "session is not valid.")));
          }
          try {
            // 将会话信息传递给后面的微服务。后面的微服务可以从context获取到会话信息，从而可以进行鉴权等。 
            invocation.addContext("session-id", sessionId);
            invocation.addContext("session-info", JsonUtils.writeValueAsString(sessionInfo));
            invocation.next(asyncResponse);
          } catch (Exception ex) {
            asyncResponse.complete(Response.failResp(new InvocationException(500, "", ex.getMessage())));
          }
        }
      });
    }
  }
}
