package ru.absaliks.logit.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

public class ResourceUtils {

  private static final Logger LOG = Logger.getLogger(ResourceUtils.class.getName());
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
      LOG.log(Level.WARNING, "Unable to find file " + FRAME_ICON_FILENAME, e);
    }
    return null;
  }
}
