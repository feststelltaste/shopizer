package com.salesmanager.shop.populator.order;

import com.salesmanager.catalog.api.ProductApi;
import com.salesmanager.catalog.api.ProductAttributeApi;
import com.salesmanager.catalog.api.ProductAttributeApi;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.Validate;

import com.salesmanager.catalog.model.product.Product;
import com.salesmanager.catalog.model.product.attribute.ProductAttribute;
import com.salesmanager.common.business.exception.ConversionException;
import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.core.model.shoppingcart.ShoppingCartItem;
import com.salesmanager.core.business.services.shoppingcart.ShoppingCartService;
import com.salesmanager.core.business.utils.AbstractDataPopulator;
import com.salesmanager.shop.model.order.PersistableOrderProduct;

public class ShoppingCartItemPopulator extends
		AbstractDataPopulator<PersistableOrderProduct, ShoppingCartItem> {
	

	private ProductApi productApi;
	@Getter @Setter
	private ProductAttributeApi productAttributeApi;
	private ShoppingCartService shoppingCartService;

	@Override
	public ShoppingCartItem populate(PersistableOrderProduct source,
			ShoppingCartItem target, MerchantStore store, Language language)
			throws ConversionException {
		Validate.notNull(productApi, "Requires to set productApi");
		Validate.notNull(productAttributeApi, "Requires to set productAttributeApi");
		Validate.notNull(shoppingCartService, "Requires to set shoppingCartService");
		
		Product product = productApi.getById(source.getProduct().getId());
		if(source.getAttributes()!=null) {

			for(com.salesmanager.shop.model.catalog.product.attribute.ProductAttribute attr : source.getAttributes()) {
				ProductAttribute attribute = productAttributeApi.getById(attr.getId());
				if(attribute==null) {
					throw new ConversionException("ProductAttribute with id " + attr.getId() + " is null");
				}
				if(attribute.getProduct().getId().longValue()!=source.getProduct().getId().longValue()) {
					throw new ConversionException("ProductAttribute with id " + attr.getId() + " is not assigned to Product id " + source.getProduct().getId());
				}
				product.getAttributes().add(attribute);
			}
		}
		
		try {
			return shoppingCartService.populateShoppingCartItem(product);
		} catch (ServiceException e) {
			throw new ConversionException(e);
		}
		
	}

	@Override
	protected ShoppingCartItem createTarget() {
		// TODO Auto-generated method stub
		return null;
	}
	public ProductApi getProductApi() {
		return productApi;
	}

	public void setProductApi(ProductApi productApi) {
		this.productApi = productApi;
	}

	public void setShoppingCartService(ShoppingCartService shoppingCartService) {
		this.shoppingCartService = shoppingCartService;
	}

	public ShoppingCartService getShoppingCartService() {
		return shoppingCartService;
	}

}
