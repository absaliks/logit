package ru.absaliks.logit.tasks;

import com.ghgande.j2mod.modbus.procimg.InputRegister;
import lombok.Data;
import lombok.ToString;
import ru.absaliks.logit.utils.ByteUtils;
import ru.absaliks.logit.config.ByteOrder32bit;
import ru.absaliks.logit.model.JournalEntry;

@Data
@ToString(callSuper = true)
public class ReadJournalTask extends ReadPagesTask<JournalEntry> {

  private static final int CURRENT_PAGE_REF = 8;
  public static final int PAGE_REF = 16;
  public static final int PAGE_LENGTH = 3;

  private ByteOrder32bit uint32ByteOrder;

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
    return new JournalEntry(getEventIdValue(registers), getTimestampValue(registers));
  }

  private int getEventIdValue(InputRegister[] registers) {
    return registers[0].getValue();
  }

  private long getTimestampValue(InputRegister[] registers) {
    return ByteUtils.getUInt32(registers[1].toBytes(), registers[2].toBytes(), uint32ByteOrder);
  }
}
