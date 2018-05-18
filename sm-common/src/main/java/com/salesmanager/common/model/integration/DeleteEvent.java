package com.salesmanager.common.model.integration;

public class DeleteEvent<T extends AbstractDTO> extends AbstractEvent<T> {

    public DeleteEvent(T dto) {
        super(dto);
    }
}
