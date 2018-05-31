package com.salesmanager.core.integration.customer;

import com.salesmanager.common.model.integration.DeleteEvent;

public class CustomerDeleteEvent extends DeleteEvent<CustomerDTO> {

    public CustomerDeleteEvent(CustomerDTO dto) {
        super(dto);
    }

}
