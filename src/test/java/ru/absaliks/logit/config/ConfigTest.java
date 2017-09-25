package ru.absaliks.logit.config;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ConfigTest {

  @Test
  public void testFreshObjectsAreEqual() throws Exception {
    assertTrue(new Config().equals(new Config()));
  }
}