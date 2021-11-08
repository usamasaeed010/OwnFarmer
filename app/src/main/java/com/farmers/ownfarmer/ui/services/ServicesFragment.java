package com.farmers.ownfarmer.ui.services;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.farmers.ownfarmer.MainActivity;
import com.farmers.ownfarmer.R;
import com.farmers.ownfarmer.retrofit.ApiClient;
import com.farmers.ownfarmer.retrofit.ApiService;
import com.farmers.ownfarmer.ui.services.DataModel.ServicesDataList;
import com.farmers.ownfarmer.ui.services.DataModel.ServicesDataModel;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class ServicesFragment extends Fragment {

    //for instance
    Activity activity;
    MainActivity mainActivity = new MainActivity();

    String defLanguage;

    RecyclerView services_RV;

    ProgressDialog progress;

    EditText searchService_ET;

    ServicesRecyclerAdapter serviceAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

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

        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        ////// initialization
        services_RV = root.findViewById(R.id.services_rv);
        services_RV.setLayoutManager(new GridLayoutManager(activity, 2));
        searchService_ET = root.findViewById(R.id.search_service_tv);

        progress.show();
        getServices();


        //// search service
        searchService_ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                serviceAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        return root;
    }


    ///////////////// Function used to get services
    private void getServices() {

        ApiService apiService = ApiClient.getClient(activity).create(ApiService.class);
        Call<ServicesDataList> call = apiService.getServices();

        call.enqueue(new Callback<ServicesDataList>() {
            @Override
            public void onResponse(Call<ServicesDataList> call, final Response<ServicesDataList> response) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ServicesDataList serviceDataList = response.body();
                        if (serviceDataList != null) {

                            int status = serviceDataList.getStatus();

                            if(status == 0){
                                progress.dismiss();
                                Toast.makeText(activity, serviceDataList.getMessage(), Toast.LENGTH_LONG).show();
                            }else if(status == 1) {

                                progress.dismiss();

                                if(serviceDataList.getServices()!=null && !serviceDataList.getServices().isEmpty()){
                                    serviceAdapter = new ServicesRecyclerAdapter(activity,  serviceDataList.getServices());
                                    services_RV.setAdapter(serviceAdapter);
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
            public void onFailure(Call<ServicesDataList> call, Throwable t) {
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