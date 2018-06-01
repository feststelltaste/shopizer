package com.salesmanager.core.integration.tax;

import com.salesmanager.common.model.integration.DeleteEvent;

public class TaxClassDeleteEvent extends DeleteEvent<TaxClassDTO> {

    public TaxClassDeleteEvent(TaxClassDTO dto) {
        super(dto);
    }

}
