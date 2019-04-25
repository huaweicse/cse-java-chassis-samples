package com.huawei.cse.porter.user.api;

import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;

@RestSchema(schemaId = "user")
@RequestMapping(path = "/")
@SwaggerDefinition(info = @Info(description = "用户认证、会话等管理", title = "用户管理接口", version = "v1"), basePath = "/")
public class UserEndpoint {
  @Autowired
  private UserService userService;

  @PostMapping(path = "/v1/user/login", produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "登录")
  public SessionInfo login(@RequestParam(name = "userName") String userName,
      @RequestParam(name = "password") String password) {
    return userService.login(userName, password);
  }

  @GetMapping(path = "/v1/user/session", produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "查询会话")
  public SessionInfo getSession(@RequestParam(name = "sessionId") String sessionId) {
    return userService.getSession(sessionId);
  }

  @GetMapping(path = "/v1/user/ping", produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "系统测试接口")
  public String ping(@RequestParam(name = "message") String message) {
    return userService.ping(message);
  }
}
