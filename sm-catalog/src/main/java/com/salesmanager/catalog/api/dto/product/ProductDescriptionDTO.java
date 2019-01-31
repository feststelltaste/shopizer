package com.salesmanager.catalog.api.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProductDescriptionDTO {

    private final Long id;
    private final String name;
    private final String seUrl;
    private final Long languageId;

}
