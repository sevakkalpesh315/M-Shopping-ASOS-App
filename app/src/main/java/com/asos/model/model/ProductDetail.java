package com.asos.model.model;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Kalpesh Sevak
 * @version 2015.0606
 * @since 1.0
 */
public class ProductDetail {

	private String basePrice, brand, colour, currentPrice, inStock, isInSet,
			previousPrice, priceType, productId, rrp, size, sku, title,
			additionalInfo;

	private List<String> imgURLS = new ArrayList<String>();

	public String getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(String basePrice) {
		this.basePrice = basePrice;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	public String getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(String currentPrice) {
		this.currentPrice = currentPrice;
	}

	public String getInStock() {
		return inStock;
	}

	public void setInStock(String inStock) {
		this.inStock = inStock;
	}

	public String getIsInSet() {
		return isInSet;
	}

	public void setIsInSet(String isInSet) {
		this.isInSet = isInSet;
	}

	public String getPreviousPrice() {
		return previousPrice;
	}

	public void setPreviousPrice(String previousPrice) {
		this.previousPrice = previousPrice;
	}

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getRrp() {
		return rrp;
	}

	public void setRrp(String rrp) {
		this.rrp = rrp;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public List<String> getImgURLS() {
		return imgURLS;
	}

	public void setImgURLS(List<String> imgURLS) {
		this.imgURLS = imgURLS;
	}
}
