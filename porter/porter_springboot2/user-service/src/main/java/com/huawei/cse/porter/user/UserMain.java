package com.huawei.cse.porter.user;

import org.apache.servicecomb.springboot2.starter.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableServiceComb
public class UserMain {
  public static void main(String[] args) throws Exception {
    try {
      SpringApplication.run(UserMain.class, args);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
