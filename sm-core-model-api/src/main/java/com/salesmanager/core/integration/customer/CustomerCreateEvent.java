package com.salesmanager.core.integration.customer;

import com.salesmanager.common.model.integration.CreateEvent;

public class CustomerCreateEvent extends CreateEvent<CustomerDTO> {

    public CustomerCreateEvent(CustomerDTO dto) {
        super(dto);
    }

}
