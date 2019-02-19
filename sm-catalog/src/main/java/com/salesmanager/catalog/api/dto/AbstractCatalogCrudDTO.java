package com.salesmanager.catalog.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class AbstractCatalogCrudDTO extends AbstractCatalogDTO {

    private EventType eventType;

    public enum EventType {
        CREATE,
        UPDATE,
        DELETE
    }

}