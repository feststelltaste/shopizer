package com.salesmanager.catalog.api.event.product;

import com.salesmanager.catalog.api.dto.product.ProductOptionValueDTO;
import com.salesmanager.common.model.integration.DeleteEvent;

public class ProductOptionValueDeleteEvent extends DeleteEvent<ProductOptionValueDTO> {

    public ProductOptionValueDeleteEvent(ProductOptionValueDTO dto) {
        super(dto);
    }

}
