package ru.absaliks.logit.view;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ViewUtils {
  public static void showInfo(String text) {
    Alert alert = new Alert(AlertType.INFORMATION, text);
    alert.setTitle("");
    alert.setHeaderText(null);
    alert.showAndWait();
  }

  public static void showError(String text) {
    Alert alert = new Alert(AlertType.ERROR, text);
    alert.setTitle("Ошибка");
    alert.setHeaderText(null);
    alert.showAndWait();
  }
}
