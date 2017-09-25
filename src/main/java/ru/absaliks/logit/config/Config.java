package ru.absaliks.logit.config;

import static java.util.Objects.isNull;

import java.io.FileNotFoundException;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Log4j2
@ToString
@EqualsAndHashCode
@XmlRootElement
public class Config {
  private static Config instance;

  public ModbusInterface currentInterface = ModbusInterface.COM;
  public boolean openFileAfterSaving;
  public ModbusConfiguration modbus = new ModbusConfiguration();
  public SerialPortConfiguration serial = new SerialPortConfiguration();
  public TCPConfiguration tcp = new TCPConfiguration();
  public String csvDivisor;
  public ByteOrder32bit real32ByteOrder = new ByteOrder32bit(new int[]{2, 3, 0, 1});
  public ByteOrder32bit uint32ByteOrder = new ByteOrder32bit(new int[]{2, 3, 0, 1});

  public static void init() {
    try {
      instance = ConfigBuilder.fromFile(false);
    } catch (FileNotFoundException e) {
      log.warn("Не найден конфигурационный файл " + e.getMessage(), e);
    } catch (JAXBException e) {
      log.warn("Ошибка чтения конфигурационного файла: " + e.getMessage(), e);
      throw new RuntimeException(e);
    }
    if (isNull(instance)) {
      instance = new Config();
    }
  }

  public static Config getInstance() {
    return instance;
  }
}