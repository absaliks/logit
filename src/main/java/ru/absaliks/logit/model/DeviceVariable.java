package ru.absaliks.logit.model;

import javax.xml.bind.annotation.XmlAttribute;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.absaliks.logit.config.DataType;
import ru.absaliks.logit.config.DefaultDeviceVariable;

@ToString
@EqualsAndHashCode
public class DeviceVariable {
  @XmlAttribute
  public String name;

  @XmlAttribute
  public int address;

  public transient String description;

  public transient DataType dataType;

  public DeviceVariable() { }

  public DeviceVariable(DefaultDeviceVariable defaultVariable) {
    this.name = defaultVariable.name();
    this.address = defaultVariable.getDefaultAddress();
    this.description = defaultVariable.getLabel();
    this.dataType = defaultVariable.getDataType();
  }
}
