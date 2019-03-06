package com.salesmanager.catalog.api.dto.product;

import com.salesmanager.catalog.api.dto.AbstractCatalogCrudDTO;
import com.salesmanager.catalog.api.dto.AbstractCatalogDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ProductOptionValueDTO extends AbstractCatalogCrudDTO {

    private Long id;

    private String code;

    private List<ProductOptionValueDescriptionDTO> descriptions;

    @AllArgsConstructor
    @Getter
    public static class ProductOptionValueDescriptionDTO implements AbstractCatalogDTO {

        private Long id;

        private String name;

        private Integer languageId;

    }

}
