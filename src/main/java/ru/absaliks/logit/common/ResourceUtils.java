package ru.absaliks.logit.common;

import java.io.FileNotFoundException;
import java.io.InputStream;
import javafx.scene.image.Image;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ResourceUtils {
  private static final String FRAME_ICON_FILENAME = "graphit-32.png";
  private static Image frameIcon;
  private static boolean frameIconError;

  public static InputStream getInputStream(String resource) throws FileNotFoundException {
    InputStream is = ResourceUtils.class.getClassLoader().getResourceAsStream(resource);
    if (is == null)
      throw new FileNotFoundException(resource);
    return is;
  }

  public static Image getFrameIcon() {
    try {
      return new Image(getInputStream(FRAME_ICON_FILENAME));
    } catch (FileNotFoundException e) {
      log.warn("Unable to find file " + FRAME_ICON_FILENAME, e);
    }
    return null;
  }
}
