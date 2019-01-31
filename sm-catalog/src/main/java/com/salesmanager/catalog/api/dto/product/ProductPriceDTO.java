package com.salesmanager.catalog.api.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public class ProductPriceDTO {

    private String code;
    private String productPriceType;
    private Boolean defaultPrice;
    private String name;
    private Double productPriceSpecialAmount;
    private Date productPriceSpecialStartDate;
    private Date productPriceSpecialEndDate;

}
