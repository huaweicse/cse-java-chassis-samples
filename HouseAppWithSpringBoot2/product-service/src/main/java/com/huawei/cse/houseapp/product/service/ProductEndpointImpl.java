package com.huawei.cse.houseapp.product.service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;

import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.huawei.cse.houseapp.product.api.ProductEndpoint;
import com.huawei.cse.houseapp.product.api.ProductInfo;
import com.huawei.cse.houseapp.product.dao.ProductMapper;
import com.netflix.config.DynamicPropertyFactory;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiResponse;

@RestSchema(schemaId = "product")
@RequestMapping(path = "/")
public class ProductEndpointImpl implements ProductEndpoint {
  private AtomicLong db = new AtomicLong(0);

  private static AtomicLong reqCount = new AtomicLong(0);

  private static AtomicLong lastStatTime = new AtomicLong(System.currentTimeMillis());

  static {
    new Thread(() -> {
      while (true) {
        reqCount.compareAndSet(reqCount.get(), 0L);
        lastStatTime.compareAndSet(lastStatTime.get(), System.currentTimeMillis());
        try {
          Thread.sleep(60000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();
  }

  // private ProductMapper productMapper = new MockedProductMapper(); //内存测试
  @Inject
  private ProductMapper productMapper;

  @Inject
  PlatformTransactionManager txManager;

  @Inject
  ProductService productService;

  @GetMapping(path = "cpu")
  public long cpuExtensive(@RequestParam(name = "base") long base) {
    faultInjection();
    if (base < 0) {
      throw new InvocationException(400, "", "bad param");
    }
    long result = base;
    long next = base - 1;
    while (next > 0) {
      result = Math.max(next * result, result);
      next = next - 1;
    }
    return result;
  }

  @GetMapping(path = "mem")
  public long memExtensive(@RequestParam(name = "base") int base) {
    faultInjection();
    if (base < 0) {
      throw new InvocationException(400, "", "bad param");
    }
    int amout = base * 1024;
    String[] ss = new String[amout];
    for (int i = 0; i < ss.length; i++) {
      ss[i] = new String("i" + i);
    }
    return ss.length;
  }

  private void faultInjection() {
    int delay = DynamicPropertyFactory.getInstance().getIntProperty("cse.test.fault.delay", 0).get();
    if (delay > 1) {
      try {
        Thread.sleep(delay);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    int exception = DynamicPropertyFactory.getInstance().getIntProperty("cse.test.fault.exception", 0).get();
    if (exception > 1) {
      throw new InvocationException(400, "", "fault injected bad request");
    }
  }

  @Override
  @GetMapping(path = "searchAll")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "userId", dataType = "integer", format = "int32", paramType = "query")})
  public List<ProductInfo> searchAll(@RequestParam(name = "userId") int userId) {
    faultInjection();

    double qps = reqCount.incrementAndGet() * 1000.0d / (System.currentTimeMillis() - lastStatTime.get());
    int configQps = DynamicPropertyFactory.getInstance().getIntProperty("cse.test.product.qps", 10).get();
    if (qps > configQps) {
      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    long sleep = DynamicPropertyFactory.getInstance().getLongProperty("cse.test.product.wait", -1).get();
    if (sleep > 0) {
      try {
        Thread.sleep(sleep);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    return productMapper.getAllProducts();
  }

  @Override
  @GetMapping(path = "searchAllForCustomer")
  public List<ProductInfo> searchAllForCustomer() {
    return productMapper.getAllProducts();
  }

  // 实际是重置数据接口，不改名字了。
  @Override
  @PostMapping(path = "login")
  public long login(@RequestParam(name = "username") String username,
      @RequestParam(name = "password") String password) {
    // 使用测试账号登陆，登陆成功分配唯一的选房账号. 这里主要是为了并发和性能测试方便，实际业务场景需要按照要求设计。
    if ("test".equals(username) && "test".equals(password)) {
      productMapper.clear();

      for (int i = 1; i <= 100; i++) {
        ProductInfo info = new ProductInfo();
        info.setId(i);
        info.setPrice(1000000);
        if (i <= 9) {
          info.setProductName("product0" + i);
        } else {
          info.setProductName("product" + i);
        }
        info.setReserved(false);
        info.setReservedUserId(-1);
        info.setSold(false);
        productMapper.createProduct(info);
      }
      return 1L;
    } else {
      return -1;
    }
  }

  @Override
  @PostMapping(path = "buyWithoutTransaction")
  @ApiResponse(code = 400, response = String.class, message = "")
  public boolean buyWithoutTransaction(@RequestParam(name = "productId") long productId,
      @RequestParam(name = "userId") long userId,
      @RequestParam(name = "price") double price) {
    ProductInfo info = productMapper.getProductInfo(productId);
    if (info == null) {
      throw new InvocationException(400, "", "product id not valid");
    }
    if (price != info.getPrice()) {
      throw new InvocationException(400, "", "product price not valid");
    }
    if (info.isSold()) {
      throw new InvocationException(400, "", "product already sold");
    }
    info.setSold(true);
    info.setReservedUserId(userId);
    productMapper.updateProductInfo(info);
    return true;
  }

  @Override
  @PostMapping(path = "buyWithTransactionSaga")
  @ApiResponse(code = 400, response = String.class, message = "")
  public boolean buyWithTransactionSaga(@RequestParam(name = "productId") long productId,
      @RequestParam(name = "userId") long userId,
      @RequestParam(name = "price") double price) {
    return productService.buyWithTransactionSaga(productId, userId, price);
  }

  @Override
  @PostMapping(path = "buyWithTransactionTCC")
  @ApiResponse(code = 400, response = String.class, message = "")
  public boolean buyWithTransactionTCC(@RequestParam(name = "productId") long productId,
      @RequestParam(name = "userId") long userId,
      @RequestParam(name = "price") double price) {
    return productService.buyWithTransactionTCC(productId, userId, price);
  }

  @Override
  @PostMapping(path = "add")
  public void addProduct(double price) {
    ProductInfo info = new ProductInfo();
    long i = db.incrementAndGet();
    info.setId(i);
    info.setPrice(1000000);
    info.setProductName("product" + i);
    info.setReserved(false);
    info.setReservedUserId(-1);
    info.setSold(false);
    productMapper.createProduct(info);

  }

  @Override
  @GetMapping(path = "queryReduced")
  public double queryReduced() {
    Double reduced = productMapper.queryReduced();
    if (reduced == null) {
      return 0D;
    } else {
      return reduced;
    }
  }
}
