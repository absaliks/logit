package ru.absaliks.logit.view.connectionsettings;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static javafx.collections.FXCollections.observableArrayList;

import java.util.Set;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javax.inject.Inject;
import ru.absaliks.logit.config.Config;
import ru.absaliks.logit.config.ModbusConfiguration;
import ru.absaliks.logit.config.ModbusInterface;
import ru.absaliks.logit.config.SerialParity;
import ru.absaliks.logit.config.SerialPortSettings;
import ru.absaliks.logit.config.TCPSettings;
import ru.absaliks.logit.service.ModbusService;

public class ConnectionSettingsPresenter {

  ObservableList<SerialParity> parityList = observableArrayList(SerialParity.values());
  ObservableList<String> stopBitList = observableArrayList(SerialPortSettings.VALID_STOP_BITS);
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
  TextField uint32ByteOrder;
  @FXML
  TextField real32ByteOrder;
  @FXML
  TableColumn deviceVariableName;
  @FXML
  TableColumn deviceVariableAddress;
  @FXML
  TableColumn deviceVariableErrorMessage;

  @Inject
  Config config;

  @FXML
  private void initialize() {
    initModbusInterfaceComboBox();
    initComPortComboBox();
    initComBaudRateChoiceBox();
    initComParityChoiceBox();
    initComStopBitsChoiceBox();
    initTcpAddressTextField();
    initTcpPortTextField();
    initModbusDeviceIdTextField();
    initModbusTimeoutTextField();
    initCsvDivisorChoiseBox();
    initDelayBetweenRequestsTextField();
    initUint32ByteOrderControl();
    initReal32ByteOrderControl();
  }

  private void initModbusInterfaceComboBox() {
    modbusInterface.getItems().addAll(ModbusInterface.values());
    modbusInterface.setValue(config.currentInterface);
    modbusInterface.setOnAction(e -> config.currentInterface = modbusInterface.getValue());
  }

  private void initComPortComboBox() {
    SerialPortSettings serialConfig = config.serial;
    Set<String> comPorts = ModbusService.getInstance().getRegisteredCOMPorts();
    comPort.getItems().addAll(comPorts);
    if (nonNull(serialConfig.portName) && comPorts.contains(serialConfig.portName)) {
      comPort.setValue(serialConfig.portName);
    }
    comPort.setOnAction(e -> serialConfig.portName = comPort.getValue());
  }

  private void initComBaudRateChoiceBox() {
    SerialPortSettings serialConfig = config.serial;
    comBaudRate.getItems().addAll(SerialPortSettings.VALID_BAUD_RATE);
    comBaudRate.setValue(serialConfig.baudRate);
    comBaudRate.setOnAction(e -> serialConfig.baudRate = comBaudRate.getValue());
  }

  private void initComParityChoiceBox() {
    SerialPortSettings serialConfig = config.serial;
    comParity.setItems(parityList);
    comParity.setValue(serialConfig.parity);
    comParity.setOnAction(e -> serialConfig.parity = comParity.getValue());
  }

  private void initComStopBitsChoiceBox() {
    SerialPortSettings serialConfig = config.serial;
    comStopBits.setItems(stopBitList);
    comStopBits.setValue(serialConfig.stopBits);
    comStopBits.setOnAction(e -> serialConfig.stopBits = comStopBits.getValue());
  }

  private void initTcpAddressTextField() {
    TCPSettings tcpConfig = config.tcp;
    tcpAddress.setText(tcpConfig.address);
    tcpAddress.textProperty().addListener(e -> tcpConfig.address = tcpAddress.getText());
  }

  private void initTcpPortTextField() {
    TCPSettings tcpConfig = config.tcp;
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

  private void initModbusDeviceIdTextField() {
    ModbusConfiguration modbusConfig = config.modbus;
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

  private void initModbusTimeoutTextField() {
    ModbusConfiguration modbusConfig = config.modbus;
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

  private void initDelayBetweenRequestsTextField() {
    ModbusConfiguration modbusConfig = config.modbus;
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

  private void initUint32ByteOrderControl() {
    uint32ByteOrder.setText(config.uint32ByteOrder.toStringValue());
    uint32ByteOrder.textProperty().addListener((observable, oldValue, newValue) -> {
      try {
        config.uint32ByteOrder.fromStringValue(newValue);
        uint32ByteOrder.setStyle(null);
      } catch (Exception e) {
        uint32ByteOrder.setStyle("-fx-background-color: red");
      }
    });
  }

  private void initReal32ByteOrderControl() {
    real32ByteOrder.setText(config.real32ByteOrder.toStringValue());
    real32ByteOrder.textProperty().addListener((observable, oldValue, newValue) -> {
      try {
        config.real32ByteOrder.fromStringValue(newValue);
        real32ByteOrder.setStyle(null);
      } catch (Exception e) {
        real32ByteOrder.setStyle("-fx-background-color: red");
      }
    });
  }

  private void initCsvDivisorChoiseBox() {
    csvDivisor.getItems().addAll(csvDivisorList);
    csvDivisor.setValue(config.csvDivisor);
    csvDivisor.setOnAction(e -> config.csvDivisor = csvDivisor.getValue());
  }
}
