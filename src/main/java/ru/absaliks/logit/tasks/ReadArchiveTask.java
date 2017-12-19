package ru.absaliks.logit.tasks;

import static java.util.Objects.nonNull;

import com.ghgande.j2mod.modbus.procimg.InputRegister;
import lombok.Data;
import lombok.ToString;
import ru.absaliks.logit.utils.ByteUtils;
import ru.absaliks.logit.config.ByteOrder32bit;
import ru.absaliks.logit.model.ArchiveEntry;

@Data
@ToString(callSuper = true)
public class ReadArchiveTask extends ReadPagesTask<ArchiveEntry> {
  private static final int CURRENT_PAGE_REF = 9;
  private static final int PAGE_REF = 20;
  private static final int PAGE_LENGTH = 4;

  private ByteOrder32bit real32ByteOrder;
  private ByteOrder32bit uint32ByteOrder;

  @Override
  public boolean isValid() {
    return super.isValid() && nonNull(real32ByteOrder) && nonNull(uint32ByteOrder);
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
  protected ArchiveEntry createPage(InputRegister[] registers) {
    return new ArchiveEntry(getPumpPowerValue(registers), getTimestampValue(registers));
  }

  private double getPumpPowerValue(InputRegister[] registers) {
    return ByteUtils.getFloat(registers[0].toBytes(), registers[1].toBytes(), real32ByteOrder);
  }

  private long getTimestampValue(InputRegister[] registers) {
    return ByteUtils.getUInt32(registers[2].toBytes(), registers[3].toBytes(), uint32ByteOrder);
  }
}
