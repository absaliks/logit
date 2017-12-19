package ru.absaliks.logit.utils;

import com.ghgande.j2mod.modbus.procimg.InputRegister;
import java.nio.ByteBuffer;
import ru.absaliks.logit.config.ByteOrder32bit;

public class ByteUtils {

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
    return getFloat(new byte[]{
        bytes[byteOrder.get(0)],
        bytes[byteOrder.get(1)],
        bytes[byteOrder.get(2)],
        bytes[byteOrder.get(3)]});
  }

  public static float getFloat(byte[] b) {
    return ByteBuffer.wrap(b).getFloat();
  }

  /** Converts InputRegisters array to a two-array string, like: [aa, ff] = [170, 255] */
  public static String toHexDecString(InputRegister[] registers) {
    if (registers == null) {
      return "null";
    } else if (registers.length == 0) {
      return "[]";
    }

    byte[] bytes = convertRegistersToByteArray(registers);
    StringBuilder builder = new StringBuilder(registers.length * 10 + 3);
    builder.append("[");
    for (int i = 0; i < bytes.length; ) {
      builder.append(Integer.toHexString(Byte.toUnsignedInt(bytes[i])));
      if (++i != bytes.length) {
        builder.append(", ");
      }
    }
    builder.append("] = [");
    for (int i = 0; i < bytes.length; ) {
      builder.append(bytes[i]);
      if (++i != bytes.length) {
        builder.append(", ");
      }
    }
    return builder.append("]").toString();
  }

  private static byte[] convertRegistersToByteArray(InputRegister[] registers) {
    byte[] bytes = new byte[registers.length * 2];
    for (int i = 0; i < registers.length; i++) {
      System.arraycopy(registers[i].toBytes(), 0, bytes, i * 2, 2);
    }
    return bytes;
  }
}