package ru.absaliks.logit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.absaliks.logit.service.ModbusService;
import ru.absaliks.logit.service.Service;
import ru.absaliks.logit.view.MainFrameController;
import ru.absaliks.logit.view.MainFrameModel;

public class MainFrame extends Application {
  @Override
  public void start(Stage primaryStage) throws Exception {
    initStage(primaryStage);
    primaryStage.show();
  }
  
  private void initStage(Stage primaryStage) {
    // final MainFrameModel model = new MainFrameModel();
    final ModbusService modbusService = ModbusService.getInstance();
    final Service service = new Service(modbusService);

    FXMLLoader loader = new FXMLLoader();
    try {
      loader.setControllerFactory((c) -> new MainFrameController(service, modbusService, primaryStage));
      Parent root = loader.load(Utils.getInputStream("MainFrame.fxml"));
      primaryStage.setTitle("Modbus Browser");
      primaryStage.setScene(new Scene(root));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
