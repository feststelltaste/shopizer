package com.salesmanager.core.business.integration.catalog.listener;

import com.salesmanager.catalog.api.dto.product.ProductDTO;
import com.salesmanager.common.model.integration.CreateEvent;
import com.salesmanager.common.model.integration.DeleteEvent;
import com.salesmanager.common.model.integration.UpdateEvent;
import com.salesmanager.core.business.services.catalog.ProductInfoService;
import com.salesmanager.core.model.catalog.ProductInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ProductCatalogEventListener {

    private final ProductInfoService productInfoService;

    @Autowired
    public ProductCatalogEventListener(ProductInfoService productInfoService) {
        this.productInfoService = productInfoService;
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleProductCreateEvent(CreateEvent<ProductDTO> event) {
        ProductDTO productDTO = event.getDto();
        if (productDTO != null) {
            ProductInfo productInfo = new ProductInfo(
                    productDTO.getId(),
                    productDTO.getSku(),
                    productDTO.getName(),
                    productDTO.getManufacturerCode());
            this.productInfoService.save(productInfo);
        }
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleProductDeleteEvent(DeleteEvent<ProductDTO> event) {
        ProductDTO productDTO = event.getDto();
        if (productDTO != null) {
            this.productInfoService.delete(productDTO.getId());
        }
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleProductUpdateEvent(UpdateEvent<ProductDTO> event) {
        ProductDTO productDTO = event.getDto();
        if (productDTO != null) {
            ProductInfo productInfo = new ProductInfo(
                    productDTO.getId(),
                    productDTO.getSku(),
                    productDTO.getName(),
                    productDTO.getManufacturerCode());
            this.productInfoService.save(productInfo);
        }
    }

}
