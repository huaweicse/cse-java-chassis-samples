package com.huawei.cse.porter.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OAuth2ServerMain {
  public static void main(String[] args) throws Exception {
    try {
      SpringApplication.run(OAuth2ServerMain.class, args);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
