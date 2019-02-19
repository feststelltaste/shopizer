package com.salesmanager.catalog.api.dto.product;

import com.salesmanager.catalog.api.dto.AbstractCatalogCrudDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductOptionDTO extends AbstractCatalogCrudDTO {

    private Long id;

    private String code;

    private List<ProductOptionDescriptionDTO> descriptions;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class ProductOptionDescriptionDTO {

        private Long id;

        private String name;

        private Integer languageId;

    }

}
