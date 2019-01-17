package com.huawei.cse.houseapp.product.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huawei.cse.houseapp.product.api.ProductInfo;

public class MockedProductMapper implements ProductMapper {
    Map<Long, ProductInfo> products = new HashMap<>();

    public MockedProductMapper() {
    }

    @Override
    public ProductInfo getProductInfo(long productId) {
        return products.get(productId);
    }

    @Override
    public void updateProductInfo(ProductInfo info) {

    }

    @Override
    public void createProduct(ProductInfo info) {
        products.putIfAbsent(info.getId(), info);
    }

    @Override
    public void clear() {
        products.clear();
    }

    @Override
    public List<ProductInfo> getAllProducts() {
        List<ProductInfo> r = new ArrayList<ProductInfo>(products.values().size());
        r.addAll(products.values());
        return r;
    }

    @Override
    public Double queryReduced() {
        double sold = 0D;
        for(ProductInfo info : products.values()) {
            if(info.isSold()) {
                sold = sold + info.getPrice();
            }
        }
        return sold;
    }

}
