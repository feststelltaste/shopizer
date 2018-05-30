package com.salesmanager.catalog.business.integration.core.listener;

import com.salesmanager.common.model.integration.CreateEvent;
import com.salesmanager.common.model.integration.DeleteEvent;
import com.salesmanager.common.model.integration.UpdateEvent;
import com.salesmanager.core.integration.language.LanguageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class LanguageCoreEventListener {

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMerchantStoreCreateEvent(CreateEvent<LanguageDTO> event) {
        LanguageDTO languageDTO = event.getDto();
        if (languageDTO != null) {

        }
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMerchantStoreDeleteEvent(DeleteEvent<LanguageDTO> event) {
        LanguageDTO languageDTO = event.getDto();
        if (languageDTO != null) {

        }
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMerchantStoreUpdateEvent(UpdateEvent<LanguageDTO> event) {
        LanguageDTO languageDTO = event.getDto();
        if (languageDTO != null) {

        }
    }

}
