package com.farmers.ownfarmer.ui.MyProducts.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyProductsDataList {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("my_products")
    @Expose
    private List<MyProductsDataModel> myProducts = null;

    public MyProductsDataList(Integer status, String message, List<MyProductsDataModel> myProducts) {
        this.status = status;
        this.message = message;
        this.myProducts = myProducts;
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

    public List<MyProductsDataModel> getMyProducts() {
        return myProducts;
    }

    public void setMyProducts(List<MyProductsDataModel> myProducts) {
        this.myProducts = myProducts;
    }
}
