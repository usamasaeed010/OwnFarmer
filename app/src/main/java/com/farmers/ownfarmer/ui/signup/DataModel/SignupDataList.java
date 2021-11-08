package com.farmers.ownfarmer.ui.signup.DataModel;

import com.farmers.ownfarmer.ui.login.DataModel.LoginDataModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignupDataList {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("isLogin")
    @Expose
    private LoginDataModel isLogin;

    public SignupDataList(Integer status, String message, LoginDataModel isLogin) {
        this.status = status;
        this.message = message;
        this.isLogin = isLogin;
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

    public LoginDataModel getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(LoginDataModel isLogin) {
        this.isLogin = isLogin;
    }
}
