package ru.absaliks.logit;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Utils {
  public static InputStream getInputStream(String resource) throws FileNotFoundException {
    InputStream is = Utils.class.getClassLoader().getResourceAsStream(resource);
    if (is == null)
      throw new FileNotFoundException(resource);
    return is;
  }
}
