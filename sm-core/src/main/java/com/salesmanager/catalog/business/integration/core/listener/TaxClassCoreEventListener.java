package com.salesmanager.catalog.business.integration.core.listener;

import com.salesmanager.common.model.integration.CreateEvent;
import com.salesmanager.common.model.integration.DeleteEvent;
import com.salesmanager.common.model.integration.UpdateEvent;
import com.salesmanager.core.integration.tax.TaxClassDTO;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class TaxClassCoreEventListener {

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleTaxClassCreateEvent(CreateEvent<TaxClassDTO> event) {

    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleTaxClassDeleteEvent(DeleteEvent<TaxClassDTO> event) {

    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handletaxClassUpdateEvent(UpdateEvent<TaxClassDTO> event) {

    }

}
