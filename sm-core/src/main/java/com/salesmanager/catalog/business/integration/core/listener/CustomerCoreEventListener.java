package com.salesmanager.catalog.business.integration.core.listener;

import com.salesmanager.common.model.integration.CreateEvent;
import com.salesmanager.common.model.integration.DeleteEvent;
import com.salesmanager.common.model.integration.UpdateEvent;
import com.salesmanager.core.integration.customer.CustomerDTO;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class CustomerCoreEventListener {

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleCustomerCreateEvent(CreateEvent<CustomerDTO> event) {
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleCustomerDeleteEvent(DeleteEvent<CustomerDTO> event) {
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleCustomerUpdateEvent(UpdateEvent<CustomerDTO> event) {
    }

}
