package com.huawei.cse.porter.gateway;

import org.apache.servicecomb.core.Handler;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.foundation.common.utils.JsonUtils;
import org.apache.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import org.apache.servicecomb.swagger.invocation.AsyncResponse;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.springframework.web.client.RestTemplate;

import com.huawei.cse.porter.user.dao.SessionInfo;

public class AuthHandler implements Handler {
    private RestTemplate restTemplate = RestTemplateBuilder.create();

    @Override
    public void handle(Invocation invocation, AsyncResponse asyncResponse) throws Exception {
        if (invocation.getMicroserviceName().equals("user-service")
                && (invocation.getOperationName().equals("login")
                        || (invocation.getOperationName().equals("getSession")))) {
            // login： 直接返回认证结果。 在JS里面管理cookie。 开发者也可以在后台服务器管理cookie。 
            //            HttpHeaders headers = new HttpHeaders();
            //            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
            //            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            //            SessionInfo sessionInfo = restTemplate.postForObject("cse://user-service/login", request, SessionInfo.class);
            invocation.next(asyncResponse);
        } else {
            // check session
            String sessionId = invocation.getContext("session-id");
            if (sessionId == null) {
                throw new InvocationException(403, "", "session is not valid.");
            }
            SessionInfo sessionInfo =
                restTemplate.getForObject("cse://user-service/session?sessionId=" + sessionId, SessionInfo.class);
            if (sessionInfo == null) {
                throw new InvocationException(403, "", "session is not valid.");
            }
            // 将会话信息传递给后面的微服务。后面的微服务可以从context获取到会话信息，从而可以进行鉴权等。 
            invocation.addContext("session-id", sessionId);
            invocation.addContext("session-info", JsonUtils.writeValueAsString(sessionInfo));
            invocation.next(asyncResponse);
        }

    }

}
