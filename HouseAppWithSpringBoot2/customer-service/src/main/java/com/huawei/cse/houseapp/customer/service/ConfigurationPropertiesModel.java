package com.huawei.cse.houseapp.customer.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "cse.test.house")
public class ConfigurationPropertiesModel {
  private String modelValue;
  
  private String yamlValue;
  
  private String yamlValueOverride;

  public String getModelValue() {
    return modelValue;
  }

  public void setModelValue(String modelValue) {
    this.modelValue = modelValue;
  }

  public String getYamlValueOverride() {
    return yamlValueOverride;
  }

  public void setYamlValueOverride(String yamlValueOverride) {
    this.yamlValueOverride = yamlValueOverride;
  }

  public String getYamlValue() {
    return yamlValue;
  }

  public void setYamlValue(String yamlValue) {
    this.yamlValue = yamlValue;
  }
  
}
