package com.salesmanager.catalog.api.event.product;

import com.salesmanager.catalog.api.dto.product.ProductDTO;
import com.salesmanager.common.model.integration.CreateEvent;

public class ProductCreateEvent extends CreateEvent<ProductDTO> {

    public ProductCreateEvent(ProductDTO dto) {
        super(dto);
    }

}
