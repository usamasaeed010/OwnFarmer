package com.farmers.ownfarmer.ui.home.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeServiceDataModel {

    @SerializedName("service_id")
    @Expose
    private String serviceId;
    @SerializedName("service_name")
    @Expose
    private String serviceName;
    @SerializedName("service_price")
    @Expose
    private String servicePrice;
    @SerializedName("service_duration")
    @Expose
    private String serviceDuration;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("service_description")
    @Expose
    private String serviceDescription;
    @SerializedName("related_user")
    @Expose
    private String relatedUser;
    @SerializedName("service_image")
    @Expose
    private String serviceImage;

    public HomeServiceDataModel(String serviceId, String serviceName, String servicePrice, String serviceDuration, String createdAt, String updatedAt, String serviceDescription, String relatedUser, String serviceImage) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
        this.serviceDuration = serviceDuration;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.serviceDescription = serviceDescription;
        this.relatedUser = relatedUser;
        this.serviceImage = serviceImage;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getServiceDuration() {
        return serviceDuration;
    }

    public void setServiceDuration(String serviceDuration) {
        this.serviceDuration = serviceDuration;
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

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public String getRelatedUser() {
        return relatedUser;
    }

    public void setRelatedUser(String relatedUser) {
        this.relatedUser = relatedUser;
    }

    public String getServiceImage() {
        return serviceImage;
    }

    public void setServiceImage(String serviceImage) {
        this.serviceImage = serviceImage;
    }
}
