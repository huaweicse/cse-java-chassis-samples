package com.huawei.paas.provider;

import org.apache.servicecomb.springboot.starter.provider.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableServiceComb
public class ProviderApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ProviderApplication.class,args);
    }
}
