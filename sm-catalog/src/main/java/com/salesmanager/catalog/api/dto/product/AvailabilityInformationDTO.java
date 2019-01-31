package com.salesmanager.catalog.api.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AvailabilityInformationDTO {

    private boolean available;
    private boolean shippable;
    private boolean virtual;

}
