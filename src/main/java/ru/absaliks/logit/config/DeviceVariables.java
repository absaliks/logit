package ru.absaliks.logit.config;

import static ru.absaliks.logit.config.DataType.*;

import lombok.Getter;

@Getter
public enum DeviceVariables {
  // Service Page Variables
  SERVICE_PAGE_MARKER("Маркет сервисной страницы", 40001, UINT16),
  CONTROLLER_MODEL("Модель контроллера", 40002, UINT16),
  SERIAL_NO("Серийный номер", 40003, UINT16),
  JOURNAL_PAGE_REFERENCE("Начальный адрес журнала", 40004, UINT16),
  JOURNAL_LENGTH("Количество страниц журнала", 40005, UINT16),
  JOURNAL_CURRENT_PAGE_REFERENCE("Текущая страница архива", 40008, UINT16),
  ARCHIVE_PAGE_REFERENCE("Начальный адрес архива", 40006, UINT16),
  ARCHIVE_LENGTH("Количество страниц архива", 40007, UINT16),
  ARCHIVE_CURRENT_PAGE_REFERENCE("Текущая страница архива", 40009, UINT16),
  PAGE_READY_REFERENCE("Маркер готовности страницы для чтения", 40010, UINT16),

  // Journal
  JOURNAL_EVENT_CODE("Код события", 40016, UINT16),
  JOURNAL_TIMESTAMP("Время в секундах, начиная с 1980 г", 40017, UINT32),

  // Archive
  ARCHIVE_PUMP_POWER("Подача насоса, л/ч", 40020, REAL32),
  ARCHIVE_TIMESTAMP("Время в секундах, начиная с 1980 г", 40022, UINT32);

  private String label;
  private int address;
  private DataType dataType;

  DeviceVariables(String label, int address, DataType dataType) {
    this.label = label;
    this.address = address;
    this.dataType = dataType;
  }
}
