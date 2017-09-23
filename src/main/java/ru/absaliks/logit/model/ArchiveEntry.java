package ru.absaliks.logit.model;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class ArchiveEntry {
  public final double pumpPower;
  public final long timestamp;
}
