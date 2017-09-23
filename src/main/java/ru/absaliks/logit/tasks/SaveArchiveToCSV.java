package ru.absaliks.logit.tasks;

import static ru.absaliks.logit.common.DateUtils.of80Year;
import static ru.absaliks.logit.common.DateUtils.parseDateTime;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import ru.absaliks.logit.model.ArchiveEntry;

public class SaveArchiveToCSV extends SaveToCSV<ArchiveEntry> {

  public SaveArchiveToCSV(File file, List<ArchiveEntry> data) {
    super(file, data);
  }

  @Override
  protected void write(Writer writer, ArchiveEntry entry) throws IOException {
    writer.write(parseDateTime(of80Year(entry.timestamp)));
    writer.write(getDivisor());
    writer.write(String.format ("%.4f", entry.pumpPower));
  }
}
