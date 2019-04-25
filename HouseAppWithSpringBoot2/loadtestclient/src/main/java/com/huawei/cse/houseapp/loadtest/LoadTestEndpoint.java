package com.huawei.cse.houseapp.loadtest;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.servicecomb.provider.pojo.RpcReference;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.huawei.cse.houseapp.customer.api.CustomerEndpoint;

@RestSchema(schemaId = "load")
@RequestMapping(path = "/")
public class LoadTestEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoadTestEndpoint.class);
    //初始化线程池
    private static ExecutorService executor;

    private static boolean running;

    @RpcReference(microserviceName = "customer-service", schemaId = "customer")
    private static CustomerEndpoint customerService;

    @GetMapping(path = "start")
    public boolean start(@RequestParam(name = "threadCount") int treadCount,
            @RequestParam(name = "userBegin") int userBegin,
            @RequestParam(name = "userEnd") int userEnd,
            @RequestParam(name = "transactioType") int transactioType) throws Exception {
        runTest(treadCount, userBegin, userEnd, transactioType);
        return running;
    }

    @GetMapping(path = "stop")
    public boolean stop() {
        synchronized (LoadTestEndpoint.class) {
            if (running && executor != null) {
                running = false;
                executor.shutdownNow();
            }
        }
        return running;
    }

    public static synchronized void runTest(int threadCount, int userBegin, int userEnd,
            int transactioType) throws Exception {
        if (running) {
            return;
        }

        LOGGER.info("starting load test. tread={}, userBein={}, transactionType={}", threadCount, userBegin, transactioType);
        //定义线程数    
        executor = Executors.newFixedThreadPool(threadCount);
        running = true;

        // 开始测试
        for (int i = 0; i < threadCount; i++) {
            int id = i + 1;
            executor.submit(new Runnable() {
                public void run() {
                    String name = "ThreadName:" + Thread.currentThread().getName();

                    while (running) {
                        Random r = new Random();
                        long userId = userBegin + id; //非并发场景
                        long productId = Math.abs(r.nextLong()) % 100 + 1; //并发场景
                        long priceId = Math.abs(r.nextLong()) % 2;
                        double price;
                        if (priceId == 0) {
                            price = 1000000D;
                        } else {
                            price = 99939393D;
                        }

                        System.out.println(
                                String.format("In thread %s, buy userid%s, productId%s, price%s start buy",
                                        name,
                                        userId,
                                        productId,
                                        price));
                        try {
                            if (transactioType == 2) {
                                customerService.buyWithTransactionSaga(userId, productId, price);
                            } else if (transactioType == 1) {
                                customerService.buyWithTransactionTCC(userId, productId, price);
                            } else {
                                customerService.buyWithoutTransaction(userId, productId, price);
                            }
                            System.out.println(
                                    String.format("In thread %s, buy userid%s, productId%s, price%s successxxxxx",
                                            name,
                                            userId,
                                            productId,
                                            price));
                        } catch (Throwable e) {
                            e.printStackTrace();
                            System.out.println(String.format("In thread %s, message %s", name, e.getMessage()));
                        }
                    }
                }
            });
        } ;
    }
}
