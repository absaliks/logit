package ru.absaliks.logit.config;

public enum DataType {
  UINT16(1), UINT32(2), REAL32(2);

  public final int size;

  DataType(int size) {
    this.size = size;
  }
}
