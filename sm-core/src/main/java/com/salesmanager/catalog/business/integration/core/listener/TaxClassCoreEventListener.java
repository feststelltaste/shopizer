package com.salesmanager.catalog.business.integration.core.listener;

import com.salesmanager.catalog.business.integration.core.repository.TaxClassInfoRepository;
import com.salesmanager.catalog.business.integration.core.service.MerchantStoreInfoService;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.catalog.model.integration.core.TaxClassInfo;
import com.salesmanager.common.model.integration.CreateEvent;
import com.salesmanager.common.model.integration.DeleteEvent;
import com.salesmanager.common.model.integration.UpdateEvent;
import com.salesmanager.core.integration.tax.TaxClassDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class TaxClassCoreEventListener {

    private TaxClassInfoRepository taxClassInfoRepository;

    private MerchantStoreInfoService merchantStoreInfoService;

    @Autowired
    public TaxClassCoreEventListener(TaxClassInfoRepository taxClassInfoRepository, MerchantStoreInfoService merchantStoreInfoService) {
        this.taxClassInfoRepository = taxClassInfoRepository;
        this.merchantStoreInfoService = merchantStoreInfoService;
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleTaxClassCreateEvent(CreateEvent<TaxClassDTO> event) {
        TaxClassDTO taxClassDTO = event.getDto();
        if (taxClassDTO != null) {
            MerchantStoreInfo merchantStoreInfo = this.merchantStoreInfoService.findbyCode(taxClassDTO.getMerchantStoreCode());
            TaxClassInfo taxClass = new TaxClassInfo(
                    taxClassDTO.getId(),
                    taxClassDTO.getCode(),
                    merchantStoreInfo
            );
            this.taxClassInfoRepository.save(taxClass);
        }
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleTaxClassDeleteEvent(DeleteEvent<TaxClassDTO> event) {
        TaxClassDTO taxClassDTO = event.getDto();
        if (taxClassDTO != null) {
            this.taxClassInfoRepository.delete(taxClassDTO.getId());
        }
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handletaxClassUpdateEvent(UpdateEvent<TaxClassDTO> event) {
        TaxClassDTO taxClassDTO = event.getDto();
        if (taxClassDTO != null) {
            MerchantStoreInfo merchantStoreInfo = this.merchantStoreInfoService.findbyCode(taxClassDTO.getMerchantStoreCode());
            TaxClassInfo taxClass = new TaxClassInfo(
                    taxClassDTO.getId(),
                    taxClassDTO.getCode(),
                    merchantStoreInfo
            );
            this.taxClassInfoRepository.save(taxClass);
        }
    }

}
