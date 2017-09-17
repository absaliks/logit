package ru.absaliks.logit.config;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.ArrayUtils;

@XmlRootElement
public class Config {
  private static final Logger LOG = Logger.getLogger(Config.class.getName());
  private static Config instance;

  public ModbusInterface currentInterface;
  public ModbusConfiguration modbus;
  public SerialPortConfiguration serial;
  public TCPConfiguration tcp;
  public String csvDivisor;
  public int[] real32ByteOrder;
  public int[] uint32ByteOrder;
  public boolean openFileAfterSaving;

  public Config() {
    currentInterface = ModbusInterface.COM;
    modbus = new ModbusConfiguration();
    serial = new SerialPortConfiguration();
    tcp = new TCPConfiguration();
    uint32ByteOrder = new int[]{2, 3, 0, 1};
  }

  public static void init() {
    try {
      instance = ConfigBuilder.fromFile();
      instance.validate();
    } catch (FileNotFoundException e) {
      LOG.log(Level.WARNING, "Не найден конфигурационный файл [" + e.getMessage() + "]", e);
    } catch (JAXBException e) {
      LOG.log(Level.WARNING, "Ошибка чтения конфигурационного файла: " + e.getMessage(), e);
    }

    if (isNull(instance)) {
      instance = new Config();
    }
  }

  private void validate() {
    System.out.println(this);
    validateUint32ByteOrder();
    validateReal32ByteOrder();
  }

  private void validateUint32ByteOrder() {
    try {
      validate32bitByteOrder(uint32ByteOrder);
    } catch (Exception e) {
      uint32ByteOrder = new int[]{1, 0, 3, 2};
      LOG.warning("Incorrect UInt32 byteorder is provided, setting default: " + Arrays.toString(uint32ByteOrder));
    }
  }

  private void validateReal32ByteOrder() {
    try {
      validate32bitByteOrder(real32ByteOrder);
    } catch (Exception e) {
      real32ByteOrder = new int[]{1, 0, 3, 2};
      LOG.warning("Incorrect UInt32 byteorder is provided, setting default: " + Arrays.toString(real32ByteOrder));
    }
  }

  private void validate32bitByteOrder(int[] byteOrder) {
    if (nonNull(byteOrder) && (
        byteOrder.length != 4 ||
        ArrayUtils.indexOf(byteOrder, 0) == -1 ||
        ArrayUtils.indexOf(byteOrder, 1) == -1 ||
        ArrayUtils.indexOf(byteOrder, 2) == -1 ||
        ArrayUtils.indexOf(byteOrder, 3) == -1
    )) {
      throw new IllegalArgumentException();
    }
  }

  public static Config getInstance() {
    return instance;
  }

  public SerialPortConfiguration getSerial() {
    return serial;
  }

  @Override
  public String toString() {
    return "Config{" +
        "currentInterface=" + currentInterface +
        ", modbus=" + modbus +
        ", serial=" + serial +
        ", tcp=" + tcp +
        ", csvDivisor='" + csvDivisor + '\'' +
        ", real32ByteOrder=" + Arrays.toString(real32ByteOrder) +
        ", uint32ByteOrder=" + Arrays.toString(uint32ByteOrder) +
        ", openFileAfterSaving=" + openFileAfterSaving +
        '}';
  }
}