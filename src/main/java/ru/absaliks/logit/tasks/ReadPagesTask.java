package ru.absaliks.logit.tasks;

import static java.util.Objects.isNull;

import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.facade.AbstractModbusMaster;
import com.ghgande.j2mod.modbus.procimg.InputRegister;
import com.ghgande.j2mod.modbus.procimg.SimpleInputRegister;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;
import javafx.beans.property.DoubleProperty;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ArrayUtils;
import ru.absaliks.logit.utils.ByteUtils;

@Log4j2
@Data
public abstract class ReadPagesTask<T> implements Callable<List<T>> {

  public static final int PAGE_READY_REF = 10;
  public static final int WAIT_FOR_PAGE_TIMEOUT = 3000;

  private AbstractModbusMaster modbusMaster;
  private int slaveId;
  private int pagesCount;
  private int delayBetweenRequests = -1;
  private DoubleProperty progress;

  public List<T> call() throws InterruptedException, ModbusException, TimeoutException {
    if (!isValid())
      throw new IllegalStateException(this.toString());

    log.debug(this::toString);

    List<T> pages = new ArrayList<>(pagesCount);
    float progressPerPage = 1f / pagesCount;
    for (int pageId = 0; pageId < pagesCount; pageId++) {
      writeJournalPageNumber(slaveId, pageId);
      waitForPageReadiness(slaveId);
      T page = createPage(readPageData());
      pages.add(page);

      progress.setValue(progressPerPage * (pageId + 1));
      log.debug("Page {}: {}", pageId, page);
    }
    return pages;
  }

  public boolean isValid() {
    return isNull(modbusMaster) || pagesCount < 1 || slaveId < 1 || isNull(progress)
        || delayBetweenRequests < 0;
  }

  private void writeJournalPageNumber(int slaveId, int page) throws ModbusException {
    try {
      modbusMaster.writeSingleRegister(slaveId, getCurrentPageRef(), new SimpleInputRegister(page));
    } catch (ModbusException e) {
      log.warn("Unable to write page number to read", e);
      throw e;
    }
  }

  private void waitForPageReadiness(int slaveId) throws InterruptedException, TimeoutException {
    long startTime = System.currentTimeMillis();
    do {
      checkTimeoutExcess(startTime);
      Thread.sleep(delayBetweenRequests);
    } while (!isPageReady(slaveId));
  }

  private void checkTimeoutExcess(long startTime) throws TimeoutException {
    if (System.currentTimeMillis() - startTime > WAIT_FOR_PAGE_TIMEOUT) {
      log.error("Timeout on waitForPageReadiness is reached - {} ms.", WAIT_FOR_PAGE_TIMEOUT);
      throw new TimeoutException("Время ожидания заполнения страницы превысило лимит в " +
          WAIT_FOR_PAGE_TIMEOUT + " мс.");
    }
  }

  private boolean isPageReady(int slaveId) {
    try {
      InputRegister[] registers = modbusMaster.readInputRegisters(slaveId, PAGE_READY_REF, 1);
      return registers[0].getValue() != 0;
    } catch (ModbusException e) {
      log.warn("Unable to read page status", e);
      return false;
    }
  }

  private InputRegister[] readPageData() throws ModbusException {
    InputRegister[] registers = modbusMaster
        .readInputRegisters(slaveId, getPageRef(), getPageLength());
    log.debug(() -> logRegisters(registers));
    return registers;
  }

  private String logRegisters(InputRegister[] registers) {
    if (ArrayUtils.isNotEmpty(registers)) {
      return "Have read registers: " + ByteUtils.toHexDecString(registers);
    } else {
      return "Have just read no registers!";
    }
  }

  protected abstract int getCurrentPageRef();

  protected abstract int getPageRef();

  protected abstract int getPageLength();

  protected abstract T createPage(InputRegister[] registers);
}
