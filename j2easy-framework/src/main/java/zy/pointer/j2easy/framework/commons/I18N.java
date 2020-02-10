package zy.pointer.j2easy.framework.commons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class I18N {

    @Autowired
    MessageSource messageSource;

    public String getI18NMessage( String code ){
        return getI18NMessage( code , null );
    }

    public String getI18NMessage( String code , Object... params ){
        return messageSource.getMessage(  code , params , LocaleContextHolder.getLocale());
    }

}
