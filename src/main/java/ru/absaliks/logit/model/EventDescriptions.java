package ru.absaliks.logit.model;

import java.util.HashMap;
import java.util.Map;

public class EventDescriptions {
  private static final Map<Integer, String> data = new HashMap<>(10);
  static {
    data.put(1, "Давление максимальное");
    data.put(2, "Давление минимальное");
    data.put(3, "Уровень реагента минимальный");
    data.put(4, "Авария ДТ отсека");
    data.put(5, "Авария ДТ реагента");
    data.put(6, "Срабатывание охранной сигнализации");
    data.put(7, "Срабатывание пожарной сигнализации");
    data.put(8, "Изменение уставки “Темп. реагента максимальная”");
    data.put(9, "Изменение уставки “Темп. реагента минимальная”");
    data.put(10, "Изменение уставки “Производительность дозирования”");
    data.put(11, "Резерв");
    data.put(12, "Резерв");
  }

  public static String getDescription(int eventId) {
    return data.get(eventId);
  }
}
