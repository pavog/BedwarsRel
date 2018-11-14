package io.github.bedwarsrel;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;

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
    public void test() {
        fail("Not implemented");
    }
 } 
