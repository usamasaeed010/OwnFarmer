package com.farmers.ownfarmer.ui.services.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServicesDataList {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("services")
    @Expose
    private List<ServicesDataModel> services = null;

    public ServicesDataList(Integer status, String message, List<ServicesDataModel> services) {
        this.status = status;
        this.message = message;
        this.services = services;
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

    public List<ServicesDataModel> getServices() {
        return services;
    }

    public void setServices(List<ServicesDataModel> services) {
        this.services = services;
    }
}
