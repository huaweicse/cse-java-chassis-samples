package com.huawei.cse.houseapp.customer.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "cse.test.house")
public class ConfigurationPropertiesModel {
  private String modelValue;

  public String getModelValue() {
    return modelValue;
  }

  public void setModelValue(String modelValue) {
    this.modelValue = modelValue;
  }
  
}
