package com.salesmanager.catalog.api.dto.product;

import com.salesmanager.catalog.api.dto.AbstractCatalogCrudDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ProductOptionDTO extends AbstractCatalogCrudDTO {

    private Long id;

    private String code;

    private List<ProductOptionDescriptionDTO> descriptions;

    @AllArgsConstructor
    @Getter
    public static class ProductOptionDescriptionDTO {

        private Long id;

        private String name;

        private Integer languageId;

    }

}
