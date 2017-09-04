package ru.absaliks.logit.tasks;

import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.facade.AbstractModbusMaster;
import com.ghgande.j2mod.modbus.procimg.InputRegister;
import java.util.concurrent.Callable;
import ru.absaliks.logit.model.ServicePage;

public class ReadServicePageTask implements Callable<ServicePage> {
  private static final int SERVICE_PAGE_REF = 1;
  private static final int SERVICE_PAGE_LENGTH = 10;

  private final AbstractModbusMaster master;
  private final int slaveId;

  public ReadServicePageTask(AbstractModbusMaster master, int slaveId) {
    this.master = master;
    this.slaveId = slaveId;
  }

  @Override
  public ServicePage call() throws ModbusException {
    InputRegister[] registers = master.readInputRegisters(slaveId, SERVICE_PAGE_REF, SERVICE_PAGE_LENGTH);
    return createServicePage(registers);
  }

  private ServicePage createServicePage(InputRegister[] registers) {
    ServicePage page = new ServicePage();
    page.servicePageRefId = registers[0].getValue();
    page.controllerModel = registers[1].getValue();
    page.serialNo = registers[2].getValue();
    page.journalRefId = registers[3].getValue();
    page.journalPageCount = registers[4].getValue();
    page.archiveRefId = registers[5].getValue();
    page.archivePageCount = registers[6].getValue();
    page.journalPageNo = registers[7].getValue();
    page.archivePageNo = registers[8].getValue();
    page.pageIsReadyFlag = registers[9].getValue();
    return page;
  }
}
