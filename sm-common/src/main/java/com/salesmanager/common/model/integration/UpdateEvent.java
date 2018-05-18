package com.salesmanager.common.model.integration;

public class UpdateEvent<T extends AbstractDTO> extends AbstractEvent<T> {

    public UpdateEvent(T dto) {
        super(dto);
    }

}
