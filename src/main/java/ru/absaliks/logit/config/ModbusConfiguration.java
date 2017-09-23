package ru.absaliks.logit.config;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class ModbusConfiguration {
  public int deviceId = 1;
  public int timeout = 3000;
  public int delayBetweenRequests = 200;
}
