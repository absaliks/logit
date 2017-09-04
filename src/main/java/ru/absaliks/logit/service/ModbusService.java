package ru.absaliks.logit.service;

import static java.util.Objects.isNull;

import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.facade.AbstractModbusMaster;
import com.ghgande.j2mod.modbus.net.SerialConnection;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.absaliks.logit.ModbusMasterBuilder;
import ru.absaliks.logit.config.Config;
import ru.absaliks.logit.model.ArchiveEntry;
import ru.absaliks.logit.model.JournalEntry;
import ru.absaliks.logit.model.ServicePage;
import ru.absaliks.logit.tasks.ReadArchiveTask;
import ru.absaliks.logit.tasks.ReadJournalTask;
import ru.absaliks.logit.tasks.ReadServicePageTask;

public class ModbusService {

  private static final Logger LOG = Logger.getLogger(ModbusService.class.getName());

  private static ModbusService instance;
  private ServicePage servicePage;
  private AbstractModbusMaster modbusMaster;

  private ModbusService() {
  }

  public static ModbusService getInstance() {
    if (instance == null) {
      instance = new ModbusService();
    }
    return instance;
  }

  public void checkConnection() {
    AbstractModbusMaster master = null;
    try {
      master = getConnection();
    } finally {
      if (master != null) {
        master.disconnect();
      }
    }
  }

  public ServicePage getServicePage() {
    return servicePage;
  }

  public ServicePage readServicePage() {
    try {
      LOG.log(Level.INFO, "Reading the service page");
      this.servicePage = new ReadServicePageTask(getConnection(), getSlaveId()).call();
      return this.servicePage;
    } catch (ModbusException e) {
      LOG.log(Level.SEVERE, "Unable to read the service page", e);
      throw new RuntimeException(e.getMessage(), e);
    } finally {
      disconnect();
    }
  }

  private void disconnect() {
    LOG.log(Level.INFO, "Closing connection");
    if (modbusMaster != null)
      modbusMaster.disconnect();
  }

  public List<JournalEntry> readJournal() {
    checkServicePage();
    try {
      LOG.log(Level.INFO, "Reading the journal");
      return new ReadJournalTask(getConnection(), getSlaveId(), servicePage.journalPageCount)
          .call();
    } catch (ModbusException e) {
      LOG.log(Level.SEVERE, "Unable to read the journal", e);
      throw new RuntimeException(e.getMessage(), e);
    } catch (InterruptedException e) {
      LOG.log(Level.INFO, "Read journal operation has been interrupted", e);
      return null;
    } finally {
      disconnect();
    }
  }

  private void checkServicePage() {
    if (isNull(servicePage)) {
      LOG.info("ServicePage is null, going to read ServicePage first");
      readServicePage();
    }
  }

  public List<ArchiveEntry> readArchive() {
    checkServicePage();
    try {
      return new ReadArchiveTask(getConnection(), getSlaveId(), servicePage.archivePageCount)
          .call();
    } catch (ModbusException e) {
      LOG.log(Level.SEVERE, "Unable to read the archive", e);
      throw new RuntimeException(e.getMessage(), e);
    } catch (InterruptedException e) {
      LOG.log(Level.INFO, "Read archive operation has been interrupted", e);
      return null;
    } finally {
      disconnect();
    }
  }

  private int getSlaveId() {
    return Config.getInstance().modbus.deviceId == 0 ? 1 : Config.getInstance().modbus.deviceId;
  }

  private AbstractModbusMaster getConnection() {
    AbstractModbusMaster master = ModbusMasterBuilder.createMaster();
    try {
      master.connect();
      modbusMaster = master;
    } catch (Exception e) {
      LOG.log(Level.SEVERE, "Cannot connect to slave", e);
      /*
       - Port %s is not a valid name for a port on this platform
       - Port [%s] cannot be opened or does not exist - Valid ports are: [%s]
      */
      throw new RuntimeException("Не удалось подключиться к устройству: " + e.getMessage());
    }
    return master;
  }

  public Set<String> getRegisteredCOMPorts() {
    return new SerialConnection().getCommPorts();
  }
}
