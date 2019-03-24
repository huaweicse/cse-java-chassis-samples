package com.huawei.cse.porter.oauth2;

import org.apache.servicecomb.springboot2.starter.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableServiceComb
public class OAuth2ServerMain {
  public static void main(String[] args) throws Exception {
    try {
      SpringApplication.run(OAuth2ServerMain.class, args);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
