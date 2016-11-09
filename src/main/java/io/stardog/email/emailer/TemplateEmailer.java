package io.stardog.email.emailer;

import io.stardog.email.interfaces.EmailTemplateSender;

import java.util.HashMap;
import java.util.Map;

public abstract class TemplateEmailer implements EmailTemplateSender {
    protected final Map<String,Object> globalVars = new HashMap<>();

    public void putGlobalVar(String key, Object val) {
        globalVars.put(key, val);
    }
}
