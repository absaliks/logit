package ru.absaliks.logit.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
  public static final DateTimeFormatter FILENAME_DATETIME_FORMAT =
      DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
  public static final DateTimeFormatter RUSSIAN_DATETIME_FORMAT =
      DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
  private static final int SECONDS_PER_DAY = 60 * 60 * 24;

  public static LocalDateTime of80Year(long second) {
    long days = Math.floorDiv(second, SECONDS_PER_DAY);
    int secondsOfDay = (int)Math.floorMod(second, SECONDS_PER_DAY);
    LocalDate date = LocalDate.of(1980, 1, 1).plusDays(days);
    LocalTime time = LocalTime.ofSecondOfDay(secondsOfDay);
    return LocalDateTime.of(date, time);
  }

  public static String parseDateTime(LocalDateTime dateTime) {
    return dateTime.format(RUSSIAN_DATETIME_FORMAT);
  }
}
