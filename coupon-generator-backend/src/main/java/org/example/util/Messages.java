package org.example.util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class Messages {

    @Autowired
    private MessageSource messageSource;

    private MessageSourceAccessor accessor;

    @PostConstruct
    private void init() {
        accessor = new MessageSourceAccessor(messageSource, Locale.JAPAN);
    }

    public String get(String code) {
        return accessor.getMessage(code);
    }

    public String getMessageForResponseCode(final String code, final Object[] params) {
        String key = "message.code." + code;
        return accessor.getMessage(key, params, "Code Message");
    }

    public String getMandatoryValidationFailureMessage(final String _field) {
        return accessor.getMessage("message.code.400", new Object[] { _field });
    }

    public String getInvalidDataValidationFailureMessage(final String _field) {
        return accessor.getMessage("message.code.401", new Object[] { _field });
    }

}
