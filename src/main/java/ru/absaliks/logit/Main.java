package ru.absaliks.logit;

import com.airhacks.afterburner.injection.Injector;
import java.io.FileNotFoundException;
import javafx.application.Application;
import javax.xml.bind.JAXBException;
import lombok.extern.log4j.Log4j2;
import ru.absaliks.logit.config.Config;
import ru.absaliks.logit.config.ConfigSerializer;

@Log4j2
public class Main {

  public static void main(String[] args) {
    log.debug("Debug level is enabled");
    Config config = getConfigurationFromFile();
    Injector.setModelOrService(Config.class, config);

    Application.launch(MainFrame.class);

    ConfigSerializer.toXml(config);
    System.exit(0);
  }

  private static Config getConfigurationFromFile() {
    try {
      return ConfigSerializer.fromFile(false);
    } catch (FileNotFoundException e) {
      log.warn("Не найден конфигурационный файл " + e.getMessage(), e);
      return new Config();
    } catch (JAXBException e) {
      log.warn("Ошибка чтения конфигурационного файла: " + e.getMessage(), e);
      throw new RuntimeException(e);
    }
  }
}
