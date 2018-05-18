package com.salesmanager.core.integration.merchant;

import com.salesmanager.common.model.integration.UpdateEvent;

public class MerchantStoreUpdateEvent extends UpdateEvent<MerchantStoreDTO> {

    public MerchantStoreUpdateEvent(MerchantStoreDTO dto) {
        super(dto);
    }

}
