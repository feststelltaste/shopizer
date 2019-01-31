package com.salesmanager.catalog.api.event.product;

import com.salesmanager.catalog.api.dto.product.ProductOptionValueDTO;
import com.salesmanager.common.model.integration.CreateEvent;

public class ProductOptionValueCreateEvent extends CreateEvent<ProductOptionValueDTO> {

    public ProductOptionValueCreateEvent(ProductOptionValueDTO dto) {
        super(dto);
    }

}
