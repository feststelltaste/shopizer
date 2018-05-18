package com.salesmanager.core.integration;

import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

@Component
public class HibernateListenerConfigurer {

    @PersistenceUnit(unitName = "shopizerContainer")
    private EntityManagerFactory entityManagerFactory;

    private CreateEventListener createEventListener;

    private DeleteEventListener deleteEventListener;

    private UpdateEventListener updateEventListener;

    @Autowired
    public HibernateListenerConfigurer(CreateEventListener createEventListener, DeleteEventListener deleteEventListener,
                                       UpdateEventListener updateEventListener) {
        this.createEventListener = createEventListener;
        this.deleteEventListener = deleteEventListener;
        this.updateEventListener = updateEventListener;
    }


    @PostConstruct
    protected void init() {
        SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
        registry.getEventListenerGroup(EventType.POST_INSERT).appendListener(createEventListener);
        registry.getEventListenerGroup(EventType.POST_DELETE).appendListener(deleteEventListener);
        registry.getEventListenerGroup(EventType.POST_UPDATE).appendListener(updateEventListener);
    }
}