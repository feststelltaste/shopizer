package com.salesmanager.catalog.api.dto.product;

import com.salesmanager.catalog.api.dto.AbstractCatalogCrudDTO;
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
    public static class ProductOptionValueDescriptionDTO {

        private Long id;

        private String name;

        private Integer languageId;

    }

}
