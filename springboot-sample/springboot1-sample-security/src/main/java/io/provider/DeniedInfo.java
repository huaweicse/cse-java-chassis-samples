package io.provider;

import java.util.Date;

public class DeniedInfo {
  private Date timestamp;

  private String message;

  private String basicErrorControllerMessage;

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getBasicErrorControllerMessage() {
    return basicErrorControllerMessage;
  }

  public void setBasicErrorControllerMessage(String basicErrorControllerMessage) {
    this.basicErrorControllerMessage = basicErrorControllerMessage;
  }
}
