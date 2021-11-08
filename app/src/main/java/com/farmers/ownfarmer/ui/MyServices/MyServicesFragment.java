package com.farmers.ownfarmer.ui.MyServices;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.farmers.ownfarmer.MainActivity;
import com.farmers.ownfarmer.R;
import com.farmers.ownfarmer.retrofit.ApiClient;
import com.farmers.ownfarmer.retrofit.ApiService;
import com.farmers.ownfarmer.ui.MyServices.DataModel.MyServicesDataList;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class MyServicesFragment extends Fragment {

    //for instance
    Activity activity;
    MainActivity mainActivity = new MainActivity();

    String defLanguage;

    ProgressDialog progress;

    RecyclerView myServices_RV;

    SharedPreferences user_preference;

    String user_id;

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
        View view = inflater.inflate(R.layout.fragment_my_services, container, false);

        //// check user type
        user_preference = activity.getSharedPreferences("user_data", MODE_PRIVATE);
        user_id = user_preference.getString("user_id", null);

        //// initialization
        myServices_RV = view.findViewById(R.id.my_services_rv);
        myServices_RV.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false));


        progress.show();
        getMyServices(user_id);


        return view;
    }


    ///////////////// Function used to get my services
    private void getMyServices(String userID) {

        ApiService apiService = ApiClient.getClient(activity).create(ApiService.class);
        Call<MyServicesDataList> call = apiService.getMyServices(userID);

        call.enqueue(new Callback<MyServicesDataList>() {
            @Override
            public void onResponse(Call<MyServicesDataList> call, final Response<MyServicesDataList> response) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyServicesDataList serviceDataList = response.body();
                        if (serviceDataList != null) {

                            int status = serviceDataList.getStatus();

                            if(status == 0){
                                progress.dismiss();
                                Toast.makeText(activity, serviceDataList.getMessage(), Toast.LENGTH_LONG).show();
                            }else if(status == 1) {

                                progress.dismiss();

                                if(serviceDataList.getMyServices()!=null && !serviceDataList.getMyServices().isEmpty()){
                                    myServices_RV.setAdapter(new MyServicesRecyclerAdapter(activity,  serviceDataList.getMyServices()));
                                }

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
            public void onFailure(Call<MyServicesDataList> call, Throwable t) {
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
    
    
}