package com.huawei.cse.saga.hotel.dao;

public interface HotelMapper {
  int getHotelCount();
  void insertBookingRecord(String transactionId,int count);
  int getBookedCount();
}
