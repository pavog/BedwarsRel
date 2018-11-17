package io.github.bedwarsrel;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestConfig {
  @SuppressWarnings("unused")
  private ServerMock server;
  @SuppressWarnings("unused")
  private BedwarsRel plugin;

  @Before
  public void setUp() throws Exception {
    server = MockBukkit.mock();
    plugin = MockBukkit.load(BedwarsRel.class);
  }

  @After
  public void tearDown() {
    MockBukkit.unload();
  }

  @Test
  public void testConfig() {
    assertNotNull(plugin.getConfig());
    assertNotNull(plugin.getConfig().getString("chat-prefix"));
    assertEquals(plugin.getConfig().getString("chat-prefix"), plugin.getStringConfig("chat-prefix", "BedWars"));
  }
}
