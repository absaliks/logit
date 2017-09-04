package ru.absaliks.logit;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LoggingFormatter extends Formatter {
  private static final String DEFAULT_DATE_FORMAT = "h:mm:ss";
  private static final String LINE_SEPARATOR = System.lineSeparator();
  private static final Date DATE = new Date();
  private DateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);

  public String format(LogRecord rec) {
    DATE.setTime(rec.getMillis());
    String output = format.format(DATE) + " " +
        rec.getLevel() + " " + getSimpleClassName(rec.getSourceClassName()) +
        "." + rec.getSourceMethodName() + "() - " + rec.getMessage() +
        LINE_SEPARATOR;

    if (rec.getThrown() != null)
      rec.getThrown().printStackTrace();

    return output;
  }

  private String getSimpleClassName(String longClassName) {
    return longClassName.substring(longClassName.lastIndexOf('.') + 1);
  }
}
