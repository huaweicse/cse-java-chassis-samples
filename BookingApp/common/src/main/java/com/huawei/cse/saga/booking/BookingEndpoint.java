package com.huawei.cse.saga.booking;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;


public interface BookingEndpoint {

  String order(@Size(min = 5, max = 30) String bookingId,
      @Min(1) @Max(10) int carCount,
      @Min(1) @Max(10) int hotelCout);
}
