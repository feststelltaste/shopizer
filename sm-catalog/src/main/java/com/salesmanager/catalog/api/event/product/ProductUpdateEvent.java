package com.salesmanager.catalog.api.event.product;

import com.salesmanager.catalog.api.dto.product.ProductDTO;
import com.salesmanager.common.model.integration.UpdateEvent;

public class ProductUpdateEvent extends UpdateEvent<ProductDTO> {

    public ProductUpdateEvent(ProductDTO dto) {
        super(dto);
    }

}
