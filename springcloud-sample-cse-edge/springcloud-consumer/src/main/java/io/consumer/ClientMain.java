package io.consumer;

import org.apache.servicecomb.springboot.starter.provider.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(exclude=DispatcherServletAutoConfiguration.class)
@EnableServiceComb
@EnableConfigurationProperties
public class ClientMain {

  public static void main(String[] args) {
    SpringApplication.run(ClientMain.class, args);
  }
}
