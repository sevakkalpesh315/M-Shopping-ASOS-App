package com.asos.model.model;

import java.io.Serializable;
/**
 * @author Kalpesh Sevak
 * @version 2015.0606
 * @since 1.0
 */
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	private String basePrice = "", brand = "", currentPrice,
			hasMoreColors = "", isInSet = "", previousPrice = "",
			productID = "", productImgURL = "", rpp = "", title = "",
			isFavourite = "", isAddedToShoppingCart = "";

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

	public String getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(String currentPrice) {
		this.currentPrice = currentPrice;
	}

	public String getHasMoreColors() {
		return hasMoreColors;
	}

	public void setHasMoreColors(String hasMoreColors) {
		this.hasMoreColors = hasMoreColors;
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

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public String getProductImgURL() {
		return productImgURL;
	}

	public void setProductImgURL(String productImgURL) {
		this.productImgURL = productImgURL;
	}

	public String getRpp() {
		return rpp;
	}

	public void setRpp(String rpp) {
		this.rpp = rpp;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIsFavourite() {
		return isFavourite;
	}

	public void setIsFavourite(String isFavourite) {
		this.isFavourite = isFavourite;
	}

	public String getIsAddedToShoppingCart() {
		return isAddedToShoppingCart;
	}

	public void setIsAddedToShoppingCart(String isAddedToShoppingCart) {
		this.isAddedToShoppingCart = isAddedToShoppingCart;
	}

}
