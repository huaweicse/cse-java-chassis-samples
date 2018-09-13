package com.huawei.cse.saga.hotel.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.huawei.cse.saga.hotel.dao.HotelMapper;

@Service
public class HotelService {
  @Inject
  private HotelMapper mapper;

  public boolean order(String transactionId, int count) {
    if (mapper.getBookedCount() + count > mapper.getHotelCount()) {
      return false;
    }
    mapper.insertBookingRecord(transactionId, count);
    return true;
  }

  public void pay(int money) {
    // TODO
  }
}
