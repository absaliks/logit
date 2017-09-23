package ru.absaliks.logit;

import javafx.application.Application;
import lombok.extern.log4j.Log4j2;
import ru.absaliks.logit.config.Config;
import ru.absaliks.logit.config.ConfigBuilder;

@Log4j2
public class Main {

  public static void main(String[] args) {
    log.debug("Debug level is enabled");
    Config.init();
    Application.launch(MainFrame.class);
    ConfigBuilder.toXml();
    System.exit(0);
  }
}
