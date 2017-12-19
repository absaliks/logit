package ru.absaliks.logit.config;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TCPSettingsTest {

  private TCPSettings tcpSettings;

  @Before
  public void setUp() throws Exception {
    tcpSettings = new TCPSettings();
    tcpSettings.port = 1;
    tcpSettings.address = "127.0.0.1";
  }

  @Test
  public void testEquals() throws Exception {
    TCPSettings cfg1 = new TCPSettings();
    cfg1.port = 1;
    cfg1.address = "127.0.0.1";
    assertTrue(tcpSettings.equals(cfg1));
  }
}