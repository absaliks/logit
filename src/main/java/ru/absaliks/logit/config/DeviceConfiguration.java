package ru.absaliks.logit.config;

import static ru.absaliks.logit.config.DefaultDeviceVariable.ARCHIVE_PUMP_POWER;
import static ru.absaliks.logit.config.DefaultDeviceVariable.ARCHIVE_TIMESTAMP;
import static ru.absaliks.logit.config.DefaultDeviceVariable.JOURNAL_EVENT_CODE;
import static ru.absaliks.logit.config.DefaultDeviceVariable.JOURNAL_TIMESTAMP;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.ArrayUtils;
import ru.absaliks.logit.model.DeviceVariable;

@ToString
@EqualsAndHashCode
public class DeviceConfiguration {

  @XmlElementWrapper(name = "variables")
  @XmlElement(name = "variable")
  public List<DeviceVariable> variables = new ArrayList<>();

  public void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
    System.out.println("After unmarshall");
    System.out.println(variables.getClass().getName());
    for (int i = variables.size() - 1; i >= 0; i--) {
      DeviceVariable variable = variables.get(i);
      try {
        if (variable.equals(new DeviceVariable(DefaultDeviceVariable.valueOf(variable.name))))
          throw new IllegalArgumentException();
      } catch (IllegalArgumentException e) {
        variables.remove(i);
      }
    }
  }
}
