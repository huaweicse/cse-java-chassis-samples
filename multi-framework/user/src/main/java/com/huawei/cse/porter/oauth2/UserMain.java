package com.huawei.cse.porter.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserMain {
  public static void main(String[] args) throws Exception {
    try {
      SpringApplication.run(UserMain.class, args);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
