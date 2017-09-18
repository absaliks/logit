package ru.absaliks.logit.config;

import static java.util.Objects.isNull;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Config {
  private static final Logger LOG = Logger.getLogger(Config.class.getName());
  private static Config instance;

  public ModbusInterface currentInterface;
  public boolean openFileAfterSaving;
  public ModbusConfiguration modbus;
  public SerialPortConfiguration serial;
  public TCPConfiguration tcp;
  public String csvDivisor;
  public ByteOrder32bit real32ByteOrder;
  public ByteOrder32bit uint32ByteOrder;

  public Config() {
    currentInterface = ModbusInterface.COM;
    modbus = new ModbusConfiguration();
    serial = new SerialPortConfiguration();
    tcp = new TCPConfiguration();
    uint32ByteOrder = new ByteOrder32bit(new int[]{2, 3, 0, 1});
    real32ByteOrder = new ByteOrder32bit(new int[]{2, 3, 0, 1});
  }

  public static void init() {
    try {
      instance = ConfigBuilder.fromFile();
    } catch (FileNotFoundException e) {
      LOG.log(Level.WARNING, "Не найден конфигурационный файл [" + e.getMessage() + "]", e);
    } catch (JAXBException e) {
      LOG.log(Level.WARNING, "Ошибка чтения конфигурационного файла: " + e.getMessage(), e);
      throw new RuntimeException(e);
    }
    if (isNull(instance)) {
      instance = new Config();
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
        ", real32ByteOrder=" + real32ByteOrder +
        ", uint32ByteOrder=" + uint32ByteOrder +
        ", openFileAfterSaving=" + openFileAfterSaving +
        '}';
  }
}