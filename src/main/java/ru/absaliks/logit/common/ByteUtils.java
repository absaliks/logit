package ru.absaliks.logit.common;

import java.nio.ByteBuffer;
import java.util.logging.Logger;
import ru.absaliks.logit.config.ByteOrder32bit;

public class ByteUtils {

  private static final Logger LOG = Logger.getLogger(ByteUtils.class.getName());

  public static long getUInt32(byte[] bytes0, byte[] bytes1, ByteOrder32bit byteOrder) {
    return getUInt32(new byte[]{bytes0[0], bytes0[1], bytes1[0], bytes1[1]}, byteOrder);
  }

  public static long getUInt32(byte[] bytes, ByteOrder32bit byteOrder) {
    int value = (bytes[byteOrder.get(3)] & 0xFF) |
        ((bytes[byteOrder.get(2)] & 0xFF) << 8) |
        ((bytes[byteOrder.get(1)] & 0xFF) << 16) |
        ((bytes[byteOrder.get(0)] & 0xFF) << 24);
    return Integer.toUnsignedLong(value);
  }

  public static float getFloat(byte[] bytes0, byte[] bytes1, ByteOrder32bit byteOrder) {
    byte[] bytes = {bytes0[0], bytes0[1], bytes1[0], bytes1[1]};
    return getFloat(new byte[]{bytes[byteOrder.get(0)], bytes[byteOrder.get(1)], bytes[byteOrder.get(2)], bytes[byteOrder.get(3)]});
  }

  public static float getFloat(byte[] b) {
    return ByteBuffer.wrap(b).getFloat();
  }
}
