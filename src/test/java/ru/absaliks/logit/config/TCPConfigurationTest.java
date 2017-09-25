package ru.absaliks.logit.config;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TCPConfigurationTest {

  private TCPConfiguration tcpConfiguration;

  @Before
  public void setUp() throws Exception {
    tcpConfiguration = new TCPConfiguration();
    tcpConfiguration.port = 1;
    tcpConfiguration.address = "127.0.0.1";
  }

  @Test
  public void testEquals() throws Exception {
    TCPConfiguration cfg1 = new TCPConfiguration();
    cfg1.port = 1;
    cfg1.address = "127.0.0.1";
    assertTrue(tcpConfiguration.equals(cfg1));
  }
}