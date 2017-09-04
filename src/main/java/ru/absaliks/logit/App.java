package ru.absaliks.logit;

import static ru.absaliks.logit.common.DateUtils.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalUnit;
import java.util.logging.Logger;

public class App {
  private static final Logger LOG = Logger.getLogger(App.class.getName());

  public static void main(String[] args) throws CloneNotSupportedException {
    LocalDateTime dt0 = LocalDateTime.of(LocalDate.of(1980, 1, 1),
        LocalTime.MIN);
    System.out.println(dt0);

    LocalDateTime dt1 = of80Year(536936447);
    System.out.println(dt1.format(RUSSIAN_DATETIME_FORMAT));

    LocalDateTime dt2 = of80Year(536936400);
    System.out.println(dt2.format(RUSSIAN_DATETIME_FORMAT));

    System.out.println(Duration.between(dt0, dt1).toMinutes() * 60);
    System.out.println(parseDateTime(of80Year(1188174755)));
  }
}
