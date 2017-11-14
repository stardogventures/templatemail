package io.stardog.email.emailers;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class AbstractHandlebarsTemplateEmailerTest {
    @Test
    public void evaluateHandlebarsTemplate() throws Exception {
        NonEmailer nonEmailer = new NonEmailer();

        Handlebars handlebars = new Handlebars();
        Template template = handlebars.compileInline("Hi there {{name}}!");
        Map<String,Object> vars = ImmutableMap.of("name", "Bob");

        String eval = nonEmailer.evaluateHandlebarsTemplate(template, vars);
        assertEquals("Hi there Bob!", eval);
    }

    @Test
    public void toAddress() throws Exception {
        assertEquals("Bob Smith <bob@example.com>",
                AbstractHandlebarsTemplateEmailer.toAddress("bob@example.com", "Bob Smith"));
        assertEquals("bob@example.com",
                AbstractHandlebarsTemplateEmailer.toAddress("bob@example.com", null));
        assertEquals("\"Bob Smith, Esq\" <bob@example.com>",
                AbstractHandlebarsTemplateEmailer.toAddress("bob@example.com", "Bob Smith, Esq"));
    }

}