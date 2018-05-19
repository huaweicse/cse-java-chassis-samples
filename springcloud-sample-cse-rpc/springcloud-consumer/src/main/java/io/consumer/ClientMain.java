package io.consumer;

import org.apache.servicecomb.springboot.starter.provider.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableServiceComb
public class ClientMain {

  public static void main(String[] args) {
    SpringApplication.run(ClientMain.class, args);
  }
}
