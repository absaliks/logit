package ru.absaliks.logit.config;

import static java.util.Objects.isNull;

import java.util.Objects;

public class TCPConfiguration {
  public String address;
  public Integer port;

  public void validate() {
    if (isNull(address) || address.isEmpty()) {
      throw new RuntimeException("Не указан адрес для подключения");
    }
    if (port == 0) {
      throw new RuntimeException("Не указан порт для подключения");
    }
  }

  @Override
  public String toString() {
    return "TCPConfiguration{" +
        "address='" + address + '\'' +
        ", port=" + port +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TCPConfiguration that = (TCPConfiguration) o;
    return Objects.equals(address, that.address) &&
        Objects.equals(port, that.port);
  }

  @Override
  public int hashCode() {
    return Objects.hash(address, port);
  }
}
