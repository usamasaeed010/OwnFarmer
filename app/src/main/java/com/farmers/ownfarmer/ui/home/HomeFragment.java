package com.farmers.ownfarmer.ui.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.farmers.ownfarmer.MainActivity;
import com.farmers.ownfarmer.R;
import com.farmers.ownfarmer.retrofit.ApiClient;
import com.farmers.ownfarmer.retrofit.ApiService;
import com.farmers.ownfarmer.ui.MyProducts.DataModel.MyProductsDataList;
import com.farmers.ownfarmer.ui.MyServices.DataModel.MyServicesDataList;
import com.farmers.ownfarmer.ui.allfarmers.AllFarmersFragment;
import com.farmers.ownfarmer.ui.allfarmers.DataModel.FarmersDataList;
import com.farmers.ownfarmer.ui.home.DataModel.HomeDataList;
import com.farmers.ownfarmer.ui.home.ForFarmer.FarmerServicesRecyclerAdapter;
import com.farmers.ownfarmer.ui.home.ForFarmer.FarmerproductsRecyclerAdapter;
import com.farmers.ownfarmer.ui.product.ProductFragment;
import com.farmers.ownfarmer.ui.services.ServicesFragment;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    //for instance
    Activity activity;
    MainActivity mainActivity = new MainActivity();

    String defLanguage;

    RecyclerView latestProducts_RV, latestServices_RV, allfarmers_RV,product_farmer_RV,service_farmer_RV;
    ViewPager homeSlider_VP;

    TextView latestProducts_TV, viewAllLatestProducts_TV, viewAllLatestServices_TV, viewAllallfarmers_TV;

    ProgressDialog progress;

    LinearLayout farmerScreen_LL, customerScreen_LL;

    String user_type,user_id;
    SharedPreferences userData_preference;

    private int currentPage = 0;
    private Handler handler;
    private int[] sliderImageId = new int[]{
            R.drawable.slider1, R.drawable.slider2, R.drawable.slider3, R.drawable.slider4,
    };
    private static final long SLIDER_TIMER = 2000; // change slider interval
    private boolean isCountDownTimerActive = false; // let the timer start if and only if it has completed previous task
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {

            if (!isCountDownTimerActive) {
                automateSlider();
            }

            handler.postDelayed(runnable, 1000);
            // our runnable should keep running for every 1000 milliseconds (1 seconds)
        }
    };

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


        //// check user type
        userData_preference = activity.getSharedPreferences("user_data", MODE_PRIVATE);
        user_type = userData_preference.getString("user_type", null);
        user_id = userData_preference.getString("user_id", null);



        ///// progress dialogue
        progress = new ProgressDialog(activity);
        progress.setMessage("Please Wait");
        progress.setCancelable(true);


        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ////// initialization
        homeSlider_VP = root.findViewById(R.id.sliderImage_vp);
        homeSlider_VP.setAdapter(new SliderViewPagerAdapter(activity, sliderImageId));
        latestProducts_RV = root.findViewById(R.id.latestProducts_rv);
        latestProducts_RV.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        latestServices_RV = root.findViewById(R.id.latestServices_rv);
        latestServices_RV.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        latestProducts_TV = root.findViewById(R.id.latest_products_tv);
        viewAllLatestProducts_TV = root.findViewById(R.id.products_viewall_TV);
        viewAllLatestServices_TV = root.findViewById(R.id.services_viewall_TV);
        farmerScreen_LL = root.findViewById(R.id.farmer_screen_ll);
        product_farmer_RV = root.findViewById(R.id.all_farmer_product_RV);
        product_farmer_RV.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

        service_farmer_RV = root.findViewById(R.id.all_farmer_ser_RV);
        service_farmer_RV.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

        customerScreen_LL = root.findViewById(R.id.customer_screen_ll);

        viewAllallfarmers_TV = root.findViewById(R.id.all_farmars_viewall_TV);
        allfarmers_RV = root.findViewById(R.id.all_farmars_rv);
        allfarmers_RV.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,false));


        ///// farmer and customer screen
        if (user_type != null && !user_type.isEmpty()) {
            if (user_type.equals("1")) {
                farmerScreen_LL.setVisibility(View.VISIBLE);

                customerScreen_LL.setVisibility(View.GONE);
                allfarmers_RV.setVisibility(View.GONE);
            } else {
                farmerScreen_LL.setVisibility(View.GONE);
                customerScreen_LL.setVisibility(View.VISIBLE);
                allfarmers_RV.setVisibility(View.VISIBLE);
            }
        } else {
            farmerScreen_LL.setVisibility(View.GONE);
            customerScreen_LL.setVisibility(View.GONE);
        }

        if (defLanguage.equals("ur")) {
            latestProducts_TV.setGravity(Gravity.END);
            latestProducts_TV.setGravity(Gravity.RIGHT);

        } else {
            latestProducts_TV.setGravity(Gravity.START);
            latestProducts_TV.setGravity(Gravity.LEFT);
        }

        ///// latest products
        viewAllLatestProducts_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProductFragment productFragment = new ProductFragment();
                FragmentTransaction transaction = ((MainActivity) activity).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, productFragment); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();

            }
        });


        ///// latest products
        viewAllLatestServices_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ServicesFragment serviceFragment = new ServicesFragment();
                FragmentTransaction transaction = ((MainActivity) activity).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, serviceFragment); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();

            }
        });

        ///// all farmer ////
        viewAllallfarmers_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AllFarmersFragment allFarmers = new AllFarmersFragment();
                FragmentTransaction transaction = ((MainActivity) activity).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, allFarmers); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();

            }
        });


        ////// page listener
        homeSlider_VP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    currentPage = 0;
                } else if (position == 1) {
                    currentPage = 1;
                } else if (position == 2) {
                    currentPage = 2;
                } else {
                    currentPage = 3;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        handler = new Handler();
        handler.postDelayed(runnable, 1000);
        runnable.run();


        progress.show();
        getHomePage();
        getFarmers();
        getMyProducts(user_id);
        getMyServices(user_id);
        return root;
    }


    private void automateSlider() {
        isCountDownTimerActive = true;
        new CountDownTimer(SLIDER_TIMER, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                int nextSlider = currentPage + 1;
                int size = sliderImageId.length;
                ////////////This is used for get the length of slider array list/////////////
                if (size == 0) {

                    size = 4;
                } else {


                    if (nextSlider == size) {
                        nextSlider = 0; // if it's last Image, let it go to the first image
                    }

                    homeSlider_VP.setCurrentItem(nextSlider);
                    isCountDownTimerActive = false;
                }
            }
        }.start();
    }


    private void getMyProducts(String userID) {

        ApiService apiService = ApiClient.getClient(activity).create(ApiService.class);
        Call<MyProductsDataList> call = apiService.getMyProducts(userID);

        call.enqueue(new Callback<MyProductsDataList>() {
            @Override
            public void onResponse(Call<MyProductsDataList> call, final Response<MyProductsDataList> response) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyProductsDataList productDataList = response.body();
                        if (productDataList != null) {

                            int status = productDataList.getStatus();

                            if(status == 0){
                                progress.dismiss();
                                Toast.makeText(activity, productDataList.getMessage(), Toast.LENGTH_LONG).show();
                            }else if(status == 1) {

                                progress.dismiss();

                                if(productDataList.getMyProducts()!=null && !productDataList.getMyProducts().isEmpty()){
                                    product_farmer_RV.setAdapter(new FarmerproductsRecyclerAdapter(activity, productDataList.getMyProducts()));
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
            public void onFailure(Call<MyProductsDataList> call, Throwable t) {
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
                                    service_farmer_RV.setAdapter(new FarmerServicesRecyclerAdapter(activity, serviceDataList.getMyServices()));
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

                            if (status == 0) {
                                progress.dismiss();
                                Toast.makeText(activity, farmerDataList.getMessage(), Toast.LENGTH_LONG).show();
                            } else if (status == 1) {

                                progress.dismiss();

                                if (farmerDataList.getFarmers() != null && !farmerDataList.getFarmers().isEmpty()) {
                                    allfarmers_RV.setAdapter(new HomeAllFarmerAdapter(activity, farmerDataList.getFarmers()));
                                }

                            } else {
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


    ///////////////// Function used to get home page
    private void getHomePage() {

        ApiService apiService = ApiClient.getClient(activity).create(ApiService.class);
        Call<HomeDataList> call = apiService.getHomePage();

        call.enqueue(new Callback<HomeDataList>() {
            @Override
            public void onResponse(Call<HomeDataList> call, final Response<HomeDataList> response) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HomeDataList homeDataList = response.body();
                        if (homeDataList != null) {

                            int status = homeDataList.getStatus();

                            if (status == 0) {
                                progress.dismiss();
                                Toast.makeText(activity, homeDataList.getMessage(), Toast.LENGTH_LONG).show();
                            } else if (status == 1) {

                                progress.dismiss();

                                if (homeDataList.getProductsHome() != null && !homeDataList.getProductsHome().isEmpty()) {
                                    latestProducts_RV.setAdapter(new LatestProductsRecyclerAdapter(activity, homeDataList.getProductsHome()));
                                }

                                if (homeDataList.getServicesHome() != null && !homeDataList.getServicesHome().isEmpty()) {
                                    latestServices_RV.setAdapter(new LatestServicesRecyclerAdapter(activity, homeDataList.getServicesHome()));
                                }


                            } else {
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
            public void onFailure(Call<HomeDataList> call, Throwable t) {
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