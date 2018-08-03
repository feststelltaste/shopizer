package com.salesmanager.catalog.integration;

import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

@Component
public class HibernateCatalogListenerConfigurer {

    @PersistenceUnit(unitName = "catalogEntityManagerFactory")
    private EntityManagerFactory entityManagerFactory;

    private CatalogCreateEventListener catalogCreateEventListener;

    private CatalogDeleteEventListener catalogDeleteEventListener;

    private CatalogUpdateEventListener catalogUpdateEventListener;

    @Autowired
    public HibernateCatalogListenerConfigurer(CatalogCreateEventListener catalogCreateEventListener, CatalogDeleteEventListener catalogDeleteEventListener,
                                              CatalogUpdateEventListener catalogUpdateEventListener) {
        this.catalogCreateEventListener = catalogCreateEventListener;
        this.catalogDeleteEventListener = catalogDeleteEventListener;
        this.catalogUpdateEventListener = catalogUpdateEventListener;
    }


    @PostConstruct
    protected void init() {
        SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
        registry.getEventListenerGroup(EventType.POST_INSERT).appendListener(catalogCreateEventListener);
        registry.getEventListenerGroup(EventType.POST_DELETE).appendListener(catalogDeleteEventListener);
        registry.getEventListenerGroup(EventType.POST_UPDATE).appendListener(catalogUpdateEventListener);
    }
}