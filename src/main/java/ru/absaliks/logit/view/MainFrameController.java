package ru.absaliks.logit.view;

import static ru.absaliks.logit.view.ViewUtils.showError;
import static ru.absaliks.logit.view.ViewUtils.showInfo;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;
import ru.absaliks.logit.ConfigFrame;
import ru.absaliks.logit.common.DateUtils;
import ru.absaliks.logit.common.OSUtils;
import ru.absaliks.logit.config.Config;
import ru.absaliks.logit.model.ArchiveEntry;
import ru.absaliks.logit.model.JournalEntry;
import ru.absaliks.logit.model.ServicePage;
import ru.absaliks.logit.service.ModbusService;
import ru.absaliks.logit.service.Service;

@Log4j2
public class MainFrameController {

  private final Service service;
  private final ModbusService modbusService;
  private final Stage stage;

  @FXML
  private ProgressIndicator servicePageProgressIndicator;
  @FXML
  private ProgressIndicator readJournalProgressIndicator;
  @FXML
  private ProgressIndicator progressIndicator;
  @FXML
  private ProgressBar progressBar;
  @FXML
  private Label portStatusLabel;
  @FXML
  private Label servicePageStatusLabel;
  @FXML
  private Label readingDataStatusLabel;
  @FXML
  private CheckBox openFileAfterSaving;
  @FXML
  private Label statusLabel;
  @FXML
  private ListView<String> listView;
  @FXML
  private Button readServicePageButton;
  @FXML
  private Button readJournalButton;
  @FXML
  private Button readArchiveButton;
  @FXML
  private Button cancel;

  public MainFrameController(Service service, ModbusService modbusService, Stage stage) {
    this.service = service;
    this.modbusService = modbusService;
    this.stage = stage;
  }

  @FXML
  private void initialize() {

    try {
      updateStatusLabel();
    } catch (Exception e) {
      log.warn("Frame initialization error", e);
    }
    service.getServicePageProperty().addListener((observable, oldValue, newValue) ->
        populateListView(newValue));
    service.getJournalProperty().addListener((observable, oldValue, newValue) ->
        saveJournal(newValue));
    service.getArchiveProperty().addListener((observable, oldValue, newValue) ->
        saveArchive(newValue));
    service.journalInProcessProperty().addListener((observable, oldValue, newValue) ->
        setProgressIndicatorVisibility(newValue));
    progressBar.progressProperty().bind(service.getProgressProperty());

    openFileAfterSaving.selectedProperty().setValue(Config.getInstance().openFileAfterSaving);
    openFileAfterSaving.selectedProperty().addListener((observable, oldValue, newValue) ->
        Config.getInstance().openFileAfterSaving = newValue);
  }

  private void setProgressIndicatorVisibility(Boolean newValue) {
    progressIndicator.setVisible(newValue);
    cancel.setDisable(!newValue);
    readServicePageButton.setDisable(newValue);
    readJournalButton.setDisable(newValue);
    readArchiveButton.setDisable(newValue);
  }

  public void checkConnection() {
    try {
      modbusService.checkConnection();
      showInfo("Соединение успешно установлено");
    } catch (Exception e) {
      log.error("Couldn't open a connection. " + Config.getInstance(), e);
      showError("Не удалось установить соединение: " + e.getMessage());
    }
  }

  public void readServicePage() {
    try {
      service.readServicePage();
    } catch (Exception e) {
      log.error("Unable to read the service page. " + Config.getInstance(), e);
      showError("Не удалось прочесть сервисную страницу: " + e.getMessage());
    }
  }

  private void populateListView(ServicePage servicePage) {
    Platform.runLater(() -> {
      ObservableList<String> items = listView.getItems();
      items.clear();
      items.addAll("Маркер сервисной страницы = " + servicePage.servicePageRefId,
          "Модель контроллера = " + servicePage.controllerModel,
          "Серийный номер = " + servicePage.serialNo,
          "Начальный адрес журнала = " + servicePage.journalRefId,
          "Количество страниц журнала = " + servicePage.journalPageCount,
          "Начальный адрес архива = " + servicePage.archiveRefId,
          "Количество страниц архива = " + servicePage.archivePageCount,
          "Текущая страница журнала = " + servicePage.journalPageNo,
          "Текущая страница архива = " + servicePage.archivePageNo,
          "Маркер готовности страницы для чтения = " + servicePage.pageIsReadyFlag
      );
    });
  }

  public void readJournal() {
    service.getProgressProperty().setValue(0);
    try {
      service.readJournal();
    } catch (Exception e) {
      log.error("Не удалось прочесть журнал", e);
    }
  }

  public void readArchive() {
    service.getProgressProperty().setValue(0);
    try {
      service.readArchive();
    } catch (Exception e) {
      log.error("Не удалось прочесть архив", e);
    }
  }

  public void openConfigurationWindow() {
    ConfigFrame.open();
    updateStatusLabel();
  }

  public void cancelTask() {
    service.cancel();
  }

  private void updateStatusLabel() {
    Config config = Config.getInstance();
    String statusText = null;
    try {
      switch (config.currentInterface) {
        case COM:
          statusText = String.format("%s   %d-%s-%s", config.serial.portName,
              config.serial.baudRate, config.serial.parity, config.serial.stopBits);
          break;
        case TCP:
          statusText = String.format("TCP   %s:%d", config.tcp.address, config.tcp.port);
      }
    } finally {
      statusLabel.setText(statusText);
    }
  }

  private void saveJournal(List<JournalEntry> journal) {
    Platform.runLater(() -> {
      File file = OSUtils.saveInto(generateFileName("_journal"), stage);
      if (file != null) {
        service.saveJournal(file, journal);
      }
      service.getProgressProperty().setValue(0);
    });
  }

  private void saveArchive(List<ArchiveEntry> archive) {
    Platform.runLater(() -> {
      File file = OSUtils.saveInto(generateFileName("_archive"), stage);
      if (file != null) {
        service.saveArchive(file, archive);
      }
      service.getProgressProperty().setValue(0);
    });
  }

  private String generateFileName(String postfix) {
    int serialNo = modbusService.getServicePage().serialNo;
    return serialNo + "_" + LocalDateTime.now().format(DateUtils.FILENAME_DATETIME_FORMAT) +
        postfix + ".csv";
  }
}
