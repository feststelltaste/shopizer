package com.salesmanager.core.business.services.shoppingcart;

import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.core.business.repositories.catalog.ProductInfoRepository;
import com.salesmanager.core.business.repositories.shoppingcart.ShoppingCartAttributeRepository;
import com.salesmanager.core.business.repositories.shoppingcart.ShoppingCartItemRepository;
import com.salesmanager.core.business.repositories.shoppingcart.ShoppingCartRepository;
import com.salesmanager.common.business.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.services.catalog.ProductInfoService;
import com.salesmanager.core.model.catalog.FinalPriceInfo;
import com.salesmanager.core.model.catalog.ProductAttributeInfo;
import com.salesmanager.core.model.catalog.ProductInfo;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.shipping.ShippingProduct;
import com.salesmanager.core.model.shoppingcart.ShoppingCart;
import com.salesmanager.core.model.shoppingcart.ShoppingCartAttributeItem;
import com.salesmanager.core.model.shoppingcart.ShoppingCartItem;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("shoppingCartService")
public class ShoppingCartServiceImpl extends SalesManagerEntityServiceImpl<Long, ShoppingCart>
		implements ShoppingCartService {

	private ShoppingCartRepository shoppingCartRepository;

	@Inject
	private ShoppingCartItemRepository shoppingCartItemRepository;
	
	@Inject
	private ShoppingCartAttributeRepository shoppingCartAttributeItemRepository;

	@Autowired
	private ProductInfoRepository productInfoRepository;

	@Autowired
	private ProductInfoService productInfoService;


	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartServiceImpl.class);

	@Inject
	public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository) {
		super(shoppingCartRepository);
		this.shoppingCartRepository = shoppingCartRepository;

	}

	/**
	 * Retrieve a {@link ShoppingCart} cart for a given customer
	 */
	@Override
	@Transactional
	public ShoppingCart getShoppingCart(final Customer customer) throws ServiceException {

		try {

			ShoppingCart shoppingCart = shoppingCartRepository.findByCustomer(customer.getId());
			getPopulatedShoppingCart(shoppingCart);
			if (shoppingCart != null && shoppingCart.isObsolete()) {
				delete(shoppingCart);
				return null;
			} else {
				return shoppingCart;
			}

		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	/**
	 * Save or update a {@link ShoppingCart} for a given customer
	 */
	@Override
	public void saveOrUpdate(final ShoppingCart shoppingCart) throws ServiceException {
		if (shoppingCart.getId() == null || shoppingCart.getId().longValue() == 0) {
			super.create(shoppingCart);
		} else {
			super.update(shoppingCart);
		}
	}

	/**
	 * Get a {@link ShoppingCart} for a given id and MerchantStore. Will update
	 * the shopping cart prices and items based on the actual inventory. This
	 * method will remove the shopping cart if no items are attached.
	 */
	@Override
	@Transactional
	public ShoppingCart getById(final Long id, final MerchantStore store) throws ServiceException {

		try {
			ShoppingCart shoppingCart = shoppingCartRepository.findById(store.getId(), id);
			if (shoppingCart == null) {
				return null;
			}
			getPopulatedShoppingCart(shoppingCart);

			if (shoppingCart.isObsolete()) {
				delete(shoppingCart);
				return null;
			} else {
				return shoppingCart;
			}

		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	/**
	 * Get a {@link ShoppingCart} for a given id. Will update the shopping cart
	 * prices and items based on the actual inventory. This method will remove
	 * the shopping cart if no items are attached.
	 */
	@Override
	@Transactional
	public ShoppingCart getById(final Long id) {

		try {
			ShoppingCart shoppingCart = shoppingCartRepository.findOne(id);
			if (shoppingCart == null) {
				return null;
			}
			getPopulatedShoppingCart(shoppingCart);

			if (shoppingCart.isObsolete()) {
				delete(shoppingCart);
				return null;
			} else {
				return shoppingCart;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Get a {@link ShoppingCart} for a given code. Will update the shopping
	 * cart prices and items based on the actual inventory. This method will
	 * remove the shopping cart if no items are attached.
	 */
	@Override
	@Transactional
	public ShoppingCart getByCode(final String code, final MerchantStore store) throws ServiceException {

		try {
			ShoppingCart shoppingCart = shoppingCartRepository.findByCode(store.getId(), code);
			if (shoppingCart == null) {
				return null;
			}
			getPopulatedShoppingCart(shoppingCart);

			if (shoppingCart.isObsolete()) {
				delete(shoppingCart);
				return null;
			} else {
				return shoppingCart;
			}

		} catch (javax.persistence.NoResultException nre) {
			return null;
		} catch (RuntimeException e) {
			throw new ServiceException(e);
		} catch (Exception ee) {
			throw new ServiceException(ee);
		} catch (Throwable t) {
			throw new ServiceException(t);
		}

	}

	@Override
	public void deleteCart(final ShoppingCart shoppingCart) throws ServiceException {
		ShoppingCart cart = this.getById(shoppingCart.getId());
		if (cart != null) {
			super.delete(cart);
		}
	}

	@Override
	@Transactional
	public ShoppingCart getByCustomer(final Customer customer) throws ServiceException {

		try {
			ShoppingCart shoppingCart = shoppingCartRepository.findByCustomer(customer.getId());
			if (shoppingCart == null) {
				return null;
			}
			return getPopulatedShoppingCart(shoppingCart);

		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Transactional(noRollbackFor = { org.springframework.dao.EmptyResultDataAccessException.class })
	private ShoppingCart getPopulatedShoppingCart(final ShoppingCart shoppingCart) throws Exception {

		try {

			boolean cartIsObsolete = false;
			if (shoppingCart != null) {

				Set<ShoppingCartItem> items = shoppingCart.getLineItems();
				if (items == null || items.size() == 0) {
					shoppingCart.setObsolete(true);
					return shoppingCart;

				}

				// Set<ShoppingCartItem> shoppingCartItems = new
				// HashSet<ShoppingCartItem>();
				for (ShoppingCartItem item : items) {
					LOGGER.debug("Populate item " + item.getId());
					getPopulatedItem(item);
					LOGGER.debug("Obsolete item ? " + item.isObsolete());
					if (item.isObsolete()) {
						cartIsObsolete = true;
					} 
				}

				// shoppingCart.setLineItems(shoppingCartItems);
				boolean refreshCart = false;
				Set<ShoppingCartItem> refreshedItems = new HashSet<ShoppingCartItem>();
				for (ShoppingCartItem item : items) {
/*					if (!item.isObsolete()) {
						refreshedItems.add(item);
					} else {
						refreshCart = true;
					}*/
					refreshedItems.add(item);
				}

				//if (refreshCart) {
					shoppingCart.setLineItems(refreshedItems);
				    update(shoppingCart);
				//}

				if (cartIsObsolete) {
					shoppingCart.setObsolete(true);
				}
				return shoppingCart;
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new ServiceException(e);
		}

		return shoppingCart;

	}

	@Override
	public ShoppingCartItem populateShoppingCartItem(final ProductInfo product) throws ServiceException {
		Validate.notNull(product, "Product should not be null");
		Validate.notNull(product.getMerchantStore(), "Product.merchantStore should not be null");

		ShoppingCartItem item = new ShoppingCartItem(product);

		// Set<ProductAttribute> productAttributes = product.getAttributes();
		// Set<ShoppingCartAttributeItem> attributesList = new
		// HashSet<ShoppingCartAttributeItem>();
		// if(!CollectionUtils.isEmpty(productAttributes)) {

		// for(ProductAttribute productAttribute : productAttributes) {
		// ShoppingCartAttributeItem attributeItem = new
		// ShoppingCartAttributeItem();
		// attributeItem.setShoppingCartItem(item);
		// attributeItem.setProductAttribute(productAttribute);
		// attributeItem.setProductAttributeId(productAttribute.getId());
		// attributesList.add(attributeItem);

		// }

		// item.setAttributes(attributesList);
		// }

		item.setProductVirtual(product.getAvailabilityInformation().getVirtual());

		// set item price
		FinalPriceInfo price = productInfoService.getProductFinalPrice(product.getId(), null);
		item.setItemPrice(BigDecimal.valueOf(price.getFinalPrice()));
		return item;

	}

	@Transactional
	private void getPopulatedItem(final ShoppingCartItem item) throws Exception {

		ProductInfo product = null;

		Long productId = item.getProductId();
		product = productInfoRepository.findOne(productId);

		if (product == null) {
			item.setObsolete(true);
			return;
		}

		item.setProduct(product);

		if (product.getAvailabilityInformation().getVirtual()) {
			item.setProductVirtual(true);
		}

		Set<ShoppingCartAttributeItem> cartAttributes = item.getAttributes();
		Set<ProductAttributeInfo> productAttributes = product.getAttributes();
		List<ProductAttributeInfo> attributesList = new ArrayList<>();//attributes maintained
		List<ShoppingCartAttributeItem> removeAttributesList = new ArrayList<ShoppingCartAttributeItem>();//attributes to remove
		//DELETE ORPHEANS MANUALLY
		if ( (productAttributes != null && productAttributes.size() > 0) || (cartAttributes != null && cartAttributes.size() > 0)) {
			for (ShoppingCartAttributeItem attribute : cartAttributes) {
				long attributeId = attribute.getProductAttributeId().longValue();
				boolean existingAttribute = false;
				for (ProductAttributeInfo productAttribute : productAttributes) {

					if (productAttribute.getId().longValue() == attributeId) {
						attribute.setProductAttribute(productAttribute);
						attributesList.add(productAttribute);
						existingAttribute = true;
						break;
					}
				}
				
				if(!existingAttribute) {
					removeAttributesList.add(attribute);
				}

			}
		}
		
		//cleanup orphean item
		if(CollectionUtils.isNotEmpty(removeAttributesList)) {
			for(ShoppingCartAttributeItem attr : removeAttributesList) {
				shoppingCartAttributeItemRepository.delete(attr);
			}
		}
		
		//cleanup detached attributes
		if(CollectionUtils.isEmpty(attributesList)) {
			item.setAttributes(null);
		}
		
		

		// set item price
		FinalPriceInfo price = productInfoService.getProductFinalPrice(product.getId(), attributesList);
		item.setItemPrice(BigDecimal.valueOf(price.getFinalPrice()));
		item.setFinalPrice(price);

		BigDecimal subTotal = item.getItemPrice().multiply(new BigDecimal(item.getQuantity().intValue()));
		item.setSubTotal(subTotal);

	}

	@Override
	public List<ShippingProduct> createShippingProduct(final ShoppingCart cart) throws ServiceException {
		/**
		 * Determines if products are virtual
		 */
		Set<ShoppingCartItem> items = cart.getLineItems();
		List<ShippingProduct> shippingProducts = null;
		for (ShoppingCartItem item : items) {
			ProductInfo product = item.getProduct();
			if (!product.getAvailabilityInformation().getVirtual() && product.getAvailabilityInformation().getShippable()) {
				if (shippingProducts == null) {
					shippingProducts = new ArrayList<ShippingProduct>();
				}
				ShippingProduct shippingProduct = new ShippingProduct(product);
				shippingProduct.setQuantity(item.getQuantity());
				shippingProduct.setFinalPrice(item.getFinalPrice());
				shippingProducts.add(shippingProduct);
			}
		}

		return shippingProducts;

	}

	@Override
	public boolean isFreeShoppingCart(final ShoppingCart cart) throws ServiceException {
		/**
		 * Determines if products are free
		 */
		Set<ShoppingCartItem> items = cart.getLineItems();
		for (ShoppingCartItem item : items) {
			ProductInfo product = item.getProduct();
			FinalPriceInfo finalPrice = productInfoService.getProductFinalPrice(product.getId(), null);
			if (finalPrice.getFinalPrice() > 0) {
				return false;
			}
		}

		return true;

	}

	@Override
	public boolean requiresShipping(final ShoppingCart cart) throws ServiceException {

		Validate.notNull(cart, "Shopping cart cannot be null");
		Validate.notNull(cart.getLineItems(), "ShoppingCart items cannot be null");
		boolean requiresShipping = false;
		for (ShoppingCartItem item : cart.getLineItems()) {
			ProductInfo product = item.getProduct();
			if (product.getAvailabilityInformation().getShippable()) {
				requiresShipping = true;
				break;
			}
		}

		return requiresShipping;

	}

	@Override
	public void removeShoppingCart(final ShoppingCart cart) throws ServiceException {
		shoppingCartRepository.delete(cart);
	}

	@Override
	public ShoppingCart mergeShoppingCarts(final ShoppingCart userShoppingModel, final ShoppingCart sessionCart,
			final MerchantStore store) throws Exception {
		if (sessionCart.getCustomerId() != null && sessionCart.getCustomerId() == userShoppingModel.getCustomerId()) {
			LOGGER.info("Session Shopping cart belongs to same logged in user");
			if (CollectionUtils.isNotEmpty(userShoppingModel.getLineItems())
					&& CollectionUtils.isNotEmpty(sessionCart.getLineItems())) {
				return userShoppingModel;
			}
		}

		LOGGER.info("Starting merging shopping carts");
		if (CollectionUtils.isNotEmpty(sessionCart.getLineItems())) {
			Set<ShoppingCartItem> shoppingCartItemsSet = getShoppingCartItems(sessionCart, store, userShoppingModel);
			boolean duplicateFound = false;
			if (CollectionUtils.isNotEmpty(shoppingCartItemsSet)) {
				for (ShoppingCartItem sessionShoppingCartItem : shoppingCartItemsSet) {
					if (CollectionUtils.isNotEmpty(userShoppingModel.getLineItems())) {
						for (ShoppingCartItem cartItem : userShoppingModel.getLineItems()) {
							if (cartItem.getProduct().getId().longValue() == sessionShoppingCartItem.getProduct()
									.getId().longValue()) {
								if (CollectionUtils.isNotEmpty(cartItem.getAttributes())) {
									if (!duplicateFound) {
										LOGGER.info("Dupliate item found..updating exisitng product quantity");
										cartItem.setQuantity(
												cartItem.getQuantity() + sessionShoppingCartItem.getQuantity());
										duplicateFound = true;
										break;
									}
								}
							}
						}
					}
					if (!duplicateFound) {
						LOGGER.info("New item found..adding item to Shopping cart");
						userShoppingModel.getLineItems().add(sessionShoppingCartItem);
					}
				}

			}

		}
		LOGGER.info("Shopping Cart merged successfully.....");
		saveOrUpdate(userShoppingModel);
		removeShoppingCart(sessionCart);

		return userShoppingModel;
	}

	private Set<ShoppingCartItem> getShoppingCartItems(final ShoppingCart sessionCart, final MerchantStore store,
			final ShoppingCart cartModel) throws Exception {

		Set<ShoppingCartItem> shoppingCartItemsSet = null;
		if (CollectionUtils.isNotEmpty(sessionCart.getLineItems())) {
			shoppingCartItemsSet = new HashSet<>();
			for (ShoppingCartItem shoppingCartItem : sessionCart.getLineItems()) {
				ProductInfo product = productInfoRepository.findOne(shoppingCartItem.getProductId());
				if (product == null) {
					throw new Exception("Item with id " + shoppingCartItem.getProductId() + " does not exist");
				}

				if (product.getMerchantStore().getId().intValue() != store.getId().intValue()) {
					throw new Exception("Item with id " + shoppingCartItem.getProductId()
							+ " does not belong to merchant " + store.getId());
				}

				ShoppingCartItem item = populateShoppingCartItem(product);
				item.setQuantity(shoppingCartItem.getQuantity());
				item.setShoppingCart(cartModel);

				List<ShoppingCartAttributeItem> cartAttributes = new ArrayList<ShoppingCartAttributeItem>(
						shoppingCartItem.getAttributes());
				if (CollectionUtils.isNotEmpty(cartAttributes)) {
					for (ShoppingCartAttributeItem shoppingCartAttributeItem : cartAttributes) {
						for (ProductAttributeInfo productAttribute : product.getAttributes()) {
							if (productAttribute.getId().equals(shoppingCartAttributeItem.getId())) {
								ShoppingCartAttributeItem attributeItem = new ShoppingCartAttributeItem(item,
										productAttribute);
								if (shoppingCartAttributeItem.getId() > 0) {
									attributeItem.setId(shoppingCartAttributeItem.getId());
								}
								item.addAttributes(attributeItem);
							}
						}
					}
				}

				shoppingCartItemsSet.add(item);
			}

		}
		return shoppingCartItemsSet;
	}

	@Override
	public boolean isFreeShoppingCart(List<ShoppingCartItem> items) throws ServiceException {
		ShoppingCart cart = new ShoppingCart();
		Set<ShoppingCartItem> cartItems = new HashSet<ShoppingCartItem>(items);
		cart.setLineItems(cartItems);
		return this.isFreeShoppingCart(cart);
	}

	@Override
	public void deleteShoppingCartItem(Long id) {
		shoppingCartItemRepository.delete(id);
	}

}
