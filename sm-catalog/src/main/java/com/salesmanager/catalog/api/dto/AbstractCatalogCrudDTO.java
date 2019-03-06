package com.salesmanager.catalog.api.dto;

import com.salesmanager.common.model.integration.AbstractCrudDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class AbstractCatalogCrudDTO extends AbstractCrudDTO implements AbstractCatalogDTO {

}