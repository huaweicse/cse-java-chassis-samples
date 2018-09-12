package com.huawei.cse.saga.hotel;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public interface HotelEndpoint {
  boolean order(@Min(1) @Max(10) int carCount);

  boolean pay(@Min(1) int money);
}
