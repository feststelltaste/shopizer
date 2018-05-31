package com.salesmanager.core.integration.customer;

import com.salesmanager.common.model.integration.UpdateEvent;

public class CustomerUpdateEvent extends UpdateEvent<CustomerDTO> {

    public CustomerUpdateEvent(CustomerDTO dto) {
        super(dto);
    }

}
