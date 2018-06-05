package com.salesmanager.catalog.presentation.controller.admin.product;

import com.salesmanager.catalog.business.integration.core.service.LanguageInfoService;
import com.salesmanager.catalog.business.integration.core.service.MerchantStoreInfoService;
import com.salesmanager.catalog.business.service.product.ProductService;
import com.salesmanager.catalog.business.service.product.review.ProductReviewService;
import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.catalog.presentation.controller.admin.ControllerConstants;
import com.salesmanager.common.business.ajax.AjaxPageableResponse;
import com.salesmanager.common.business.ajax.AjaxResponse;
import com.salesmanager.catalog.model.product.Product;
import com.salesmanager.catalog.model.product.review.ProductReview;
import com.salesmanager.catalog.model.product.review.ProductReviewDescription;
import com.salesmanager.core.integration.language.LanguageDTO;
import com.salesmanager.core.integration.merchant.MerchantStoreDTO;
import com.salesmanager.common.presentation.model.admin.Menu;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.common.presentation.util.LabelUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
public class ProductReviewController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductReviewController.class);
	
	@Inject
	private ProductService productService;
	
	@Inject
	private ProductReviewService productReviewService;
	
	@Inject
	LabelUtils messages;

	@Autowired
	private MerchantStoreInfoService merchantStoreInfoService;

	@Autowired
	private LanguageInfoService languageInfoService;
	
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/reviews.html", method=RequestMethod.GET)
	public String displayProductReviews(@RequestParam("id") long productId,Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		setMenu(model, request);

		LanguageDTO languageDTO = (LanguageDTO) request.getAttribute("LANGUAGE_DTO");
		LanguageInfo language = this.languageInfoService.findbyCode(languageDTO.getCode());


		MerchantStoreDTO storeDTO = (MerchantStoreDTO) request.getAttribute(Constants.ADMIN_STORE_DTO);
		MerchantStoreInfo store = this.merchantStoreInfoService.findbyCode(storeDTO.getCode());
		Product product = productService.getById(productId);
		
		if(product==null) {
			return "redirect:/admin/products/products.html";
		}
		
		if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
			return "redirect:/admin/products/products.html";
		}
		
		
		model.addAttribute("product", product);
		
		return ControllerConstants.Tiles.Product.productReviews;

	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/reviews/paging.html", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> pageProductReviews(HttpServletRequest request, HttpServletResponse response) {

		String sProductId = request.getParameter("productId");
		MerchantStoreDTO storeDTO = (MerchantStoreDTO) request.getAttribute(Constants.ADMIN_STORE_DTO);
		MerchantStoreInfo store = this.merchantStoreInfoService.findbyCode(storeDTO.getCode());
		
		
		AjaxResponse resp = new AjaxResponse();
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		
		Long productId;
		Product product = null;
		
		try {
			productId = Long.parseLong(sProductId);
		} catch (Exception e) {
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorString("Product id is not valid");
			String returnString = resp.toJSONString();
			return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
		}

		
		try {

			product = productService.getById(productId);

			
			if(product==null) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorString("Product id is not valid");
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
			}
			
			if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorString("Product id is not valid");
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
			}


			LanguageDTO languageDTO = (LanguageDTO) request.getAttribute("LANGUAGE_DTO");
			LanguageInfo language = this.languageInfoService.findbyCode(languageDTO.getCode());

			
			List<ProductReview> reviews = productReviewService.getByProduct(product);
			


			for(ProductReview review : reviews) {
				Map entry = new HashMap();
				entry.put("reviewId", review.getId());
				entry.put("rating", review.getReviewRating().intValue());
				Set<ProductReviewDescription> descriptions = review.getDescriptions();
				String reviewDesc= "";
				if(!CollectionUtils.isEmpty(descriptions)) {
					reviewDesc = descriptions.iterator().next().getDescription();
				}
				//for(ProductReviewDescription description : descriptions){
				//	if(description.getLanguage().getCode().equals(language.getCode())) {
				//		reviewDesc = description.getDescription();
				//	}
				//}
				entry.put("description", reviewDesc);
				resp.addDataEntry(entry);
			}

			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_SUCCESS);
		
		} catch (Exception e) {
			LOGGER.error("Error while paging products", e);
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);


	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/reviews/remove.html", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> deleteProductReview(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String sReviewid = request.getParameter("reviewId");


		MerchantStoreDTO storeDTO = (MerchantStoreDTO) request.getAttribute(Constants.ADMIN_STORE_DTO);
		MerchantStoreInfo store = this.merchantStoreInfoService.findbyCode(storeDTO.getCode());
		
		AjaxResponse resp = new AjaxResponse();
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

		
		try {
			
			Long reviewId = Long.parseLong(sReviewid);

			
			ProductReview review = productReviewService.getById(reviewId);
			

			if(review==null || review.getProduct().getMerchantStore().getId().intValue()!=store.getId()) {

				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
			} 
			

			productReviewService.delete(review);
			
			
			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

		
		
		} catch (Exception e) {
			LOGGER.error("Error while deleting category", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
	}
	
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("catalogue", "catalogue");
		activeMenus.put("catalogue-products", "catalogue-products");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("catalogue");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}
	
	

}
