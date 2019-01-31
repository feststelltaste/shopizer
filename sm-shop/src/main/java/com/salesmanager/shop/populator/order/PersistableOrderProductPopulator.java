package com.salesmanager.shop.populator.order;

import com.salesmanager.catalog.api.DigitalProductApi;
import com.salesmanager.catalog.api.ProductApi;
import com.salesmanager.catalog.api.ProductAttributeApi;
import com.salesmanager.common.business.exception.ConversionException;
import com.salesmanager.core.business.utils.AbstractDataPopulator;
import com.salesmanager.catalog.model.product.Product;
import com.salesmanager.catalog.model.product.file.DigitalProduct;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.order.orderproduct.OrderProduct;
import com.salesmanager.core.model.order.orderproduct.OrderProductAttribute;
import com.salesmanager.core.model.order.orderproduct.OrderProductDownload;
import com.salesmanager.core.model.order.orderproduct.OrderProductPrice;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.shop.constants.ApplicationConstants;
import com.salesmanager.shop.model.catalog.product.attribute.ProductAttribute;
import com.salesmanager.shop.model.order.PersistableOrderProduct;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.Validate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PersistableOrderProductPopulator extends
		AbstractDataPopulator<PersistableOrderProduct, OrderProduct> {
	
	private ProductApi productApi;
	private DigitalProductApi digitalProductApi;
	private ProductAttributeApi productAttributeApi;

	public ProductAttributeApi getProductAttributeApi() {
		return productAttributeApi;
	}

	public void setProductAttributeApi(ProductAttributeApi productAttributeApi) {
		this.productAttributeApi = productAttributeApi;
	}

	public DigitalProductApi getDigitalProductApi() {
		return digitalProductApi;
	}

	public void setDigitalProductApi(DigitalProductApi digitalProductApi) {
		this.digitalProductApi = digitalProductApi;
	}

	/**
	 * Converts a ShoppingCartItem carried in the ShoppingCart to an OrderProduct
	 * that will be saved in the system
	 */
	@Override
	public OrderProduct populate(PersistableOrderProduct source, OrderProduct target,
			MerchantStore store, Language language) throws ConversionException {
		
		Validate.notNull(productApi,"productApi must be set");
		Validate.notNull(digitalProductApi,"digitalProductApi must be set");
		Validate.notNull(productAttributeApi,"productAttributeApi must be set");

		
		try {
			Product modelProduct = productApi.getById(source.getProduct().getId());
			if(modelProduct==null) {
				throw new ConversionException("Cannot get product with id (productId) " + source.getProduct().getId());
			}
			
			if(modelProduct.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
				throw new ConversionException("Invalid product id " + source.getProduct().getId());
			}

			String digitalProductFileName = digitalProductApi.getFileNameByProduct(store.toDTO(), modelProduct);

			if(digitalProductFileName != null) {
				OrderProductDownload orderProductDownload = new OrderProductDownload();
				orderProductDownload.setOrderProductFilename(digitalProductFileName);
				orderProductDownload.setOrderProduct(target);
				orderProductDownload.setDownloadCount(0);
				orderProductDownload.setMaxdays(ApplicationConstants.MAX_DOWNLOAD_DAYS);
				target.getDownloads().add(orderProductDownload);
			}

			target.setOneTimeCharge(source.getPrice());
			target.setProductQuantity(source.getOrderedQuantity());
			target.setSku(source.getProduct().getSku());
			
			OrderProductPrice orderProductPrice = new OrderProductPrice();
			orderProductPrice.setDefaultPrice(true);
			orderProductPrice.setProductPrice(source.getPrice());
			orderProductPrice.setOrderProduct(target);
			

			
			Set<OrderProductPrice> prices = new HashSet<OrderProductPrice>();
			prices.add(orderProductPrice);

			/** DO NOT SUPPORT MUTIPLE PRICES **/
/*			//Other prices
			List<FinalPrice> otherPrices = finalPrice.getAdditionalPrices();
			if(otherPrices!=null) {
				for(FinalPrice otherPrice : otherPrices) {
					OrderProductPrice other = orderProductPrice(otherPrice);
					other.setOrderProduct(target);
					prices.add(other);
				}
			}*/
			
			target.setPrices(prices);
			
			//OrderProductAttribute
			List<ProductAttribute> attributeItems = source.getAttributes();
			if(!CollectionUtils.isEmpty(attributeItems)) {
				Set<OrderProductAttribute> attributes = new HashSet<OrderProductAttribute>();
				for(ProductAttribute attribute : attributeItems) {
					OrderProductAttribute orderProductAttribute = new OrderProductAttribute();
					orderProductAttribute.setOrderProduct(target);
					Long id = attribute.getId();
					com.salesmanager.catalog.model.product.attribute.ProductAttribute attr = productAttributeApi.getById(id);
					if(attr==null) {
						throw new ConversionException("Attribute id " + id + " does not exists");
					}
					
					if(attr.getProduct().getMerchantStore().getId().intValue()!=store.getId().intValue()) {
						throw new ConversionException("Attribute id " + id + " invalid for this store");
					}
					
					orderProductAttribute.setProductAttributeIsFree(attr.getProductAttributeIsFree());
					orderProductAttribute.setProductAttributeName(attr.getProductOption().getDescriptionsSettoList().get(0).getName());
					orderProductAttribute.setProductAttributeValueName(attr.getProductOptionValue().getDescriptionsSettoList().get(0).getName());
					orderProductAttribute.setProductAttributePrice(attr.getProductAttributePrice());
					orderProductAttribute.setProductAttributeWeight(attr.getProductAttributeWeight());
					orderProductAttribute.setProductOptionId(attr.getProductOption().getId());
					orderProductAttribute.setProductOptionValueId(attr.getProductOptionValue().getId());
					attributes.add(orderProductAttribute);
				}
				target.setOrderAttributes(attributes);
			}

			
		} catch (Exception e) {
			throw new ConversionException(e);
		}
		
		
		return target;
	}

	@Override
	protected OrderProduct createTarget() {
		return null;
	}

	public ProductApi getProductApi() {
		return productApi;
	}

	public void setProductApi(ProductApi productApi) {
		this.productApi = productApi;
	}
}
