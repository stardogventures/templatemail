package io.stardog.email.emailers;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractTemplateEmailerTest {
    @Test
    public void testGlobalVars() throws Exception {
        NonEmailer emailer = new NonEmailer();
        emailer.addGlobalVar("foo", "bar");
        assertEquals("bar", emailer.getGlobalVar("foo"));
        assertEquals(ImmutableMap.of("foo", "bar"), emailer.getGlobalVars());
    }

    @Test
    public void testWhitelist() throws Exception {
        NonEmailer emailer = new NonEmailer();
        emailer.setWhitelist(ImmutableSet.of("test@test.com", "example.com"));

        assertTrue(emailer.isWhitelisted("test@test.com"));
        assertFalse(emailer.isWhitelisted("test2@test.com"));
        assertTrue(emailer.isWhitelisted("test@example.com"));
        assertFalse(emailer.isWhitelisted("test@gmail.com"));
    }
}