package ru.absaliks.logit.config;

import java.io.File;
import java.io.FileNotFoundException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ConfigBuilder {
  private static final String DEFAULT_CONFIG_PATH = System.getProperty("user.dir") + "\\config.xml";

  public static void toXml() {
    toXml(new File(DEFAULT_CONFIG_PATH));
  }

  private static void toXml(File file) {
    if (file == null) {
      throw new IllegalArgumentException();
    }

    Config config = Config.getInstance();

    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(Config.class);
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      jaxbMarshaller.marshal(config, file);
      jaxbMarshaller.marshal(config, System.out);
    } catch (JAXBException e) {
      log.warn("Unable to save configuration to " + file.getAbsolutePath(), e);
    }
  }

  public static Config fromFile() throws FileNotFoundException, JAXBException {
    return fromFile(new File(DEFAULT_CONFIG_PATH));
  }

  private static Config fromFile(File file) throws FileNotFoundException, JAXBException {
    if (!file.exists()) {
      throw new FileNotFoundException(file.getAbsolutePath());
    }

    JAXBContext context = JAXBContext.newInstance(Config.class);
    Unmarshaller unmarshaller = context.createUnmarshaller();
    return (Config) unmarshaller.unmarshal(file);
  }
}
