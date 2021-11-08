package com.farmers.ownfarmer.ui.product;

import android.app.Activity;
import android.app.ProgressDialog;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.farmers.ownfarmer.MainActivity;
import com.farmers.ownfarmer.R;
import com.farmers.ownfarmer.retrofit.ApiClient;
import com.farmers.ownfarmer.retrofit.ApiService;
import com.farmers.ownfarmer.ui.product.DataModel.ProductDataList;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragment extends Fragment {

    //for instance
    Activity activity;
    MainActivity mainActivity = new MainActivity();

    String defLanguage;

    RecyclerView products_RV;

    ProgressDialog progress;

    EditText searchProduct_ET;

    ProductsRecyclerAdapter productAdapter;

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

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        ////// initialization
        products_RV = root.findViewById(R.id.products_rv);
        products_RV.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false));
        searchProduct_ET = root.findViewById(R.id.search_product_tv);

        ///// get products
        progress.show();
        getProducts();



        //// search product
        searchProduct_ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                productAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return root;
    }


    ///////////////// Function used to get products
    private void getProducts() {

        ApiService apiService = ApiClient.getClient(activity).create(ApiService.class);
        Call<ProductDataList> call = apiService.getProducts();

        call.enqueue(new Callback<ProductDataList>() {
            @Override
            public void onResponse(Call<ProductDataList> call, final Response<ProductDataList> response) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ProductDataList productDataList = response.body();
                        if (productDataList != null) {

                            int status = productDataList.getStatus();

                            if(status == 0){
                                progress.dismiss();
                                Toast.makeText(activity, productDataList.getMessage(), Toast.LENGTH_LONG).show();
                            }else if(status == 1) {

                                progress.dismiss();

                                if(productDataList.getProducts()!=null && !productDataList.getProducts().isEmpty()){
                                    productAdapter = new ProductsRecyclerAdapter(activity,  productDataList.getProducts());
                                    products_RV.setAdapter(productAdapter);
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
            public void onFailure(Call<ProductDataList> call, Throwable t) {
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