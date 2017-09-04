package ru.absaliks.logit;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfigFrame {
  private static final Logger LOG = Logger.getLogger(ConfigFrame.class.getName());

  public static void open() {
    try {
      Parent root = new FXMLLoader().load(Utils.getInputStream("ConfigFrame.fxml"));
      Stage primaryStage = new Stage();
      primaryStage.setTitle("Настройки");
      primaryStage.initModality(Modality.APPLICATION_MODAL);
      primaryStage.setScene(new Scene(root));
      primaryStage.showAndWait();
    } catch (Exception e) {
      LOG.log(Level.SEVERE, "Unable to open the configuration window", e);
    }
  }
}
