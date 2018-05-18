package com.salesmanager.common.model.integration;

public class CreateEvent<T extends AbstractDTO> extends AbstractEvent<T> {

    public CreateEvent(T dto) {
        super(dto);
    }
}
