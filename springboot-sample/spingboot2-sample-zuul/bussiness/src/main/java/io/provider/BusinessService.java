package io.provider;

import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

// @RestController = @RestSchema(schemaId = "io.provider.BusinessService")
// @RestController

@RestSchema(schemaId = "BusinessService")
@RequestMapping(path = "/user")
public class BusinessService implements IBusinessService {
  @GetMapping(path = "hello")
  public String hello(@RequestParam(name = "name") String name) {
    return String.format("hello %s", name);
  }
}
