package site.alexkononsol.siteToOK.service.impl;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import site.alexkononsol.siteToOK.service.MessagesSourcesService;

import java.util.Locale;

@Service
public class MessagesSourcesServiceImpl implements MessagesSourcesService {
    private MessageSource messageSource;

    public MessagesSourcesServiceImpl(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    @Override
    public String getStringValue(String value) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(value, null, locale);
    }
}
