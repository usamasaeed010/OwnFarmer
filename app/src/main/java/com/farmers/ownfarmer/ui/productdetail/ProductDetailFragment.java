package com.farmers.ownfarmer.ui.productdetail;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.farmers.ownfarmer.MainActivity;
import com.farmers.ownfarmer.R;
import com.farmers.ownfarmer.retrofit.ApiClient;
import com.farmers.ownfarmer.retrofit.ApiService;
import com.farmers.ownfarmer.ui.chat.ChatActivity;
import com.farmers.ownfarmer.ui.productdetail.DataModel.ProductDetailDataList;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class ProductDetailFragment extends Fragment {

    //for instance
    Activity activity;
    MainActivity mainActivity = new MainActivity();

    String defLanguage;

    TextView productName_TV, productDescription_TV, ownerName_TV;
    ImageView productImage_IV, ownerImage_IV;
    Button chat_BTN, call_BTN;

    String product_id;

    ProgressDialog progress;

    String farmerContactNo = "";

    /////firebase
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    String target_user_name, target_user_id, target_user_image;

    SharedPreferences user_preference;
    String user_id, user_name, user_email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity =  mainActivity.getInstance();

        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0);
            defLanguage = locale.getLanguage();
        } else {
            //noinspection deprecation
            locale = getResources().getConfiguration().locale;
            defLanguage = locale.getLanguage();
        }

        ///// progress dialogue
        progress = new ProgressDialog(activity);
        progress.setMessage("Please Wait");
        progress.setCancelable(true);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);


        FirebaseApp.initializeApp(activity);

        ///////////firebase
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");


        //// check user type
        user_preference = activity.getSharedPreferences("user_data", MODE_PRIVATE);
        user_id = user_preference.getString("user_id", null);
        user_name = user_preference.getString("user_name", null);
        user_email = user_preference.getString("user_email", null);


        //////////// get value of product  id
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            product_id = bundle.getString("product_id", "");
        }


        //////initialization
        productName_TV = view.findViewById(R.id.product_name_tv);
        productDescription_TV = view.findViewById(R.id.product_description_tv);
        productImage_IV = view.findViewById(R.id.product_image_iv);
        ownerName_TV = view.findViewById(R.id.owner_name_tv);
        ownerImage_IV = view.findViewById(R.id.owner_image_iv);
        chat_BTN = view.findViewById(R.id.chat_btn);
        call_BTN = view.findViewById(R.id.call_btn);


        //// go to chat
        chat_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SignInWIthFirebase(user_email, user_email);

            }
        });



        //// call intent
        call_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                makePhoneCall(farmerContactNo);

            }
        });



        progress.show();
        getProductDetail(product_id);


        return view;
    }


    ///////////////// Function used to get products
    private void getProductDetail(String product_id) {

        ApiService apiService = ApiClient.getClient(activity).create(ApiService.class);
        Call<ProductDetailDataList> call = apiService.getProductDetail(product_id);

        call.enqueue(new Callback<ProductDetailDataList>() {
            @Override
            public void onResponse(Call<ProductDetailDataList> call, final Response<ProductDetailDataList> response) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ProductDetailDataList productDataList = response.body();
                        if (productDataList != null) {

                            int status = productDataList.getStatus();

                            if(status == 0){
                                progress.dismiss();
                                Toast.makeText(activity, productDataList.getMessage(), Toast.LENGTH_LONG).show();
                            }else if(status == 1) {

                                if(productDataList.getProduct()!=null){

                                    Glide.with(activity)
                                            .load(activity.getResources().getString(R.string.image_url) + productDataList.getProduct().getProductImage())
                                            .apply(RequestOptions.placeholderOf(R.drawable.splash))
                                            .apply(RequestOptions.skipMemoryCacheOf(false))
                                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA))
                                            .into((ImageView) productImage_IV);

                                    if(productDataList.getProduct().getProductName()!=null && !productDataList.getProduct().getProductName().isEmpty()){
                                        productName_TV.setText(productDataList.getProduct().getProductName());
                                    } else{
                                        productName_TV.setText("N/A");
                                    }

                                    if(productDataList.getProduct().getProductDescription()!=null && !productDataList.getProduct().getProductDescription().isEmpty()){
                                        productDescription_TV.setText(productDataList.getProduct().getProductDescription());
                                    } else{
                                        productDescription_TV.setText("N/A");
                                    }

                                    Glide.with(activity)
                                            .load(activity.getResources().getString(R.string.image_url) + productDataList.getProduct().getUserImage())
                                            .apply(RequestOptions.placeholderOf(R.drawable.ic_baseline_person_24))
                                            .apply(RequestOptions.skipMemoryCacheOf(false))
                                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA))
                                            .into((ImageView) ownerImage_IV);

                                    if(productDataList.getProduct().getUserName()!=null && !productDataList.getProduct().getUserName().isEmpty()){
                                        ownerName_TV.setText(productDataList.getProduct().getUserName());
                                    } else{
                                        ownerName_TV.setText("N/A");
                                    }


                                    if(productDataList.getProduct().getUserPhone()!=null && !productDataList.getProduct().getUserPhone().isEmpty()){
                                        farmerContactNo = productDataList.getProduct().getUserPhone();
                                    } else{

                                    }

                                    target_user_name = productDataList.getProduct().getUserName();
                                    target_user_image = productDataList.getProduct().getUserImage();
                                    target_user_id = productDataList.getProduct().getUserID();

                                }

                                progress.dismiss();

                            } else{
                                progress.dismiss();
                                Toast.makeText(activity, "Connecting ....", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            progress.dismiss();
                            Toast.makeText(activity, "Connecting ....", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<ProductDetailDataList> call, Throwable t) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        Toast.makeText(activity, "Connecting ...", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }




    ///// call
    private void makePhoneCall(String contact){
        if(contact!=null && !contact.isEmpty()){
            Intent intent=new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:"+contact));
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity)activity, new String[]{Manifest.permission.CALL_PHONE},1);
            }
            else
            {
                activity.startActivity(intent);
            }
        }
        else {
            Toast.makeText(activity,"No Contact Number Found",Toast.LENGTH_LONG);
        }
    }





    //////////////// checking email in firebase
    private void SignInWIthFirebase(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                Intent intent = new Intent(activity, ChatActivity.class);
                intent.putExtra("target_user_name", target_user_name);
                intent.putExtra("target_user_id", target_user_id);
                intent.putExtra("target_user_image", target_user_image);
                activity.startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e.getMessage()!=null && e.getMessage().equals("The email address is already in use by another account.")) {

                    Intent intent = new Intent(activity, ChatActivity.class);
                    intent.putExtra("target_user_name", target_user_name);
                    intent.putExtra("target_user_id", target_user_id);
                    intent.putExtra("target_user_image", target_user_image);
                    activity.startActivity(intent);

                }
                else
                {
                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }



}