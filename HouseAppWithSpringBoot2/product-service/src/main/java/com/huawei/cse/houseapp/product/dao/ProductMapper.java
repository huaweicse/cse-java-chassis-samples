package com.huawei.cse.houseapp.product.dao;

import java.util.List;

import com.huawei.cse.houseapp.product.api.ProductInfo;

public interface ProductMapper {
    List<ProductInfo> getAllProducts();
    
    void createProduct(ProductInfo info);
    
    ProductInfo getProductInfo(long productId);
    
    void updateProductInfo(ProductInfo info);
    
    void clear();

    Double queryReduced();
}
