package com.farmers.ownfarmer.ui.MyServices.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyServicesDataList {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("my_services")
    @Expose
    private List<MyServicesDataModel> myServices = null;

    public MyServicesDataList(Integer status, String message, List<MyServicesDataModel> myServices) {
        this.status = status;
        this.message = message;
        this.myServices = myServices;
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

    public List<MyServicesDataModel> getMyServices() {
        return myServices;
    }

    public void setMyServices(List<MyServicesDataModel> myServices) {
        this.myServices = myServices;
    }
}
