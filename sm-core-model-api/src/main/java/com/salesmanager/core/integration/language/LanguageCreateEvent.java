package com.salesmanager.core.integration.language;

import com.salesmanager.common.model.integration.CreateEvent;

public class LanguageCreateEvent extends CreateEvent<LanguageDTO> {

    public LanguageCreateEvent(LanguageDTO dto) {
        super(dto);
    }

}
