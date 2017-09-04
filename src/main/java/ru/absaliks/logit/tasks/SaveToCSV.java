package ru.absaliks.logit.tasks;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.concurrent.Callable;
import ru.absaliks.logit.config.Config;

public abstract class SaveToCSV<T> implements Callable {
  private static final String LINE_SEPARATOR = System.lineSeparator();

  private final List<T> data;
  private final File file;
  private String divisor;

  public SaveToCSV(File file, List<T> data) {
    this.file = file;
    if (isNull(data) || data.size() == 0)
      throw new IllegalArgumentException("Nothing to write");

    this.data = data;

    setDivisor();
  }

  private void setDivisor() {
    divisor = Config.getInstance().csvDivisor;
    if (isEmpty(divisor)) {
      divisor = ";";
    } else
      switch (divisor) {
        case "tab": divisor = "\t"; break;
        case ",":
        case ";": break;
        default: divisor = ";";
    }
  }

  public String getDivisor() {
    return divisor;
  }


  public Void call() {
    try(FileWriter writer = new FileWriter(file)) {
      for (int i = 0; i < data.size(); i++) {
        write(writer, data.get(i));
        writer.write(LINE_SEPARATOR);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  protected abstract void write(Writer writer, T object) throws IOException;
}
