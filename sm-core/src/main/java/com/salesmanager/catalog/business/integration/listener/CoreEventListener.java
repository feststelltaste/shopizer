package com.salesmanager.catalog.business.integration.listener;

import com.salesmanager.common.model.integration.CreateEvent;
import com.salesmanager.common.model.integration.DeleteEvent;
import com.salesmanager.common.model.integration.UpdateEvent;
import com.salesmanager.core.integration.merchant.MerchantStoreDTO;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CoreEventListener {

    @EventListener
    public void handleMerchantStoreCreateEvent(CreateEvent<MerchantStoreDTO> event) {
        System.out.println(event);
    }

    @EventListener
    public void handleMerchantStoreDeleteEvent(DeleteEvent<MerchantStoreDTO> event) {
        System.out.println(event);
    }

    @EventListener
    public void handleMerchantStoreUpdateEvent(UpdateEvent<MerchantStoreDTO> event) {
        System.out.println(event);
    }

}
