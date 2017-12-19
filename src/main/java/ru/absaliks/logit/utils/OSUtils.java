package ru.absaliks.logit.utils;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

public class OSUtils {

  private static final ExtensionFilter[] CSV_EXTENSION = {
      new ExtensionFilter("CSV файлы", "*.csv"),
      new ExtensionFilter("Все файлы", "*.*")};

  public static File saveInto(String filename, Window stage) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Сохранть CSV файл");
    fileChooser.setInitialFileName(filename);
    fileChooser.getExtensionFilters().addAll(CSV_EXTENSION);
    return fileChooser.showSaveDialog(stage);
  }

  public static void openFile(File file) throws IOException {
    Desktop.getDesktop().open(file);
  }
}
