package com.salesmanager.catalog.api.event.product;

import com.salesmanager.catalog.api.dto.product.ProductOptionDTO;
import com.salesmanager.common.model.integration.CreateEvent;

public class ProductOptionCreateEvent extends CreateEvent<ProductOptionDTO> {

    public ProductOptionCreateEvent(ProductOptionDTO dto) {
        super(dto);
    }

}
