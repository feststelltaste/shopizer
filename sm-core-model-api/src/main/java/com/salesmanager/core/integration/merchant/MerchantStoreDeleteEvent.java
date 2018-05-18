package com.salesmanager.core.integration.merchant;

import com.salesmanager.common.model.integration.DeleteEvent;

public class MerchantStoreDeleteEvent extends DeleteEvent<MerchantStoreDTO> {

    public MerchantStoreDeleteEvent(MerchantStoreDTO dto) {
        super(dto);
    }

}
