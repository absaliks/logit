package ru.absaliks.logit.view;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static javafx.collections.FXCollections.observableArrayList;

import java.util.Set;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import ru.absaliks.logit.config.Config;
import ru.absaliks.logit.config.ModbusConfiguration;
import ru.absaliks.logit.config.ModbusInterface;
import ru.absaliks.logit.config.SerialParity;
import ru.absaliks.logit.config.SerialPortConfiguration;
import ru.absaliks.logit.config.TCPConfiguration;
import ru.absaliks.logit.service.ModbusService;

public class ConfigFrameController {

  ObservableList<SerialParity> parityList = observableArrayList(SerialParity.values());
  ObservableList<String> stopBitList = observableArrayList(SerialPortConfiguration.VALID_STOP_BITS);
  ObservableList<String> csvDivisorList = observableArrayList(";", "tab");

  @FXML
  ChoiceBox<ModbusInterface> modbusInterface;
  @FXML
  ComboBox<String> comPort;
  @FXML
  ChoiceBox<Integer> comBaudRate;
  @FXML
  ChoiceBox<SerialParity> comParity;
  @FXML
  ChoiceBox<String> comStopBits;
  @FXML
  TextField tcpAddress;
  @FXML
  TextField tcpPort;
  @FXML
  TextField modbusDeviceId;
  @FXML
  TextField modbusTimeout;
  @FXML
  ChoiceBox<String> csvDivisor;
  @FXML
  TextField delayBetweenRequests;

  @FXML
  private void initialize() {
    Config config = Config.getInstance();
    initModbusInterfaceComboBox(config);
    initComPortComboBox(config.serial);
    initComBaudRateChoiceBox(config.serial);
    initComParityChoiceBox(config.serial);
    initComStopBitsChoiceBox(config.serial);
    initTcpAddressTextField(config.tcp);
    initTcpPortTextField(config.tcp);
    initModbusDeviceIdTextField(config.modbus);
    initModbusTimeoutTextField(config.modbus);
    initCsvDivisorChoiseBox(config);
    initDelayBetweenRequestsTextField(config.modbus);
  }

  private void initModbusInterfaceComboBox(Config config) {
    modbusInterface.getItems().addAll(ModbusInterface.values());
    modbusInterface.setValue(config.currentInterface);
    modbusInterface.setOnAction(e -> config.currentInterface = modbusInterface.getValue());
  }

  private void initComPortComboBox(SerialPortConfiguration serialConfig) {
    Set<String> comPorts = ModbusService.getInstance().getRegisteredCOMPorts();
    comPort.getItems().addAll(comPorts);
    if (nonNull(serialConfig.portName) && comPorts.contains(serialConfig.portName)) {
      comPort.setValue(serialConfig.portName);
    }
    comPort.setOnAction(e -> serialConfig.portName = comPort.getValue());
  }

  private void initComBaudRateChoiceBox(SerialPortConfiguration serialConfig) {
    comBaudRate.getItems().addAll(SerialPortConfiguration.VALID_BAUD_RATE);
    comBaudRate.setValue(serialConfig.baudRate);
    comBaudRate.setOnAction(e -> serialConfig.baudRate = comBaudRate.getValue());
  }

  private void initComParityChoiceBox(SerialPortConfiguration serialConfig) {
    comParity.setItems(parityList);
    comParity.setValue(serialConfig.parity);
    comParity.setOnAction(e -> serialConfig.parity = comParity.getValue());
  }

  private void initComStopBitsChoiceBox(SerialPortConfiguration serialConfig) {
    comStopBits.setItems(stopBitList);
    comStopBits.setValue(serialConfig.stopBits);
    comStopBits.setOnAction(e -> serialConfig.stopBits = comStopBits.getValue());
  }

  private void initTcpAddressTextField(TCPConfiguration tcpConfig) {
    tcpAddress.setText(tcpConfig.address);
    tcpAddress.textProperty().addListener(e -> tcpConfig.address = tcpAddress.getText());
  }

  private void initTcpPortTextField(TCPConfiguration tcpConfig) {
    setIntValueToTextField(tcpPort, tcpConfig.port);
    tcpPort.textProperty().addListener((observable, oldValue, newValue) -> {
      if ("".equals(newValue)) {
        tcpConfig.port = null;
      } else {
        try {
          int port = Integer.parseInt(newValue);
          if (port < 1 || port > 65535) {
            throw new IllegalArgumentException();
          }
          tcpConfig.port = port;
        } catch (IllegalArgumentException e1) {
          setIntValueToTextField(tcpPort, tcpConfig.port);
        }
      }
    });
  }

  private void setIntValueToTextField(TextField textField, Integer intValue) {
    textField.setText(isNull(intValue) ? "" : intValue.toString());
  }

  private void initModbusDeviceIdTextField(ModbusConfiguration modbusConfig) {
    setIntValueToTextField(modbusDeviceId, modbusConfig.deviceId);
    modbusDeviceId.textProperty().addListener((observable, oldValue, newValue) -> {
      if ("".equals(newValue)) {
        modbusConfig.deviceId = 1;
      } else {
        try {
          modbusConfig.deviceId = Integer.parseInt(newValue);
        } catch (IllegalArgumentException e1) {
          setIntValueToTextField(modbusDeviceId, modbusConfig.deviceId);
        }
      }
    });
  }

  private void initModbusTimeoutTextField(ModbusConfiguration modbusConfig) {
    setIntValueToTextField(modbusTimeout, modbusConfig.timeout);
    modbusTimeout.textProperty().addListener((observable, oldValue, newValue) -> {
      if ("".equals(newValue)) {
        modbusConfig.timeout = 1;
      } else {
        try {
          modbusConfig.timeout = Integer.parseInt(newValue);
        } catch (IllegalArgumentException e1) {
          setIntValueToTextField(modbusTimeout, modbusConfig.timeout);
        }
      }
    });
  }

  private void initDelayBetweenRequestsTextField(ModbusConfiguration modbusConfig) {
    setIntValueToTextField(delayBetweenRequests, modbusConfig.delayBetweenRequests);
    delayBetweenRequests.textProperty().addListener((observable, oldValue, newValue) -> {
      if ("".equals(newValue)) {
        modbusConfig.delayBetweenRequests = 200;
      } else {
        try {
          modbusConfig.delayBetweenRequests = Integer.parseInt(newValue);
        } catch (IllegalArgumentException e1) {
          setIntValueToTextField(delayBetweenRequests, modbusConfig.delayBetweenRequests);
        }
      }
    });
  }

  private void initCsvDivisorChoiseBox(Config config) {
    csvDivisor.getItems().addAll(csvDivisorList);
    csvDivisor.setValue(config.csvDivisor);
    csvDivisor.setOnAction(e -> config.csvDivisor = csvDivisor.getValue());
  }
}
