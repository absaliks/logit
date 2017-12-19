package ru.absaliks.logit.config;

import static ru.absaliks.logit.config.DataType.REAL32;
import static ru.absaliks.logit.config.DataType.UINT16;
import static ru.absaliks.logit.config.DataType.UINT32;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DefaultDeviceVariable {
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
  JOURNAL_EVENT_CODE("Журнал - Код события", 40016, UINT16),
  JOURNAL_TIMESTAMP("Журнал - Время в секундах, начиная с 1980 г", 40017, UINT32),

  // Archive
  ARCHIVE_PUMP_POWER("Архив - Подача насоса, л/ч", 40020, REAL32),
  ARCHIVE_TIMESTAMP("Архив - Время в секундах, начиная с 1980 г", 40022, UINT32);

  private final String label;
  private final int defaultAddress;
  private final DataType dataType;
}
