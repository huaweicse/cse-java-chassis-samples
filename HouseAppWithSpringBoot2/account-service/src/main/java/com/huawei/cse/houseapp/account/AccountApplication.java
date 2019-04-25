package com.huawei.cse.houseapp.account;

import org.apache.servicecomb.springboot2.starter.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableServiceComb
public class AccountApplication {
  public static void main(String[] args) throws Exception {
    try {
      SpringApplication.run(AccountApplication.class, args);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
