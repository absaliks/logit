package ru.absaliks.logit.config;

import static java.util.Objects.isNull;

import lombok.EqualsAndHashCode;
import lombok.ToString;


@ToString
@EqualsAndHashCode
public class TCPConfiguration {
  public String address;
  public Integer port;

  public void validate() {
    if (isNull(address) || address.isEmpty()) {
      throw new IllegalStateException("Не указан адрес для подключения");
    }
    if (port == 0) {
      throw new IllegalStateException("Не указан порт для подключения");
    }
  }
}
