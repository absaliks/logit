package ru.absaliks.logit.model;

import java.time.LocalDateTime;
import ru.absaliks.logit.common.DateUtils;

public class JournalEntry {
  private int eventId;
  private long timestamp;

  public int getEventId() {
    return eventId;
  }

  public void setEventId(int eventId) {
    this.eventId = eventId;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public String toString() {
    return "JournalEntry{" +
        "eventId=" + eventId +
        ", timestamp=" + timestamp +
        '}';
  }
}
