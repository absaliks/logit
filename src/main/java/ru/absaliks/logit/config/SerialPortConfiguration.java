package ru.absaliks.logit.config;

import static java.util.Objects.isNull;

import com.ghgande.j2mod.modbus.Modbus;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SerialPortConfiguration {
  public static final List<String> VALID_STOP_BITS = Arrays.asList("1", "1.5", "2");
  public static final List<Integer> VALID_BAUD_RATE = Arrays.asList(300, 600, 1200, 2400, 4800,
      9600, 14400, 19200, 38400, 56000, 57600, 115200, 128000, 256000);

  public String portName;
  public Integer baudRate = 9600;
  public SerialParity parity = SerialParity.N;
  public String stopBits = "1";

  public void validate() {
    if (isNull(portName) || portName.isEmpty()) {
      throw new RuntimeException("Не указан COM порт");
    }
    if (!VALID_BAUD_RATE.contains(baudRate)) {
      throw new RuntimeException("Некорректная скорость COM порта");
    }
    if (isNull(parity)) {
      throw new RuntimeException("Не указана четность");
    }
    if (!VALID_STOP_BITS.contains(stopBits)) {
      throw new RuntimeException("Некорректное количество стоповых бит");
    }
  }

  @Override
  public String toString() {
    return "SerialPortConfiguration{" +
        "portName='" + portName + '\'' +
        ", baudRate=" + baudRate +
        ", parity=" + parity +
        ", stopBits=" + stopBits +
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
    SerialPortConfiguration that = (SerialPortConfiguration) o;
    return Objects.equals(portName, that.portName) &&
        Objects.equals(baudRate, that.baudRate) &&
        Objects.equals(parity, that.parity) &&
        Objects.equals(stopBits, that.stopBits);
  }

  @Override
  public int hashCode() {
    return Objects.hash(portName, baudRate, parity, stopBits);
  }
}
