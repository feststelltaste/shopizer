package com.salesmanager.catalog.api.event.product;

import com.salesmanager.catalog.api.dto.product.ProductOptionDTO;
import com.salesmanager.common.model.integration.UpdateEvent;

public class ProductOptionUpdateEvent extends UpdateEvent<ProductOptionDTO> {

    public ProductOptionUpdateEvent(ProductOptionDTO dto) {
        super(dto);
    }

}
