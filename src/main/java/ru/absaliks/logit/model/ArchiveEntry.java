package ru.absaliks.logit.model;

public class ArchiveEntry {
  private double pumpPower;
  private long timestamp;

  public double getPumpPower() {
    return pumpPower;
  }

  public void setPumpPower(double pumpPower) {
    this.pumpPower = pumpPower;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public String toString() {
    return "ArchiveEntry{" +
        "pumpPower=" + pumpPower +
        ", timestamp=" + timestamp +
        '}';
  }
}
