package com.huawei.cse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping(path = "/")
public interface AuthService {
  @GetMapping(path = "/oauth/token")
  Token auth(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password,
      @RequestParam(name = "grant_type") String grant_type,
      @RequestParam(name = "scope") String scope,
      @RequestParam(name = "client_id") String clientId,
      @RequestParam(name = "client_secret") String clientSecret);
}
