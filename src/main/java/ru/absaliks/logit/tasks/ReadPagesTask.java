package ru.absaliks.logit.tasks;

import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.facade.AbstractModbusMaster;
import com.ghgande.j2mod.modbus.procimg.InputRegister;
import com.ghgande.j2mod.modbus.procimg.SimpleInputRegister;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.absaliks.logit.config.Config;

public abstract class ReadPagesTask<T> implements Callable<List<T>> {
  private static final Logger LOG = Logger.getLogger(ReadPagesTask.class.getName());

  public static final int PAGE_READY_REF = 10;
  public static final int WAIT_FOR_PAGE_TIMEOUT = 3000;

  private final AbstractModbusMaster modbusMaster;
  private final int slaveId;
  private final int pagesCount;
  private final int delayBetweenRequests;

  private final List<T> pages;

  public ReadPagesTask(AbstractModbusMaster modbusMaster, int slaveId, int pagesCount) {
    this.modbusMaster = modbusMaster;
    this.pagesCount = pagesCount;
    this.slaveId = slaveId;
    this.pages = new ArrayList<>(pagesCount);
    this.delayBetweenRequests = Config.getInstance().modbus.delayBetweenRequests;

    LOG.config(this::toString);
  }

  public List<T> call() throws InterruptedException, ModbusException {
    for (int pageId = 0; pageId < pagesCount; pageId++) {
      writeJournalPageNumber(slaveId, pageId);
      Thread.sleep(delayBetweenRequests);
      waitForPageReadiness(slaveId);
      T page = createPage(readPageData());
      pages.add(page);

      if (LOG.isLoggable(Level.CONFIG)) {
        LOG.config(String.format("Page #%d: %s", pageId, page));
      }
    }
    return pages;
  }

  private void writeJournalPageNumber(int slaveId, int page) throws ModbusException {
    try {
      modbusMaster.writeSingleRegister(slaveId, getCurrentPageRef(), new SimpleInputRegister(page));
    } catch (ModbusException e) {
      LOG.log(Level.WARNING, "Unable to write page number to read", e);
      throw e;
    }
  }

  private void waitForPageReadiness(int slaveId) throws InterruptedException {
    boolean timedout = false;
    boolean isReady = false;
    long startTime = System.currentTimeMillis();
    while (!((timedout = isTimeoutReached(startTime)) || (isReady = isPageReady(slaveId)))) {
      Thread.sleep(delayBetweenRequests);
    }
    if (LOG.isLoggable(Level.CONFIG)) {
      LOG.config("timedout=" + timedout + ", ready=" + isReady);
    }
  }

  private boolean isTimeoutReached(long startTime) {
    return System.currentTimeMillis() - startTime > WAIT_FOR_PAGE_TIMEOUT;
  }

  private boolean isPageReady(int slaveId) {
    try {
      InputRegister[] registers = modbusMaster.readInputRegisters(slaveId, PAGE_READY_REF, 1);
      return registers[0].getValue() != 0;
    } catch (Exception e) {
      LOG.log(Level.INFO, "Unable to read page status", e);
      return false;
    }
  }

  private InputRegister[] readPageData() throws ModbusException {
    return modbusMaster.readInputRegisters(slaveId, getPageRef(), getPageLength());
  }

  protected abstract int getCurrentPageRef();

  protected abstract int getPageRef();

  protected abstract int getPageLength();

  protected abstract T createPage(InputRegister[] registers);

  @Override
  public String toString() {
    return this.getClass().getSimpleName() +
        "{slaveId=" + slaveId +
        ", pagesCount=" + pagesCount +
        ", pages=" + pages +
        ", currentPageRef=" + getCurrentPageRef() +
        ", pageRef=" + getPageRef() +
        ", pageLength=" + getPageLength() +
        ", delayBetweenRequests=" + delayBetweenRequests +
        '}';
  }
}
