package com.salesmanager.core.integration.language;

import com.salesmanager.common.model.integration.DeleteEvent;

public class LanguageDeleteEvent extends DeleteEvent<LanguageDTO> {

    public LanguageDeleteEvent(LanguageDTO dto) {
        super(dto);
    }

}
