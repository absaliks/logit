package ru.absaliks.logit.service;

import static ru.absaliks.logit.view.ViewUtils.showError;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import ru.absaliks.logit.model.ArchiveEntry;
import ru.absaliks.logit.model.JournalEntry;
import ru.absaliks.logit.model.ServicePage;
import ru.absaliks.logit.tasks.SaveArchiveToCSV;
import ru.absaliks.logit.tasks.SaveJournalToCSV;

public class Service {

  private static final Logger LOG = Logger.getLogger(Service.class.getName());

  private ExecutorService executor = Executors.newSingleThreadExecutor();

  private final ModbusService modbusService;

  private ObjectProperty<ServicePage> servicePageProperty;
  private ObjectProperty<List<JournalEntry>> journalProperty;
  private ObjectProperty<List<ArchiveEntry>> archiveProperty;
  private BooleanProperty journalInProcessProperty = new SimpleBooleanProperty();
  private BooleanProperty archiveInProcessProperty = new SimpleBooleanProperty();

  private Function<Throwable, Void> exceptionHandler = throwable -> {
    LOG.log(Level.SEVERE, throwable.getMessage(), throwable);
    showError(throwable.getMessage());
    journalInProcessProperty.setValue(false);
    return null;
  };

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

  public void readServicePage() {
    journalInProcessProperty.setValue(true);
    CompletableFuture
        .supplyAsync(() -> modbusService.readServicePage(), executor)
        .thenAccept(servicePage -> updateServicePageProperty(servicePage))
        .exceptionally(exceptionHandler);
  }

  public void readJournal() {
    journalInProcessProperty.setValue(true);
    CompletableFuture
        .supplyAsync(() -> modbusService.readJournal(), executor)
        .thenAccept(journal -> updateJournalProperty(journal))
        .exceptionally(exceptionHandler);
  }

  public void readArchive() {
    journalInProcessProperty.setValue(true);
    CompletableFuture
        .supplyAsync(() -> modbusService.readArchive(), executor)
        .thenAccept(archive -> updateArchiveProperty(archive))
        .exceptionally(exceptionHandler);
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
    CompletableFuture
        .supplyAsync(new SaveJournalToCSV(file, journal)::call)
        .thenAccept((n) -> setJournalInProcessProperty(false))
        .exceptionally(exceptionHandler);
  }

  private void setJournalInProcessProperty(boolean value) {
    journalInProcessProperty.setValue(value);
  }

  public void saveArchive(File file, List<ArchiveEntry> journal) {
    journalInProcessProperty.setValue(true);
    CompletableFuture
        .supplyAsync(new SaveArchiveToCSV(file, journal)::call)
        .thenAccept((n) -> setArchiveInProcessProperty(false))
        .exceptionally(exceptionHandler);
  }

  private void setArchiveInProcessProperty(boolean value) {
    archiveInProcessProperty.setValue(value);
    journalInProcessProperty.setValue(false); // TODO
  }
}
