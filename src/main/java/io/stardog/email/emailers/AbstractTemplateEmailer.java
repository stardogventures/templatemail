package io.stardog.email.emailers;

import com.google.common.collect.ImmutableSet;
import io.stardog.email.interfaces.TemplateEmailer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class AbstractTemplateEmailer implements TemplateEmailer {
    private final Map<String,Object> globalVars = new HashMap<>();
    private Set<String> whitelist = null;

    @Override
    public void addGlobalVar(String key, Object val) {
        globalVars.put(key, val);
    }

    @Override
    public void setWhitelist(Collection<String> whitelist) {
        this.whitelist = ImmutableSet.copyOf(whitelist);
    }

    public Object getGlobalVar(String key) {
        return globalVars.get(key);
    }

    public Map<String, Object> getGlobalVars() {
        return globalVars;
    }

    /**
     * Given an email address, check whether it, or its domain, is on the whitelist.
     * @param email email address
     * @return  true if the email address is on the whitelist (or if there is no whitelist)
     */
    public boolean isWhitelisted(String email) {
        if (whitelist == null) {
            return true;
        }
        if (whitelist.contains(email)) {
            return true;
        }
        int i = email.indexOf("@");
        if (i == -1) {
            return false;
        }
        String domain = email.substring(i+1);
        return whitelist.contains(domain);
    }
}
