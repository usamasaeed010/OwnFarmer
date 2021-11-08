package com.farmers.ownfarmer.ui.profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.farmers.ownfarmer.MainActivity;
import com.farmers.ownfarmer.R;
import com.farmers.ownfarmer.retrofit.ApiClient;
import com.farmers.ownfarmer.retrofit.ApiService;
import com.farmers.ownfarmer.ui.Messaging.MessageActivity;
import com.farmers.ownfarmer.ui.login.LogInActivity;
import com.farmers.ownfarmer.ui.profile.DataModel.ProfileDataList;
import com.farmers.ownfarmer.ui.profile.DataModel.ProfileMyProductsDataList;
import com.farmers.ownfarmer.ui.profile.DataModel.ProfileMyServicesDataModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    //for instance
    Activity activity;
    MainActivity mainActivity = new MainActivity();

    String defLanguage;

    Button logout_BTN, chat_BTN;
    TextView name_TV, email_TV, phoneNumber_TV, myProducts_TV, myServices_TV, address_TV;
    LinearLayout myProducts_LL, myServices_LL;

    SharedPreferences user_preference;
    String user_id, user_type;

    ProgressDialog progress;

    /////firebase
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    String target_user_name, target_user_id, target_user_image;

    String user_name, user_email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ////
        activity = mainActivity.getInstance();

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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //// check user type
        user_preference = activity.getSharedPreferences("user_data", MODE_PRIVATE);
        user_id = user_preference.getString("user_id", null);
        user_type = user_preference.getString("user_type", null);
        user_name = user_preference.getString("user_name", null);
        user_email = user_preference.getString("user_email", null);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");


        ///// initialization
        logout_BTN = view.findViewById(R.id.logout_user_btn);
        chat_BTN = view.findViewById(R.id.chat_profile_tv);
        name_TV = view.findViewById(R.id.name_profile_tv);
        email_TV = view.findViewById(R.id.email_profile_tv);
        phoneNumber_TV = view.findViewById(R.id.phone_profile_tv);
        myProducts_TV = view.findViewById(R.id.my_product_profile_tv);
        myServices_TV = view.findViewById(R.id.my_services_ptofile_tv);
        address_TV = view.findViewById(R.id.my_address_tv);
        myProducts_LL = view.findViewById(R.id.my_products_ll);
        myServices_LL = view.findViewById(R.id.my_services_ll);



        //// logout User
        logout_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = activity.getSharedPreferences("autologin_patient", MODE_PRIVATE).edit();
                SharedPreferences.Editor editor1 = activity.getSharedPreferences("user_data", MODE_PRIVATE).edit();
                editor.clear();
                editor.apply();
                editor1.clear(); //clear all stored data
                editor1.apply();

                Toast.makeText(activity, "Logout Successfully", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(activity, LogInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                activity.finish();

            }
        });



        /// call chat listing
        chat_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SignInWIthFirebase(user_email, user_email);

            }
        });



        /// call profile
        if(user_id!=null && !user_id.isEmpty()){

            progress.show();
            getProfile(user_id, user_type);

        }


        return view;
    }


    ///////////////// Function used to get profile
    private void getProfile(String user_id, String user_type) {

        ApiService apiService = ApiClient.getClient(activity).create(ApiService.class);
        Call<ProfileDataList> call = apiService.getMyProfile(user_id, user_type);

        call.enqueue(new Callback<ProfileDataList>() {
            @Override
            public void onResponse(Call<ProfileDataList> call, final Response<ProfileDataList> response) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ProfileDataList profileDataList = response.body();
                        if (profileDataList != null) {

                            int status = profileDataList.getStatus();

                            if(status == 0){
                                progress.dismiss();
                                Toast.makeText(activity, profileDataList.getMessage(), Toast.LENGTH_LONG).show();
                            }else if(status == 1) {

                                if(profileDataList.getMyProfile()!=null){

                                    if(profileDataList.getMyProfile().getUserName()!=null && !profileDataList.getMyProfile().getUserName().isEmpty()){
                                        name_TV.setText(profileDataList.getMyProfile().getUserName());
                                    } else{
                                        name_TV.setText("N/A");
                                    }

                                    if(profileDataList.getMyProfile().getUserEmail()!=null && !profileDataList.getMyProfile().getUserEmail().isEmpty()){
                                        email_TV.setText(profileDataList.getMyProfile().getUserEmail());
                                    } else{
                                        email_TV.setText("N/A");
                                    }

                                    if(profileDataList.getMyProfile().getUserPhone()!=null && !profileDataList.getMyProfile().getUserPhone().isEmpty()){
                                        phoneNumber_TV.setText(profileDataList.getMyProfile().getUserPhone());
                                    } else{
                                        phoneNumber_TV.setText("N/A");
                                    }

                                    if(profileDataList.getMyProfile().getUserAddress()!=null && !profileDataList.getMyProfile().getUserAddress().isEmpty()){
                                        address_TV.setText(profileDataList.getMyProfile().getUserAddress());
                                    } else{
                                        address_TV.setText("N/A");
                                    }

                                    List<ProfileMyProductsDataList> myProductsModel = new ArrayList<>();

                                    myProductsModel = profileDataList.getMyProducts();

                                    if(myProductsModel != null && !myProductsModel.isEmpty()){
                                        String output = "";
                                        for (int i = 0; i < myProductsModel.size(); i++) {
                                            //Append all the values to a string
                                            if(i == 0){
                                                output += myProductsModel.get(i).getProductName();
                                            } else{
                                                output += "\n" + myProductsModel.get(i).getProductName();
                                            }
                                        }
                                        myProducts_TV.setText(output);
                                    } else{
                                        myProducts_TV.setText("N/A");
                                        myProducts_LL.setVisibility(View.GONE);
                                    }

                                    List<ProfileMyServicesDataModel> myServicesModel = new ArrayList<>();

                                    myServicesModel = profileDataList.getMyServices();

                                    if(myServicesModel != null && !myServicesModel.isEmpty()){
                                        String output = "";
                                        for (int i = 0; i < myServicesModel.size(); i++) {
                                            //Append all the values to a string
                                            if(i == 0){
                                                output += myServicesModel.get(i).getServiceName();
                                            } else{
                                                output += "\n" + myServicesModel.get(i).getServiceName();
                                            }
                                        }
                                        myServices_TV.setText(output);
                                    } else{
                                        myServices_TV.setText("N/A");
                                        myServices_LL.setVisibility(View.GONE);
                                    }

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
            public void onFailure(Call<ProfileDataList> call, Throwable t) {
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




    //////////////// checking email in firebase
    private void SignInWIthFirebase(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                Intent intent = new Intent(activity, MessageActivity.class);

                startActivity(intent);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e.getMessage()!=null && e.getMessage().equals("The email address is already in use by another account.")) {

                    Intent intent = new Intent(activity, MessageActivity.class);

                    startActivity(intent);

                }
                else
                {
                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }



}