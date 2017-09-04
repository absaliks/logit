package ru.absaliks.logit.common;

import static com.ghgande.j2mod.modbus.util.ModbusUtil.registersToInt;
import static java.nio.ByteOrder.BIG_ENDIAN;
import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Logger;
import org.apache.commons.lang3.ArrayUtils;
import ru.absaliks.logit.config.Config;

public class ByteUtils {

  private static final Logger LOG = Logger.getLogger(ByteUtils.class.getName());
  private static ByteOrder BYTE_ORDER;

  static {
    String byteOrder = Config.getInstance().decimalByteOrder;
    if (isNotEmpty(byteOrder)) {
      switch (byteOrder) {
        case "BIG_ENDIAN":
          BYTE_ORDER = BIG_ENDIAN;
          break;
        case "LITTLE_ENDIAN":
          BYTE_ORDER = LITTLE_ENDIAN;
          break;
        default:
          BYTE_ORDER = null;
      }
    }
    if (BYTE_ORDER != null) {
      LOG.info("Successfully set Decimal Byte Order to " + BYTE_ORDER);
    } else if (isNotEmpty(byteOrder)) {
      LOG.info("Unsupported byteorder provided: " + byteOrder);
    }
  }

  public static long getUInt32(byte[] bytes0, byte[] bytes1) {
    int value = (bytes1[0] & 0xFF) |
        ((bytes1[1] & 0xFF) << 8) |
        ((bytes0[0] & 0xFF) << 16) |
        ((bytes0[1] & 0xFF) << 24);
    return Integer.toUnsignedLong(value);
  }

  public static float getFloat(byte[] bytes0, byte[] bytes1) {
    return getFloat(ArrayUtils.addAll(bytes0, bytes1));
  }

  public static float getFloat(byte[] b) {
    if (nonNull(BYTE_ORDER)) {
      System.out.println(BYTE_ORDER);
      return ByteBuffer.wrap(b).order(BYTE_ORDER).getFloat();
    } else {
      return ByteBuffer.wrap(b).getFloat();
    }
  }
}
