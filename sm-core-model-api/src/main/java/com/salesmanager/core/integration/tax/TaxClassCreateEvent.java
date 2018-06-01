package com.salesmanager.core.integration.tax;

import com.salesmanager.common.model.integration.CreateEvent;

public class TaxClassCreateEvent extends CreateEvent<TaxClassDTO> {

    public TaxClassCreateEvent(TaxClassDTO dto) {
        super(dto);
    }

}
