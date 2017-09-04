package ru.absaliks.logit;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import org.apache.commons.lang3.ArrayUtils;
import ru.absaliks.logit.config.Config;
import ru.absaliks.logit.config.ConfigBuilder;

public class Main {
  private static final Logger LOG = Logger.getLogger(Main.class.getName());

  public static void main(String[] args) {
    setupLogging(args);
    Config.getInstance().init();
    Application.launch(MainFrame.class);
    ConfigBuilder.toXml();
    System.exit(0);
  }

  private static void setupLogging(String[] args) {
    setUpCustomLoggingFormatter();
    if (ArrayUtils.contains(args, "debug")) {
      Logger.getLogger("").setLevel(Level.CONFIG);
      getRootConsoleHandler().setLevel(Level.CONFIG);
      LOG.config("Debug level enabled");
    }
  }

  private static void setUpCustomLoggingFormatter() {
    try {
      getRootConsoleHandler().setFormatter(new LoggingFormatter());
    } catch (Exception e) {
      LOG.log(Level.WARNING, "Unable to setup custom logging", e);
    }
  }

  private static Handler getRootConsoleHandler() {
    for (Handler handler : Logger.getLogger("").getHandlers()) {
      if (handler instanceof ConsoleHandler) {
        return handler;
      }
    }
    throw new IllegalStateException("I expected to find here ConsoleHandler"
        + " for global logger. Sorry, but you are doomed to suffer from watching"
        + " standard java two-line logging messages, unless you override global"
        + " java logging.properties");
  }
}
