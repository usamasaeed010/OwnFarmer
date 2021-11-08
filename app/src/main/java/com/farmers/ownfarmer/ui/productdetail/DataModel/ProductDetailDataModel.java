package com.farmers.ownfarmer.ui.productdetail.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDetailDataModel {

    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_unit")
    @Expose
    private String productUnit;
    @SerializedName("product_location")
    @Expose
    private String productLocation;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("product_description")
    @Expose
    private String productDescription;
    @SerializedName("related_user")
    @Expose
    private String relatedUser;
    @SerializedName("product_image")
    @Expose
    private String productImage;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("user_phone")
    @Expose
    private String userPhone;
    @SerializedName("user_id")
    @Expose
    private String userID;

    public ProductDetailDataModel(String productId, String categoryId, String productName, String productUnit, String productLocation, String productPrice, String createdAt, String updatedAt, String productDescription, String relatedUser, String productImage, String userName, String userImage, String userPhone, String userID) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.productName = productName;
        this.productUnit = productUnit;
        this.productLocation = productLocation;
        this.productPrice = productPrice;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.productDescription = productDescription;
        this.relatedUser = relatedUser;
        this.productImage = productImage;
        this.userName = userName;
        this.userImage = userImage;
        this.userPhone = userPhone;
        this.userID = userID;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public String getProductLocation() {
        return productLocation;
    }

    public void setProductLocation(String productLocation) {
        this.productLocation = productLocation;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getRelatedUser() {
        return relatedUser;
    }

    public void setRelatedUser(String relatedUser) {
        this.relatedUser = relatedUser;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
