package com.huawei.cse.porter.user;

import java.io.File;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.servicecomb.foundation.common.utils.BeanUtils;
import org.apache.servicecomb.foundation.common.utils.Log4jUtils;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class UserMain {
  public static void main(String[] args) throws Exception {
    Log4jUtils.init();
    BeanUtils.init();

  }

  private static void test() throws Exception {
    CloseableHttpClient httpClient = HttpClients.custom()
        .setSSLContext(SSLContexts.custom()
            .loadTrustMaterial(new File(
                "D:\\code\\cse-java-chassis-samples\\porter\\gateway-service\\src\\main\\resources\\trust.jks"),
                "Changeme_123".toCharArray())
            .build())
        .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
        .build();
    HttpComponentsClientHttpRequestFactory requestFactory =
        new HttpComponentsClientHttpRequestFactory();

    requestFactory.setHttpClient(httpClient);

    RestTemplate restTemplate = new RestTemplate(requestFactory);

    for (int i = 0; i < 10; i++) {
      new Thread() {
        public void run() {
          while (true) {
            try {
              String s = restTemplate.getForObject("https://localhost:9090/ui/porter-website/index.html", String.class);
              if (26376 != s.length()) {
                System.out.println(s.length());
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
      }.start();
    }
  }
}
