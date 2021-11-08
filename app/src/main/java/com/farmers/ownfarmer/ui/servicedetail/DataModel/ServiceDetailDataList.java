package com.farmers.ownfarmer.ui.servicedetail.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceDetailDataList {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("service")
    @Expose
    private ServiceDetailDataModel service;

    public ServiceDetailDataList(Integer status, String message, ServiceDetailDataModel service) {
        this.status = status;
        this.message = message;
        this.service = service;
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

    public ServiceDetailDataModel getService() {
        return service;
    }

    public void setService(ServiceDetailDataModel service) {
        this.service = service;
    }
}
