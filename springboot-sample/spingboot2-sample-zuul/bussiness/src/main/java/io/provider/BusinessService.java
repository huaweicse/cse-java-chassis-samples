package io.provider;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/user")
public class BusinessService {
  @GetMapping(path = "hello")
  public String hello(@RequestParam(name = "name") String name) {
    return String.format("hello %s", name);
  }
}
