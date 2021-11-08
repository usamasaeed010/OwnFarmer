package com.farmers.ownfarmer.ui.home.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeDataList {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("products_home")
    @Expose
    private List<HomeProductsDataModel> productsHome = null;
    @SerializedName("services_home")
    @Expose
    private List<HomeServiceDataModel> servicesHome = null;

    public HomeDataList(Integer status, String message, List<HomeProductsDataModel> productsHome, List<HomeServiceDataModel> servicesHome) {
        this.status = status;
        this.message = message;
        this.productsHome = productsHome;
        this.servicesHome = servicesHome;
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

    public List<HomeProductsDataModel> getProductsHome() {
        return productsHome;
    }

    public void setProductsHome(List<HomeProductsDataModel> productsHome) {
        this.productsHome = productsHome;
    }

    public List<HomeServiceDataModel> getServicesHome() {
        return servicesHome;
    }

    public void setServicesHome(List<HomeServiceDataModel> servicesHome) {
        this.servicesHome = servicesHome;
    }
}
