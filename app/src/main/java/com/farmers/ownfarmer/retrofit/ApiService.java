package com.farmers.ownfarmer.retrofit;

import com.farmers.ownfarmer.ui.MyProducts.DataModel.DeleteDataList;
import com.farmers.ownfarmer.ui.MyProducts.DataModel.MyProductsDataList;
import com.farmers.ownfarmer.ui.MyServices.DataModel.MyServicesDataList;
import com.farmers.ownfarmer.ui.addproduct.DataModel.AddDataList;
import com.farmers.ownfarmer.ui.allfarmers.DataModel.FarmersDataList;
import com.farmers.ownfarmer.ui.editservice.DataModel.EditDataList;
import com.farmers.ownfarmer.ui.home.DataModel.HomeDataList;
import com.farmers.ownfarmer.ui.login.DataModel.LoginDataList;
import com.farmers.ownfarmer.ui.product.DataModel.ProductDataList;
import com.farmers.ownfarmer.ui.productdetail.DataModel.ProductDetailDataList;
import com.farmers.ownfarmer.ui.profile.DataModel.ProfileDataList;
import com.farmers.ownfarmer.ui.servicedetail.DataModel.ServiceDetailDataList;
import com.farmers.ownfarmer.ui.services.DataModel.ServicesDataList;
import com.farmers.ownfarmer.ui.signup.DataModel.SignupDataList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    ///////////// get services
    @POST("api.php?func=get_all_services")
    Call<ServicesDataList> getServices();

    ///////////// get products
    @POST("api.php?func=get_all_products")
    Call<ProductDataList> getProducts();


    ///////////// login user
    @FormUrlEncoded
    @POST("api.php?func=login")
    Call<LoginDataList> loginUser(@Field("email") String email,
                                  @Field("password") String password);

    ///////////// sign up user
    @FormUrlEncoded
    @POST("api.php?func=signup")
    Call<SignupDataList> signupUser(@Field("name") String name,
                                    @Field("email") String email,
                                    @Field("password") String password,
                                    @Field("phone") String phone,
                                    @Field("address") String address,
                                    @Field("user_type") String user_type);

    ///////////// product detail
    @FormUrlEncoded
    @POST("api.php?func=product_detail")
    Call<ProductDetailDataList> getProductDetail(@Field("product_id") String product_id);

    ///////////// service detail
    @FormUrlEncoded
    @POST("api.php?func=service_detail")
    Call<ServiceDetailDataList> getServiceDetail(@Field("service_id") String service_id);

    ///////////// get home page
    @POST("api.php?func=home")
    Call<HomeDataList> getHomePage();

    ///////////// get my products
    @FormUrlEncoded
    @POST("api.php?func=my_products")
    Call<MyProductsDataList> getMyProducts(@Field("user_id") String user_id);

    ///////////// get my services
    @FormUrlEncoded
    @POST("api.php?func=my_services")
    Call<MyServicesDataList> getMyServices(@Field("user_id") String user_id);

    ///////////// delete my product
    @FormUrlEncoded
    @POST("api.php?func=delete_my_product")
    Call<DeleteDataList> deleteMyProduct(@Field("product_id") String product_id);

    ///////////// delete my service
    @FormUrlEncoded
    @POST("api.php?func=delete_my_service")
    Call<DeleteDataList> deleteMyService(@Field("service_id") String service_id);

    ///////////// user profile
    @FormUrlEncoded
    @POST("api.php?func=my_profile")
    Call<ProfileDataList> getMyProfile(@Field("user_id") String user_id,
                                       @Field("user_type") String user_type);

    ////////////// add product
    @FormUrlEncoded
    @POST("api.php?func=add_product")
    Call<AddDataList> addProduct(@Field("product_name") String product_name,
                                 @Field("product_price") String product_price,
                                 @Field("product_unit") String product_unit,
                                 @Field("product_description") String product_description,
                                 @Field("related_user_id") String related_user_id);

    ////////////// add service
    @FormUrlEncoded
    @POST("api.php?func=add_service")
    Call<AddDataList> addService(@Field("service_name") String service_name,
                                 @Field("service_price") String service_price,
                                 @Field("service_duration") String service_duration,
                                 @Field("service_description") String service_description,
                                 @Field("related_user_id") String related_user_id);

    ///////////// get farmers
    @POST("api.php?func=get_all_farmers")
    Call<FarmersDataList> getFarmers();


    ////////////// edit service
    @FormUrlEncoded
    @POST("api.php?func=edit_service")
    Call<EditDataList> updateService(@Field("service_name") String service_name,
                                     @Field("service_price") String service_price,
                                     @Field("service_duration") String service_duration,
                                     @Field("service_description") String service_description,
                                     @Field("service_id") String service_id);


    ////////////// edit product
    @FormUrlEncoded
    @POST("api.php?func=edit_product")
    Call<EditDataList> updateProduct(@Field("product_name") String product_name,
                                    @Field("product_price") String product_price,
                                    @Field("product_unit") String product_unit,
                                    @Field("product_description") String product_description,
                                    @Field("product_id") String product_id);

}