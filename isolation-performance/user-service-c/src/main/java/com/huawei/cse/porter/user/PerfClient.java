package com.huawei.cse.porter.user;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.servicecomb.foundation.common.utils.BeanUtils;
import org.apache.servicecomb.foundation.common.utils.Log4jUtils;
import org.apache.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

public class PerfClient {
  private static final Logger LOG = LoggerFactory.getLogger(PerfClient.class);
  public static void main(String[] args) throws Exception {
    Log4jUtils.init();
    BeanUtils.init();

    RestTemplate client = RestTemplateBuilder.create();
    AtomicLong counterS = new AtomicLong(0);
    AtomicLong counterE = new AtomicLong(0);
    for (int i = 0; i < 20; i++) {
      new Thread() {
        public void run() {
          while (true) {
            try {
              String s = client.getForObject("cse://user-service/hello?name=123", String.class);
              if (!s.equals("123")) {
                throw new Exception();
              }
              counterS.incrementAndGet();
            } catch (Exception e) {
              counterE.getAndIncrement();
            }
          }
        }
      }.start();
    }

    while (true) {
      Thread.sleep(5000);
      LOG.info("S=" + counterS.get() + ";E=" + counterE.get());
    }
  }
}
