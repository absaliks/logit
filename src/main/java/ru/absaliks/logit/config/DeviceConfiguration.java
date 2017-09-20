package ru.absaliks.logit.config;

import java.util.Map;
import lombok.Data;

@Data
public class DeviceConfiguration {

  private Map<DeviceVariables, Integer> variables;
}
