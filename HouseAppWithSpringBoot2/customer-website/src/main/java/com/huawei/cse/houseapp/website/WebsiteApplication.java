package com.huawei.cse.houseapp.website;

import org.apache.servicecomb.springboot2.starter.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;

@SpringBootApplication(exclude = DispatcherServletAutoConfiguration.class)
@EnableServiceComb
public class WebsiteApplication {
  public static void main(String[] args) throws Exception {
    SpringApplication.run(WebsiteApplication.class, args);
  }
}
