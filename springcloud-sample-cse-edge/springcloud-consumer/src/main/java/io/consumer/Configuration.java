package io.consumer;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="cse.dynamic")
public class Configuration {
  private String property;

  public String getProperty() {
    return property;
  }

  public void setProperty(String property) {
    this.property = property;
  }
  
}
