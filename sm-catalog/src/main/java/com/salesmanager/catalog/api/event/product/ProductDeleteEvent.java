package com.salesmanager.catalog.api.event.product;

import com.salesmanager.catalog.api.dto.product.ProductDTO;
import com.salesmanager.common.model.integration.DeleteEvent;

public class ProductDeleteEvent extends DeleteEvent<ProductDTO> {

    public ProductDeleteEvent(ProductDTO dto) {
        super(dto);
    }

}
