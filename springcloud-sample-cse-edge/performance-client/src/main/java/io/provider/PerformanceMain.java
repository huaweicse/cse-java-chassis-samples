package io.provider;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.servicecomb.provider.pojo.RpcReference;
import org.apache.servicecomb.springboot.starter.provider.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;

@SpringBootApplication(exclude = DispatcherServletAutoConfiguration.class)
@EnableServiceComb
public class PerformanceMain {

  @RpcReference(microserviceName = "helloconsumer", schemaId = "hello")
  static Hello hello;

  public static void main(String[] args) throws Exception {
    SpringApplication.run(PerformanceMain.class, args);

    runTest("10");
  }

  public static void runTest(String threadC) throws Exception {
    String data = data();

    //定义线程数    
    final int threadCount = Integer.valueOf(threadC);
    //初始化线程池
    final ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    final CountDownLatch latch = new CountDownLatch(threadCount);
    for (int j = 0; j < threadCount; j++) {
      executor.submit(new Runnable() {
        public void run() {
          try {
            assertEquals("from provider: Hello " + data + "-from provider: Hello " + data, hello.hello(data));
          } catch (Throwable e) {
            e.printStackTrace();
          }
          latch.countDown();
        }
      });
    } ;
    latch.await();

    // 开始测试
    for (int i = 0; i < threadCount; i++) {
      executor.submit(new Runnable() {
        public void run() {
          while (true) {
            assertEquals("from provider: Hello " + data + "-from provider: Hello " + data, hello.hello(data));
          }
        }
      });
    } ;
  }

  static String data() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 10; i++) {
      sb.append("c");
    }
    return sb.toString();
  }

  private static void assertEquals(String a, String b) {
    if (a == null) {
      if (b != null) {
        System.out.println("assert failed, expected[" + a + "],actual[" + b + "]");
      }
    } else {
      if (!a.equals(b)) {
        System.out.println("assert failed, expected[" + a + "],actual[" + b + "]");
      }
    }
  }
}
