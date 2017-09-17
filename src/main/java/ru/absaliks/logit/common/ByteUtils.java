package ru.absaliks.logit.common;

import static java.nio.ByteOrder.BIG_ENDIAN;
import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static ru.absaliks.logit.common.DateUtils.of80Year;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Logger;
import org.apache.commons.lang3.ArrayUtils;
import ru.absaliks.logit.config.Config;

public class ByteUtils {

  private static final Logger LOG = Logger.getLogger(ByteUtils.class.getName());

  public static void getUInt32a(byte[] bytes, int a, int b, int c, int d) {
    int value = (bytes[a] & 0xFF) |
        ((bytes[b] & 0xFF) << 8) |
        ((bytes[c] & 0xFF) << 16) |
        ((bytes[d] & 0xFF) << 24);
    long l = Integer.toUnsignedLong(value);
    System.out.println("==[" + d+c+b+a+"]== "+ l + " > " + DateUtils.parseDateTime(of80Year(l)));
  }

  public static long getUInt32(byte[] bytes0, byte[] bytes1, int[] byteOrder) {
    return getUInt32(new byte[]{bytes0[0], bytes0[1], bytes1[0], bytes1[1]}, byteOrder);
  }
  public static long getUInt32(byte[] bytes, int[] byteOrder) {
    int value = (bytes[byteOrder[3]] & 0xFF) |
        ((bytes[byteOrder[2]] & 0xFF) << 8) |
        ((bytes[byteOrder[1]] & 0xFF) << 16) |
        ((bytes[byteOrder[0]] & 0xFF) << 24);
    return Integer.toUnsignedLong(value);
  }

  public static float getFloat(byte[] bytes0, byte[] bytes1) {
    return getFloat(ArrayUtils.addAll(bytes1, bytes0));
  }

  public static float getFloatF(byte[] bytes, byte[] byteOrder) {
    float result = (bytes[byteOrder[3]] & 0xFF) |
        ((bytes[byteOrder[2]] & 0xFF) << 8) |
        ((bytes[byteOrder[1]] & 0xFF) << 16) |
        ((bytes[byteOrder[0]] & 0xFF) << 24);
    return result;
  }

  public static float getFloat(byte[] b) {
    return ByteBuffer.wrap(b).getFloat();
  }
}
