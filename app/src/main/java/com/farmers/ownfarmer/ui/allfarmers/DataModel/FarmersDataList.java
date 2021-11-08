package com.farmers.ownfarmer.ui.allfarmers.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FarmersDataList {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("farmers")
    @Expose
    private List<FarmersDataModel> farmers = null;

    public FarmersDataList(Integer status, String message, List<FarmersDataModel> farmers) {
        this.status = status;
        this.message = message;
        this.farmers = farmers;
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

    public List<FarmersDataModel> getFarmers() {
        return farmers;
    }

    public void setFarmers(List<FarmersDataModel> farmers) {
        this.farmers = farmers;
    }
}
