package ru.absaliks.logit;

import static java.util.Objects.nonNull;

import com.ghgande.j2mod.modbus.Modbus;
import com.ghgande.j2mod.modbus.facade.AbstractModbusMaster;
import com.ghgande.j2mod.modbus.facade.ModbusSerialMaster;
import com.ghgande.j2mod.modbus.facade.ModbusTCPMaster;
import com.ghgande.j2mod.modbus.util.SerialParameters;
import ru.absaliks.logit.config.Config;
import ru.absaliks.logit.config.SerialPortConfiguration;
import ru.absaliks.logit.config.TCPConfiguration;

public class ModbusMasterBuilder {

  public static AbstractModbusMaster createMaster() {
    switch (Config.getInstance().currentInterface) {
      case COM:
        return createSerialMaster();
      case TCP:
        return createTCPMaster();
      default:
        throw new UnsupportedOperationException("Не поддерживаемый интерфейс подключения");
    }
  }

  private static ModbusTCPMaster createTCPMaster() {
    Config config = Config.getInstance();
    TCPConfiguration tcp = config.tcp;
    tcp.validate();
    int timeout = nonNull(config.modbus) && nonNull(config.modbus.timeout) ?
        config.modbus.timeout : Modbus.DEFAULT_TIMEOUT;
    return new ModbusTCPMaster(tcp.address, tcp.port, timeout, true);
  }

  private static ModbusSerialMaster createSerialMaster() {
    return new ModbusSerialMaster(getSerialParameters());
  }

  private static SerialParameters getSerialParameters() {
    SerialPortConfiguration serial = Config.getInstance().getSerial();
    serial.validate();

    SerialParameters params = new SerialParameters();
    params.setPortName(serial.portName);
    params.setBaudRate(serial.baudRate);
    params.setParity(serial.parity.getTitle());
    params.setStopbits(serial.stopBits);
    params.setEncoding(Modbus.SERIAL_ENCODING_RTU);
    return params;
  }
}
