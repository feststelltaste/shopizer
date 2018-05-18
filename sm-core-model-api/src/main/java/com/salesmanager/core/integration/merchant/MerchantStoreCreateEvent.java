package com.salesmanager.core.integration.merchant;

import com.salesmanager.common.model.integration.CreateEvent;

public class MerchantStoreCreateEvent extends CreateEvent<MerchantStoreDTO> {

    public MerchantStoreCreateEvent(MerchantStoreDTO dto) {
        super(dto);
    }

}
