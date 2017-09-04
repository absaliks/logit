package ru.absaliks.logit.config;

public class ModbusConfiguration {
  public int deviceId = 1;
  public int timeout = 3000;
  public int delayBetweenRequests = 200;

  @Override
  public String toString() {
    return "ModbusConfiguration{" +
        "deviceId=" + deviceId +
        ", timeout=" + timeout +
        ", delayBetweenRequests=" + delayBetweenRequests +
        '}';
  }
}
