package ru.absaliks.logit.config;

import static org.apache.commons.lang3.ArrayUtils.indexOf;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

import java.util.Arrays;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlAttribute;
import org.apache.commons.lang3.StringUtils;

public class ByteOrder32bit {

  private static final Logger LOG = Logger.getLogger(ByteOrder32bit.class.getName());
  private static final int LENGTH = 4;

  private int[] value;

  public ByteOrder32bit() {
  }

  public ByteOrder32bit(int[] value) {
    setValue(value);
  }

  public int[] getValue() {
    return value;
  }

  @XmlAttribute
  public void setValue(int[] value) {
    if (!validate(value)) {
      LOG.warning("Попытка установить некорректный порядок байтов: " + Arrays.toString(value));
      throw new IllegalArgumentException();
    }
    this.value = value;
  }

  public int get(int pos) {
    return value[pos];
  }

  public boolean validate(int[] byteOrder) {
    boolean isValid = isNotEmpty(byteOrder) && byteOrder.length == LENGTH;
    for (int i = 0; isValid && i < LENGTH; i++) {
      isValid = isValid && indexOf(byteOrder, i) != -1;
    }
    return isValid;
  }

  public String toStringValue() {
    return StringUtils.join(value, ' ');
  }

  public void fromStringValue(String string) throws IllegalArgumentException {
    String[] strings = StringUtils.split(string, ' ');
    int[] value = new int[strings.length];
    for (int i = 0; i < strings.length; i++) {
      value[i] = Integer.parseInt(strings[i]);
    }
    setValue(value);
  }

  @Override
  public String toString() {
    return "ByteOrder32bit{value=" + Arrays.toString(value) + '}';
  }
}
