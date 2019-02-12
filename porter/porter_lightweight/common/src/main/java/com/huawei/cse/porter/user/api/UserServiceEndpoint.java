package com.huawei.cse.porter.user.api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping(path = "/")
public interface UserServiceEndpoint {
  @PostMapping(path = "/v1/user/login", produces = MediaType.APPLICATION_JSON_VALUE)
  public SessionInfo login(@RequestParam(name = "userName") String userName,
      @RequestParam(name = "password") String password);

  @GetMapping(path = "/v1/user/session", produces = MediaType.APPLICATION_JSON_VALUE)
  public SessionInfo getSession(@RequestParam(name = "sessionId") String sessionId);
  
  @GetMapping(path = "/v1/user/ping", produces = MediaType.APPLICATION_JSON_VALUE)
  public String ping(@RequestParam(name = "message") String message);
}
