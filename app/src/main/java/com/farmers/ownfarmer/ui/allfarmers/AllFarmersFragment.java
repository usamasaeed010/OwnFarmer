package com.farmers.ownfarmer.ui.allfarmers;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.farmers.ownfarmer.ui.allfarmers.DataModel.FarmersDataList;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllFarmersFragment extends Fragment {

    //for instance
    Activity activity;
    MainActivity mainActivity = new MainActivity();

    String defLanguage;

    ProgressDialog progress;

    RecyclerView allFarmers_RV;

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
        View view = inflater.inflate(R.layout.fragment_all_farmers, container, false);


        //// initialization
        allFarmers_RV = view.findViewById(R.id.all_farmers_rv);
        allFarmers_RV.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false));

        
        progress.show();
        getFarmers();
        

        return view;
    }


    ///////////////// Function used to get all farmers
    private void getFarmers() {

        ApiService apiService = ApiClient.getClient(activity).create(ApiService.class);
        Call<FarmersDataList> call = apiService.getFarmers();

        call.enqueue(new Callback<FarmersDataList>() {
            @Override
            public void onResponse(Call<FarmersDataList> call, final Response<FarmersDataList> response) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FarmersDataList farmerDataList = response.body();
                        if (farmerDataList != null) {

                            int status = farmerDataList.getStatus();

                            if(status == 0){
                                progress.dismiss();
                                Toast.makeText(activity, farmerDataList.getMessage(), Toast.LENGTH_LONG).show();
                            }else if(status == 1) {

                                progress.dismiss();

                                if(farmerDataList.getFarmers()!=null && !farmerDataList.getFarmers().isEmpty()){
                                    allFarmers_RV.setAdapter(new AllFarmersRecyclerAdapter(activity,  farmerDataList.getFarmers()));
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
            public void onFailure(Call<FarmersDataList> call, Throwable t) {
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