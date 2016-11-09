package io.stardog.email.emailer;

import com.sailthru.client.SailthruClient;
import com.sailthru.client.params.Send;
import io.stardog.email.interfaces.EmailTemplateSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

public class SailthruEmailer implements EmailTemplateSender {
    private final SailthruClient client;
    private final static Logger LOGGER = LoggerFactory.getLogger(SailthruEmailer.class);
    protected final Map<String,Object> globalVars = new HashMap<>();

    public void putGlobalVar(String key, Object val) {
        globalVars.put(key, val);
    }

    @Inject
    public SailthruEmailer(SailthruClient client) {
        super();
        this.client = client;
    }

    @Override
    public String sendTemplate(String templateName, String toEmail, String toName, Map<String, Object> vars) {
        Send send = new Send();
        send.setTemplate(templateName);
        send.setEmail(toEmail);

        Map<String,Object> scope = new HashMap<>();
        scope.putAll(globalVars);
        scope.putAll(vars);
        scope.put("name", toName);
        send.setVars(scope);

        try {
            return (String)client.send(send).getResponse().get("send_id");
        } catch (IOException e) {
            LOGGER.error("Failed to send " + templateName + " to " + toEmail, e);
            throw new UncheckedIOException(e);
        }
    }
}
