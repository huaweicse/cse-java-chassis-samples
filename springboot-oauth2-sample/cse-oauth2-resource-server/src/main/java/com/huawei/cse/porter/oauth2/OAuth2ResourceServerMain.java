package com.huawei.cse.porter.oauth2;

import org.apache.servicecomb.springboot2.starter.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableServiceComb
public class OAuth2ResourceServerMain {
  public static void main(String[] args) throws Exception {
    try {
      SpringApplication.run(OAuth2ResourceServerMain.class, args);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
