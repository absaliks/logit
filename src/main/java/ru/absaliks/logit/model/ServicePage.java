package ru.absaliks.logit.model;

import lombok.ToString;

@ToString
public class ServicePage {
  public int servicePageRefId;
  public int controllerModel;
  public int serialNo;
  public int journalRefId;
  public int journalPageCount;
  public int journalPageNo;
  public int archiveRefId;
  public int archivePageCount;
  public int archivePageNo;
  public int pageIsReadyFlag;
}
