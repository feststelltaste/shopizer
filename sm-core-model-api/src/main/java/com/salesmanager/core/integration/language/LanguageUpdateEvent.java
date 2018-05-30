package com.salesmanager.core.integration.language;

import com.salesmanager.common.model.integration.UpdateEvent;

public class LanguageUpdateEvent extends UpdateEvent<LanguageDTO> {

    public LanguageUpdateEvent(LanguageDTO dto) {
        super(dto);
    }

}
