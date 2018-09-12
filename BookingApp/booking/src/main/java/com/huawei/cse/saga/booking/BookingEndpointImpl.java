package com.huawei.cse.saga.booking;

import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestSchema(schemaId = "BookingEndpoint")
@RequestMapping(path = "/booking")
public class BookingEndpointImpl implements BookingEndpoint {

  @Override
  @GetMapping(path = "/order")
  public String order(@RequestParam(value = "bookingId") String bookingId,
      @RequestParam(value = "carCount") int carCount,
      @RequestParam(value = "hotelCout") int hotelCout) {
    return null;
  }



}
