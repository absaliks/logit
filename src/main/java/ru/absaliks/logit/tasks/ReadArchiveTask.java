package ru.absaliks.logit.tasks;

import com.ghgande.j2mod.modbus.facade.AbstractModbusMaster;
import com.ghgande.j2mod.modbus.procimg.InputRegister;
import ru.absaliks.logit.common.ByteUtils;
import ru.absaliks.logit.model.ArchiveEntry;

public class ReadArchiveTask extends ReadPagesTask<ArchiveEntry> {
  private static final int CURRENT_PAGE_REF = 9;
  public static final int PAGE_REF = 20;
  public static final int PAGE_LENGTH = 4;

  public ReadArchiveTask(AbstractModbusMaster master, int slaveId, int pagesCount) {
    super(master, slaveId, pagesCount);
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
    ArchiveEntry entry = new ArchiveEntry();
    entry.setPumpPower(getPumpPowerValue(registers));
    entry.setTimestamp(getTimestampValue(registers));
    return entry;
  }

  private double getPumpPowerValue(InputRegister[] registers) {
    return ByteUtils.getFloat(registers[0].toBytes(), registers[1].toBytes());
  }

  private long getTimestampValue(InputRegister[] registers) {
    return ByteUtils.getUInt32(registers[2].toBytes(), registers[3].toBytes());
  }
}
