package com.salesmanager.core.integration.customer;

import com.salesmanager.core.integration.AbstractCoreCrudDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerDTO extends AbstractCoreCrudDTO {

    private Long id;

    private String nick;

    private String merchantStoreCode;

    private String firstName;

    private String lastName;

    private String defaultLanguageCode;

}
