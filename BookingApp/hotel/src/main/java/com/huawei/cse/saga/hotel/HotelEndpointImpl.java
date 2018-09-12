package com.huawei.cse.saga.hotel;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestSchema(schemaId = "HotelEndpoint")
@RequestMapping(path = "/hotel")
public class HotelEndpointImpl implements HotelEndpoint {

  @Override
  @GetMapping(path = "/order")
  public boolean order(@RequestParam(name = "count") @Min(1) @Max(10) int count) {
    return false;
  }

  @Override
  @GetMapping(path = "/pay")
  public boolean pay(@RequestParam(name = "money") @Min(1) int money) {
    return false;
  }

}
