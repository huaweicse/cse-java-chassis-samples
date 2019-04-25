package com.huawei.cse.porter.oauth2;

import org.apache.servicecomb.swagger.invocation.Response;
import org.apache.servicecomb.swagger.invocation.SwaggerInvocation;
import org.apache.servicecomb.swagger.invocation.exception.ExceptionToProducerResponseConverter;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.springframework.security.access.AccessDeniedException;

public class AccessDeniedExceptionConverter implements ExceptionToProducerResponseConverter<AccessDeniedException> {

  @Override
  public Response convert(SwaggerInvocation arg0, AccessDeniedException arg1) {
    return Response.failResp(new InvocationException(403, "", "access denied by spring security"));
  }

  @Override
  public Class<AccessDeniedException> getExceptionClass() {
    return AccessDeniedException.class;
  }

}
