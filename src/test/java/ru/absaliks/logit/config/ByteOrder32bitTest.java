package ru.absaliks.logit.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

public class ByteOrder32bitTest {

  private static final int[] ARRAY = new int[]{0, 3, 2, 1};
  private ByteOrder32bit byteOrder = new ByteOrder32bit(ARRAY);

  @Test
  public void testGet() throws Exception {
    for (int i = 0; i < ARRAY.length; i++) {
      assertEquals(ARRAY[i], byteOrder.get(i));
    }
  }

  @Test
  public void testEquals() throws Exception {
    assertTrue(new ByteOrder32bit(ArrayUtils.clone(ARRAY)).equals(byteOrder));
  }

  @Test
  public void testFromStringValue() throws Exception {
    byteOrder.fromStringValue("3 0 1 2");
    assertEquals(new ByteOrder32bit(new int[]{3, 0, 1, 2}), byteOrder);
  }

  @Test
  public void testToStringValue() throws Exception {
    assertEquals("0 3 2 1", byteOrder.toStringValue());
  }

  @Test
  public void testSetValue() throws Exception {
    byteOrder.setValue(new int[]{0, 1, 2, 3});
    assertEquals(new ByteOrder32bit(new int[]{0, 1, 2, 3}), byteOrder);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCannotSetMoreThan4Bytes() throws Exception {
    byteOrder.setValue(new int[]{0, 1, 2, 3, 4});
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCannotSetLessThan4Bytes() throws Exception {
    byteOrder.setValue(new int[]{0, 1, 2});
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBytesCannotBeBiggerThanLength() throws Exception {
    byteOrder.setValue(new int[]{0, 1, 3, 5});
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBytesCannotBeNegative() throws Exception {
    byteOrder.setValue(new int[]{0, -1, 2, 3});
  }
}