package com.huawei.cse.saga.edge;

import org.apache.servicecomb.springboot.starter.provider.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;

@SpringBootApplication(exclude = DispatcherServletAutoConfiguration.class)
@EnableServiceComb
public class EdgeApplication {
  public static void main(String[] args) throws Exception {
    SpringApplication.run(EdgeApplication.class, args);
  }
}
