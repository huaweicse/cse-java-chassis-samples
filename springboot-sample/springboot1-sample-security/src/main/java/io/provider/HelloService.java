package io.provider;

import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//@RestSchema(schemaId="hello")
@RestController
@RequestMapping(path = "/hello")
public class HelloService implements Hello {
  private static org.slf4j.Logger log = LoggerFactory.getLogger(HelloService.class);

  @Override
  @RequestMapping(path = "/sayhi", method = RequestMethod.GET)
  public String sayHi(@RequestParam(name = "name", required = false) String name) {
    log.info("Access /hello/sayhi, and name is " + name);
    return "from provider: Hello " + name;
  }
}
