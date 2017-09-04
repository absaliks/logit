package ru.absaliks.logit.tasks;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static ru.absaliks.logit.common.DateUtils.*;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import ru.absaliks.logit.model.EventDescriptions;
import ru.absaliks.logit.model.JournalEntry;

public class SaveJournalToCSV extends SaveToCSV<JournalEntry> {

  public SaveJournalToCSV(File file, List<JournalEntry> data) {
    super(file, data);
  }

  @Override
  protected void write(Writer writer, JournalEntry entry) throws IOException {
    writer.write(parseDateTime(of80Year(entry.getTimestamp())));
    writer.write(getDivisor());
    writer.write(Integer.toString(entry.getEventId()));
    writer.write(getDivisor());
    String eventDescription = EventDescriptions.getDescription(entry.getEventId());
    if (isNotEmpty(eventDescription)) {
      writer.write(eventDescription);
    }
  }
}
