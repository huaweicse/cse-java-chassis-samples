package com.huawei.cse.porter.oauth2;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserEndpoint {

  @GetMapping("/user/{id}")
  @ResponseBody
  public ResponseEntity<String> getUser(@PathVariable String id) {
    System.out.println(id);
    return ResponseEntity.ok("user id : " + id);
  }

}
