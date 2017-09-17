package ru.absaliks.logit.tasks;

import com.ghgande.j2mod.modbus.facade.AbstractModbusMaster;
import com.ghgande.j2mod.modbus.procimg.InputRegister;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import org.apache.commons.lang3.ArrayUtils;
import ru.absaliks.logit.common.ByteUtils;
import ru.absaliks.logit.model.JournalEntry;

public class ReadJournalTask extends ReadPagesTask<JournalEntry> {

  private static final Logger LOG = Logger.getLogger(ReadJournalTask.class.getName());

  private static final int CURRENT_PAGE_REF = 8;
  public static final int PAGE_REF = 16;
  public static final int PAGE_LENGTH = 3;

  public ReadJournalTask(AbstractModbusMaster master, int slaveId, int pagesCount, DoubleProperty progress) {
    super(master, slaveId, pagesCount, progress);
  }

  @Override
  protected int getCurrentPageRef() {
    return CURRENT_PAGE_REF;
  }

  @Override
  protected int getPageRef() {
    return PAGE_REF;
  }

  @Override
  protected int getPageLength() {
    return PAGE_LENGTH;
  }

  @Override
  protected JournalEntry createPage(InputRegister[] registers) {
    JournalEntry entry = new JournalEntry();
    entry.setEventId(getEventIdValue(registers));
    entry.setTimestamp(getTimestampValue(registers));
    return entry;
  }

  private int getEventIdValue(InputRegister[] registers) {
    return registers[0].getValue();
  }

  private long getTimestampValue(InputRegister[] registers) {
    return ByteUtils.getUInt32(registers[1].toBytes(), registers[2].toBytes(), config.uint32ByteOrder);
  }
}
