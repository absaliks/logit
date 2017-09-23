package ru.absaliks.logit;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;
import ru.absaliks.logit.common.ResourceUtils;

@Log4j2
public class ConfigFrame {

  public static void open() {
    try {
      Parent root = new FXMLLoader().load(ResourceUtils.getInputStream("ConfigFrame.fxml"));
      Stage primaryStage = new Stage();
      primaryStage.setTitle("Настройки");
      primaryStage.initModality(Modality.APPLICATION_MODAL);
      primaryStage.setScene(new Scene(root));
      primaryStage.showAndWait();
    } catch (Exception e) {
      log.error("Unable to open the configuration window", e);
    }
  }
}
