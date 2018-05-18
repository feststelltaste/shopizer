package com.salesmanager.core.integration;

import com.salesmanager.common.model.integration.CreateEvent;
import com.salesmanager.core.integration.merchant.MerchantStoreCreateEvent;
import com.salesmanager.core.integration.merchant.MerchantStoreDTO;
import com.salesmanager.core.model.merchant.MerchantStore;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;


@Component
public class CreateEventListener implements PostInsertEventListener, ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void onPostInsert(PostInsertEvent event) {
        if (event.getEntity() instanceof MerchantStore) {
            MerchantStore store = ((MerchantStore) event.getEntity());
            applicationEventPublisher.publishEvent(new MerchantStoreCreateEvent(store.toDTO()));
        }
    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        return false;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
