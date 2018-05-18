package com.salesmanager.catalog.business.integration.core.listener;

import com.salesmanager.common.model.integration.CreateEvent;
import com.salesmanager.core.integration.merchant.MerchantStoreDTO;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MerchantStoreCoreEventListener {

    @EventListener
    public void handleMerchantStoreCreateEvent(CreateEvent<MerchantStoreDTO> event) {
        System.out.println(event);
    }

}
