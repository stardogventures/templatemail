package io.stardog.email.emailers;

import io.stardog.email.interfaces.TemplateEmailer;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTemplateEmailer implements TemplateEmailer {
    private final Map<String,Object> globalVars = new HashMap<>();

    @Override
    public void addGlobalVar(String key, Object val) {
        globalVars.put(key, val);
    }

    public Object getGlobalVar(String key) {
        return globalVars.get(key);
    }

    public Map<String, Object> getGlobalVars() {
        return globalVars;
    }
}
