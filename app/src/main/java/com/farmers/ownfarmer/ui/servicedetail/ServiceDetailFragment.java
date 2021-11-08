package com.farmers.ownfarmer.ui.servicedetail;

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
import com.farmers.ownfarmer.ui.servicedetail.DataModel.ServiceDetailDataList;
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


public class ServiceDetailFragment extends Fragment {

    //for instance
    Activity activity;
    MainActivity mainActivity = new MainActivity();

    String defLanguage;

    ImageView serviceImage_IV, ownerImage_IV;
    TextView serviceName_TV, serviceDescription_TV, ownerName_TV;
    Button chat_BTN, call_BTN;

    String service_id;

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
        View view = inflater.inflate(R.layout.fragment_service_detail, container, false);



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
            service_id = bundle.getString("service_id", "");
        }


        //// initialization
        serviceImage_IV = view.findViewById(R.id.service_image_iv);
        serviceName_TV = view.findViewById(R.id.service_name_tv);
        serviceDescription_TV = view.findViewById(R.id.service_description_tv);
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
        getServiceDetail(service_id);



        return view;
    }


    ///////////////// Function used to get service
    private void getServiceDetail(String service_id) {

        ApiService apiService = ApiClient.getClient(activity).create(ApiService.class);
        Call<ServiceDetailDataList> call = apiService.getServiceDetail(service_id);

        call.enqueue(new Callback<ServiceDetailDataList>() {
            @Override
            public void onResponse(Call<ServiceDetailDataList> call, final Response<ServiceDetailDataList> response) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ServiceDetailDataList serviceDataList = response.body();
                        if (serviceDataList != null) {

                            int status = serviceDataList.getStatus();

                            if(status == 0){
                                progress.dismiss();
                                Toast.makeText(activity, serviceDataList.getMessage(), Toast.LENGTH_LONG).show();
                            }else if(status == 1) {

                                if(serviceDataList.getService()!=null){

                                    Glide.with(activity)
                                            .load(activity.getResources().getString(R.string.image_url) + serviceDataList.getService().getServiceImage())
                                            .apply(RequestOptions.placeholderOf(R.drawable.splash))
                                            .apply(RequestOptions.skipMemoryCacheOf(false))
                                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA))
                                            .into((ImageView) serviceImage_IV);

                                    if(serviceDataList.getService().getServiceName()!=null && !serviceDataList.getService().getServiceName().isEmpty()){
                                        serviceName_TV.setText(serviceDataList.getService().getServiceName());
                                    } else{
                                        serviceName_TV.setText("N/A");
                                    }

                                    if(serviceDataList.getService().getServiceDescription()!=null && !serviceDataList.getService().getServiceDescription().isEmpty()){
                                        serviceDescription_TV.setText(serviceDataList.getService().getServiceDescription());
                                    } else{
                                        serviceDescription_TV.setText("N/A");
                                    }

                                    Glide.with(activity)
                                            .load(activity.getResources().getString(R.string.image_url) + serviceDataList.getService().getUserImage())
                                            .apply(RequestOptions.placeholderOf(R.drawable.ic_baseline_person_24))
                                            .apply(RequestOptions.skipMemoryCacheOf(false))
                                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA))
                                            .into((ImageView) ownerImage_IV);

                                    if(serviceDataList.getService().getUserName()!=null && !serviceDataList.getService().getUserName().isEmpty()){
                                        ownerName_TV.setText(serviceDataList.getService().getUserName());
                                    } else{
                                        ownerName_TV.setText("N/A");
                                    }


                                    if(serviceDataList.getService().getUserPhone()!=null && !serviceDataList.getService().getUserPhone().isEmpty()){
                                        farmerContactNo = serviceDataList.getService().getUserPhone();
                                    } else{

                                    }

                                    target_user_name = serviceDataList.getService().getUserName();
                                    target_user_image = serviceDataList.getService().getUserImage();
                                    target_user_id = serviceDataList.getService().getUserID();


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
            public void onFailure(Call<ServiceDetailDataList> call, Throwable t) {
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