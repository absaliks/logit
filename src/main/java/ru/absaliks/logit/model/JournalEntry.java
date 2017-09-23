package ru.absaliks.logit.model;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class JournalEntry {
  public final int eventId;
  public final long timestamp;
}
