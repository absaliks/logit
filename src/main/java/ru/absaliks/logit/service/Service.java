package ru.absaliks.logit.service;

import static ru.absaliks.logit.view.ViewUtils.showError;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.extern.log4j.Log4j2;
import ru.absaliks.logit.common.OSUtils;
import ru.absaliks.logit.config.Config;
import ru.absaliks.logit.model.ArchiveEntry;
import ru.absaliks.logit.model.JournalEntry;
import ru.absaliks.logit.model.ServicePage;
import ru.absaliks.logit.tasks.SaveArchiveToCSV;
import ru.absaliks.logit.tasks.SaveJournalToCSV;

@Log4j2
public class Service {

  private ExecutorService executor = Executors.newSingleThreadExecutor();

  private final ModbusService modbusService;

  private ObjectProperty<ServicePage> servicePageProperty;
  private ObjectProperty<List<JournalEntry>> journalProperty;
  private ObjectProperty<List<ArchiveEntry>> archiveProperty;
  private BooleanProperty journalInProcessProperty = new SimpleBooleanProperty();
  private BooleanProperty archiveInProcessProperty = new SimpleBooleanProperty();
  private DoubleProperty progress = new SimpleDoubleProperty();
  private Future<?> future;

  public Service(ModbusService modbusService) {
    this.modbusService = modbusService;
    servicePageProperty = new SimpleObjectProperty<>();
    journalProperty = new SimpleObjectProperty<>();
    archiveProperty = new SimpleObjectProperty<>();
  }

  public ObjectProperty<ServicePage> getServicePageProperty() {
    return servicePageProperty;
  }

  public ObjectProperty<List<JournalEntry>> getJournalProperty() {
    return journalProperty;
  }

  public ObjectProperty<List<ArchiveEntry>> getArchiveProperty() {
    return archiveProperty;
  }

  public BooleanProperty journalInProcessProperty() {
    return journalInProcessProperty;
  }

  public BooleanProperty archiveInProcessProperty() {
    return archiveInProcessProperty;
  }

  public DoubleProperty getProgressProperty() {
    return progress;
  }

  public void readServicePage() {
    journalInProcessProperty.setValue(true);
    future = executor.submit(() -> {
      try {
        ServicePage servicePage = modbusService.readServicePage();
        updateServicePageProperty(servicePage);
      } catch (Exception e) {
        handleException(e);
      }
      future = null;
    });
  }

  public void readJournal() {
    journalInProcessProperty.setValue(true);
    future = executor.submit(() -> {
      try {
        List<JournalEntry> journal = modbusService.readJournal(progress);
        updateJournalProperty(journal);
      } catch (Exception e) {
        handleException(e);
      }
      future = null;
    });
  }

  public void readArchive() {
    journalInProcessProperty.setValue(true);
    future = executor.submit(() -> {
      try {
        List<ArchiveEntry> archive = modbusService.readArchive(progress);
        updateArchiveProperty(archive);
      } catch (Exception e) {
        handleException(e);
      }
      future = null;
    });
  }

  private void updateServicePageProperty(ServicePage servicePage) {
    servicePageProperty.setValue(servicePage);
    journalInProcessProperty.setValue(false);
  }

  private void updateJournalProperty(List<JournalEntry> journal) {
    journalProperty.setValue(journal);
    journalInProcessProperty.setValue(false);
  }

  private void updateArchiveProperty(List<ArchiveEntry> archive) {
    archiveProperty.setValue(archive);
    journalInProcessProperty.setValue(false);
  }

  public void saveJournal(File file, List<JournalEntry> journal) {
    journalInProcessProperty.setValue(true);
    future = executor.submit(() -> {
      try {
        new SaveJournalToCSV(file, journal).call();
        setJournalInProcessProperty(false);
        openFile(file);
      } catch (Exception e) {
        handleException(e);
      }
      future = null;
    });
  }

  private void setJournalInProcessProperty(boolean value) {
    journalInProcessProperty.setValue(value);
  }

  public void saveArchive(File file, List<ArchiveEntry> journal) {
    journalInProcessProperty.setValue(true);
    future = executor.submit(() -> {
      try {
        new SaveArchiveToCSV(file, journal).call();
        setArchiveInProcessProperty(false);
        openFile(file);
      } catch (Exception e) {
        handleException(e);
      }
      future = null;
    });
  }

  public void cancel() {
    if (future != null) {
      log.info("Cancelling a task");
      future.cancel(true);
    }
  }

  private void setArchiveInProcessProperty(boolean value) {
    archiveInProcessProperty.setValue(value);
    journalInProcessProperty.setValue(false); // TODO
  }

  private void openFile(File file) throws IOException {
    if (Config.getInstance().openFileAfterSaving) {
      log.debug("Opening a file " + file.getAbsolutePath());
      OSUtils.openFile(file);
    }
  }

  private void handleException(Throwable throwable) {
    future = null;
    log.error(throwable);
    journalInProcessProperty.setValue(false);
    progress.setValue(0);
    Platform.runLater(() -> showError(throwable.getMessage()));
  }
}
