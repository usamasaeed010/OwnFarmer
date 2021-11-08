package com.farmers.ownfarmer.ui.profile.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProfileDataList {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("my_profile")
    @Expose
    private ProfileDataModel myProfile;
    @SerializedName("my_products")
    @Expose
    private List<ProfileMyProductsDataList> myProducts = null;
    @SerializedName("my_services")
    @Expose
    private List<ProfileMyServicesDataModel> myServices = null;

    public ProfileDataList(Integer status, String message, ProfileDataModel myProfile, List<ProfileMyProductsDataList> myProducts, List<ProfileMyServicesDataModel> myServices) {
        this.status = status;
        this.message = message;
        this.myProfile = myProfile;
        this.myProducts = myProducts;
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

    public ProfileDataModel getMyProfile() {
        return myProfile;
    }

    public void setMyProfile(ProfileDataModel myProfile) {
        this.myProfile = myProfile;
    }

    public List<ProfileMyProductsDataList> getMyProducts() {
        return myProducts;
    }

    public void setMyProducts(List<ProfileMyProductsDataList> myProducts) {
        this.myProducts = myProducts;
    }

    public List<ProfileMyServicesDataModel> getMyServices() {
        return myServices;
    }

    public void setMyServices(List<ProfileMyServicesDataModel> myServices) {
        this.myServices = myServices;
    }
}
