package com.salesmanager.catalog.api.event.product;

import com.salesmanager.catalog.api.dto.product.ProductOptionValueDTO;
import com.salesmanager.common.model.integration.UpdateEvent;

public class ProductOptionValueUpdateEvent extends UpdateEvent<ProductOptionValueDTO> {

    public ProductOptionValueUpdateEvent(ProductOptionValueDTO dto) {
        super(dto);
    }

}
