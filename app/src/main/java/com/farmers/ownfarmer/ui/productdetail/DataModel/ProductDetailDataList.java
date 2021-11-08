package com.farmers.ownfarmer.ui.productdetail.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDetailDataList {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("product")
    @Expose
    private ProductDetailDataModel product;

    public ProductDetailDataList(Integer status, String message, ProductDetailDataModel product) {
        this.status = status;
        this.message = message;
        this.product = product;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ProductDetailDataModel getProduct() {
        return product;
    }

    public void setProduct(ProductDetailDataModel product) {
        this.product = product;
    }
}
