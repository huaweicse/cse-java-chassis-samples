/*
 * Copyright 2017 Huawei Technologies Co., Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.consumer;

import org.apache.servicecomb.provider.pojo.RpcReference;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.apache.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.netflix.config.DynamicPropertyFactory;

@RestSchema(schemaId="hello")
@RequestMapping(path = "/hello")
public class ConsumerHelloService {
  private static org.slf4j.Logger log = LoggerFactory.getLogger(ConsumerHelloService.class);

  @RpcReference(microserviceName="helloprovider", schemaId="hello")
  Hello client;

  @Value(value = "${cse.dynamic.property:null}")
  String value;

  @Autowired
  Configuration config;

  RestTemplate restTemplate = RestTemplateBuilder.create();
  
  @RequestMapping(method = RequestMethod.GET)
  public String hello(String name) {
    log.info("Access /hello rpc, and name is " + name);
    String rpcCall = client.sayHi(name);
    log.info("Access /hello rest, and name is " + name);
    String restTemplateCall = restTemplate.getForObject("cse://helloprovider/hello/sayhi?name=" + name, String.class);
    return rpcCall + "-" + restTemplateCall;
  }

  @RequestMapping(method = RequestMethod.GET, path="dynamicProperty")
  public String dynamicProperty() {
    String dynamicProperty = DynamicPropertyFactory.getInstance().getStringProperty("cse.dynamic.property", "").get();
    String configProperty = config.getProperty();
    return "@Value is " + value + "; Api read is " + dynamicProperty + "; config Property is: " + configProperty;
  }
}
