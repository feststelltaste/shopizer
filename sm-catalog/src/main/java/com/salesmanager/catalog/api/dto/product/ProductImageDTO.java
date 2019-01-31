package com.salesmanager.catalog.api.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProductImageDTO {

    private final Long id;
    private final String imageName;
    private final boolean defaultImage;
    private final String imageUrl;

}
