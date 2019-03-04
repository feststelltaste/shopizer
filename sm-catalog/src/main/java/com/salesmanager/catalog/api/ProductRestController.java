package com.salesmanager.catalog.api;

import com.salesmanager.catalog.api.dto.product.AvailabilityInformationDTO;
import com.salesmanager.catalog.api.dto.product.DimensionDTO;
import com.salesmanager.catalog.api.dto.product.ProductAttributeDTO;
import com.salesmanager.catalog.api.dto.product.ProductDescriptionDTO;
import com.salesmanager.catalog.business.service.product.ProductService;
import com.salesmanager.catalog.model.product.Product;
import com.salesmanager.catalog.model.product.attribute.ProductAttribute;
import com.salesmanager.catalog.model.product.description.ProductDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(path = "/catalog/product")
public class ProductRestController {

    private final ProductService productService;

    @Autowired
    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(path = "/{productId}/dimension", method = RequestMethod.GET)
    public ResponseEntity<?> getDimensionForProduct(@PathVariable("productId") Long productId) {
        Product product = this.productService.getById(productId);
        if (product != null) {
            DimensionDTO dimensionDTO =  new DimensionDTO(
                    product.getProductWidth() != null ? product.getProductWidth().doubleValue() : null,
                    product.getProductLength() != null ? product.getProductLength().doubleValue() : null,
                    product.getProductHeight() != null ? product.getProductHeight().doubleValue() : null,
                    product.getProductWeight() != null ? product.getProductWeight().doubleValue() : null
            );
            return ResponseEntity.ok(dimensionDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(path = "/{productId}/shipping", method = RequestMethod.GET)
    public ResponseEntity<?> getProductAvailabilityInformation(@PathVariable("productId") Long productId) {
        Product product = this.productService.getById(productId);
        if (product != null) {
            AvailabilityInformationDTO availabilityInformationDTO = new AvailabilityInformationDTO(
                    product.isAvailable(),
                    product.isProductShipeable(),
                    product.isProductVirtual());
            return ResponseEntity.ok(availabilityInformationDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(path = "/{productId}/descriptions", method = RequestMethod.GET)
    public ResponseEntity<?> getProductDescriptions(@PathVariable("productId") Long productId) {
        Product product = this.productService.getById(productId);
        if (product != null) {
            Set<ProductDescriptionDTO> descriptions= new HashSet<>();
            if (product.getDescriptions() != null) {
                for (ProductDescription productDescription : product.getDescriptions()) {
                    descriptions.add(new ProductDescriptionDTO(
                            productDescription.getId(),
                            productDescription.getName(),
                            productDescription.getSeUrl(),
                            productDescription.getLanguage() != null ? productDescription.getLanguage().getId().longValue() : null
                    ));
                }
            }
            return ResponseEntity.ok(descriptions);
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(path = "/{productId}/merchant", method = RequestMethod.GET)
    public ResponseEntity<?> getProductMerchant(@PathVariable("productId") Long productId) {
        Product product = this.productService.getById(productId);
        if (product != null) {
            return ResponseEntity.ok(product.getMerchantStore() != null ? product.getMerchantStore().getId() : null);
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(path = "/{productId}/tax-class", method = RequestMethod.GET)
    public ResponseEntity<?> getProductTaxClass(@PathVariable("productId") Long productId) {
        Product product = this.productService.getById(productId);
        if (product != null) {
            return ResponseEntity.ok(product.getTaxClass() != null ? product.getTaxClass().getId() : null);
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(path = "/{productId}/attributes", method = RequestMethod.GET)
    public ResponseEntity<?> getProductAttributes(@PathVariable("productId") Long productId) {
        Product product = this.productService.getById(productId);
        if (product != null) {
            if (product.getAttributes() != null) {
                Set<ProductAttributeDTO> attributes = new HashSet<>();
                for (ProductAttribute productAttribute : product.getAttributes()) {
                    attributes.add(new ProductAttributeDTO(
                            productAttribute.getId(),
                            productAttribute.getProductAttributePrice() != null ? productAttribute.getProductAttributePrice().doubleValue() : null,
                            productAttribute.getProductAttributeIsFree(),
                            productAttribute.getProductAttributeWeight() != null ? productAttribute.getProductAttributeWeight().doubleValue() : null,
                            productAttribute.getProductOption().getId(),
                            productAttribute.getProductOptionValue().getId()));
                }
                return ResponseEntity.ok(attributes);
            }
        }
        return ResponseEntity.notFound().build();
    }
}
