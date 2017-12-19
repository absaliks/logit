package ru.absaliks.logit.config;

import javax.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Log4j2
@ToString
@EqualsAndHashCode
@XmlRootElement
public class Config {
  public ModbusInterface currentInterface;
  public boolean openFileAfterSaving;
  public ModbusConfiguration modbus;
  public SerialPortSettings serial;
  public TCPSettings tcp;
  public DeviceConfiguration deviceConfiguration;
  public String csvDivisor;
  public ByteOrder32bit real32ByteOrder;
  public ByteOrder32bit uint32ByteOrder;

  public Config() {
    currentInterface = ModbusInterface.COM;
    modbus = new ModbusConfiguration();
    serial = new SerialPortSettings();
    tcp = new TCPSettings();
    deviceConfiguration = new DeviceConfiguration();
    real32ByteOrder = new ByteOrder32bit(new int[]{2, 3, 0, 1});
    uint32ByteOrder = new ByteOrder32bit(new int[]{2, 3, 0, 1});
  }
}