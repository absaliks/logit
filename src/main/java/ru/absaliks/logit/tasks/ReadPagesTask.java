package ru.absaliks.logit.tasks;

import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.facade.AbstractModbusMaster;
import com.ghgande.j2mod.modbus.procimg.InputRegister;
import com.ghgande.j2mod.modbus.procimg.SimpleInputRegister;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import javafx.beans.property.DoubleProperty;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ArrayUtils;
import ru.absaliks.logit.config.Config;

@Log4j2
@ToString(exclude = {"modbusMaster"})
public abstract class ReadPagesTask<T> implements Callable<List<T>> {

  public static final int PAGE_READY_REF = 10;
  public static final int WAIT_FOR_PAGE_TIMEOUT = 3000;

  private final AbstractModbusMaster modbusMaster;
  private final int slaveId;
  private final int pagesCount;
  private final int delayBetweenRequests;
  private final DoubleProperty progress;
  private final List<T> pages;
  protected Config config = Config.getInstance();

  private final float progressPerPage;

  public ReadPagesTask(AbstractModbusMaster modbusMaster, int slaveId, int pagesCount,
      DoubleProperty progress) {
    if (pagesCount == 0)
      throw new IllegalArgumentException("pagesCount = 0");

    this.modbusMaster = modbusMaster;
    this.pagesCount = pagesCount;
    this.slaveId = slaveId;
    this.pages = new ArrayList<>(pagesCount);
    this.progress = progress;
    this.delayBetweenRequests = Config.getInstance().modbus.delayBetweenRequests;
    this.progressPerPage = 1f / pagesCount;

    log.debug(this::toString);
  }

  public List<T> call() throws InterruptedException, ModbusException {
    for (int pageId = 0; pageId < pagesCount; pageId++) {
      writeJournalPageNumber(slaveId, pageId);
      Thread.sleep(delayBetweenRequests);
      waitForPageReadiness(slaveId);
      T page = createPage(readPageData());
      pages.add(page);

      progress.setValue(progressPerPage * (pageId + 1));
      log.debug("Page {}: {}", pageId, page);
    }
    return pages;
  }

  private void writeJournalPageNumber(int slaveId, int page) throws ModbusException {
    try {
      modbusMaster.writeSingleRegister(slaveId, getCurrentPageRef(), new SimpleInputRegister(page));
    } catch (ModbusException e) {
      log.warn("Unable to write page number to read", e);
      throw e;
    }
  }

  private void waitForPageReadiness(int slaveId) throws InterruptedException {
    boolean timedOut = false;
    boolean isReady = false;
    long startTime = System.currentTimeMillis();
    while (!((timedOut = isTimeoutReached(startTime)) || (isReady = isPageReady(slaveId)))) {
      Thread.sleep(delayBetweenRequests);
    }
    log.debug("timedOut={}, ready={}", timedOut, isReady);
  }

  private boolean isTimeoutReached(long startTime) {
    return System.currentTimeMillis() - startTime > WAIT_FOR_PAGE_TIMEOUT;
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
      byte[] bytes = new byte[registers.length * 2];
      for (int i = 0; i < registers.length; i++) {
        System.arraycopy(registers[i].toBytes(), 0, bytes, i * 2, 2);
      }

      StringBuilder builder = new StringBuilder("Have read registers: [");
      for (int i = 0; i < bytes.length;) {
        builder.append(Integer.toHexString(Byte.toUnsignedInt(bytes[i])));
        if (++i != bytes.length)
          builder.append(", ");
      }
      builder.append("] > ");
      builder.append(Arrays.toString(bytes));
      return builder.toString();
    } else
      return "Have just read no registers!";
  }

  protected abstract int getCurrentPageRef();

  protected abstract int getPageRef();

  protected abstract int getPageLength();

  protected abstract T createPage(InputRegister[] registers);
}
