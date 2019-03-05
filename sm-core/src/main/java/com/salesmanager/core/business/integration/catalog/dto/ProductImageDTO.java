package com.salesmanager.core.business.integration.catalog.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ProductImageDTO {

    private Long id;
    private String imageName;
    private boolean defaultImage;
    private String imageUrl;

}