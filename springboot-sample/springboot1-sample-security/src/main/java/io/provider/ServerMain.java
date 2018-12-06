package io.provider;

import org.apache.servicecomb.springboot.starter.provider.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableServiceComb
public class ServerMain {
  public static void main(String[] args) {
    try {
      SpringApplication.run(ServerMain.class, args);
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }
}
