package com.huawei.cse.houseapp.product.api;

public class ProductInfo {
    private long id;

    private String productName;
    
    private boolean reserved;
    
    private double price;
    
    private boolean sold;
    
    private long reservedUserId;

    public long getId() {
        return id;
    }

    public void setId(long productId) {
        this.id = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getReservedUserId() {
        return reservedUserId;
    }

    public void setReservedUserId(long reservedUserId) {
        this.reservedUserId = reservedUserId;
    }
    
    
}
