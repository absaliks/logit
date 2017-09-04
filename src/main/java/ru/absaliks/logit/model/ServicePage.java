package ru.absaliks.logit.model;

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

  @Override
  public String toString() {
    return "ServicePage{" +
        "servicePageRefId=" + servicePageRefId +
        ", controllerModel=" + controllerModel +
        ", serialNo=" + serialNo +
        ", journalRefId=" + journalRefId +
        ", journalPageCount=" + journalPageCount +
        ", journalPageNo=" + journalPageNo +
        ", archiveRefId=" + archiveRefId +
        ", archivePageCount=" + archivePageCount +
        ", archivePageNo=" + archivePageNo +
        ", pageIsReadyFlag=" + pageIsReadyFlag +
        '}';
  }
}
