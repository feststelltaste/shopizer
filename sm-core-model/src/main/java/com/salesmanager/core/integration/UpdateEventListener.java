package com.salesmanager.core.integration;

import com.salesmanager.core.integration.merchant.MerchantStoreCreateEvent;
import com.salesmanager.core.integration.merchant.MerchantStoreDeleteEvent;
import com.salesmanager.core.integration.merchant.MerchantStoreUpdateEvent;
import com.salesmanager.core.model.merchant.MerchantStore;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;


@Component
public class UpdateEventListener implements PostUpdateEventListener, ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        if (event.getEntity() instanceof MerchantStore) {
            MerchantStore store = ((MerchantStore) event.getEntity());
            applicationEventPublisher.publishEvent(new MerchantStoreUpdateEvent(store.toDTO()));
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
