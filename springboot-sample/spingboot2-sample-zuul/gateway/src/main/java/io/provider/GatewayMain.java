package io.provider;

import org.apache.servicecomb.provider.pojo.RpcReference;
import org.apache.servicecomb.springboot2.starter.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
@EnableServiceComb
public class GatewayMain {
  @RpcReference(microserviceName = "springboot2-sample-zuul-bussiness", schemaId = "BusinessService")
  private static IBusinessService b;
  
  public static void main(String[] args) {
    try {
      SpringApplication.run(GatewayMain.class, args);
    } catch (Throwable e) {
      e.printStackTrace();
    }
    
    System.out.println(b.hello("World"));
  }
}
