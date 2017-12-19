package ru.absaliks.logit.service;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.ghgande.j2mod.modbus.facade.AbstractModbusMaster;
import com.ghgande.j2mod.modbus.net.SerialConnection;
import java.util.List;
import java.util.Set;
import javafx.beans.property.DoubleProperty;
import javax.inject.Inject;
import lombok.extern.log4j.Log4j2;
import ru.absaliks.logit.ModbusMasterBuilder;
import ru.absaliks.logit.config.Config;
import ru.absaliks.logit.model.ArchiveEntry;
import ru.absaliks.logit.model.JournalEntry;
import ru.absaliks.logit.model.ServicePage;
import ru.absaliks.logit.tasks.ReadArchiveTask;
import ru.absaliks.logit.tasks.ReadJournalTask;
import ru.absaliks.logit.tasks.ReadPagesTask;
import ru.absaliks.logit.tasks.ReadServicePageTask;

@Log4j2
public class ModbusService {

  @Inject
  private Config config;

  private ServicePage servicePage;

  private AbstractModbusMaster master;

  public void checkConnection() throws Exception {
    try {
      openConnection();
    } finally {
      disconnect();
    }
  }

  private void openConnection() throws Exception {
    log.debug("Opening connection");
    AbstractModbusMaster master = ModbusMasterBuilder.createMaster();
    master.connect();
  }

  private void disconnect() {
    log.info("Closing connection");
    if (nonNull(master)) {
      master.disconnect();
    }
  }

  public ServicePage readServicePage() throws Exception {
    try {
      openConnection();
      return servicePage = getServicePage(true);
    } finally {
      disconnect();
    }
  }

  private ServicePage getServicePage(boolean forceUpdate) throws Exception {
    if (forceUpdate || isNull(servicePage)) {
      log.info("Reading Service Page");
      return servicePage = new ReadServicePageTask(master, getSlaveId()).call();
    } else {
      log.info("Returning cached Service Page");
      return servicePage;
    }
  }

  public List<JournalEntry> readJournal(DoubleProperty progress) throws Exception {
    log.info("Reading Journal");
    try {
      openConnection();
      return createReadJournalTask(progress).call();
    } finally {
      disconnect();
    }
  }

  public List<ArchiveEntry> readArchive(DoubleProperty progress) throws Exception {
    log.info("Reading Archive");
    try {
      openConnection();
      return createReadArchiveTask(progress).call();
    } finally {
      disconnect();
    }
  }

  private ReadJournalTask createReadJournalTask(DoubleProperty progress) {
    ReadJournalTask task = new ReadJournalTask();
    task.setPagesCount(servicePage.journalPageCount);
    task.setUint32ByteOrder(config.uint32ByteOrder);
    fillInReadPagesTaskProperties(task, progress);
    return task;
  }

  private ReadArchiveTask createReadArchiveTask(DoubleProperty progress) {
    ReadArchiveTask task = new ReadArchiveTask();
    task.setPagesCount(servicePage.archivePageCount);
    task.setUint32ByteOrder(config.uint32ByteOrder);
    task.setReal32ByteOrder(config.real32ByteOrder);
    fillInReadPagesTaskProperties(task, progress);
    return task;
  }

  private void fillInReadPagesTaskProperties(ReadPagesTask task, DoubleProperty progress) {
    task.setModbusMaster(master);
    task.setSlaveId(getSlaveId());
    task.setDelayBetweenRequests(config.modbus.delayBetweenRequests);
    task.setProgress(progress);
  }

  private int getSlaveId() {
    return config.modbus.deviceId == 0 ? 1 : config.modbus.deviceId;
  }

  public Set<String> getRegisteredCOMPorts() {
    return new SerialConnection().getCommPorts();
  }
}
