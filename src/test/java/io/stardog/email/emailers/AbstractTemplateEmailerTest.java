package io.stardog.email.emailers;

import com.google.common.collect.ImmutableMap;
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
}