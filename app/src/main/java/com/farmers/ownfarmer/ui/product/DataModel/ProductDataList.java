package com.farmers.ownfarmer.ui.product.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductDataList {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("products")
    @Expose
    private List<ProductDataModel> products = null;

    public ProductDataList(Integer status, String message, List<ProductDataModel> products) {
        this.status = status;
        this.message = message;
        this.products = products;
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

    public List<ProductDataModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDataModel> products) {
        this.products = products;
    }
}
