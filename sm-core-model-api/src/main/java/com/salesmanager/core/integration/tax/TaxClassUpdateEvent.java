package com.salesmanager.core.integration.tax;

import com.salesmanager.common.model.integration.UpdateEvent;

public class TaxClassUpdateEvent extends UpdateEvent<TaxClassDTO> {

    public TaxClassUpdateEvent(TaxClassDTO dto) {
        super(dto);
    }

}
