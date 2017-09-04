package ru.absaliks.logit.config;

public enum SerialParity {
  N("none"), E("even"), O("odd");

  private String title;

  SerialParity(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }
}
