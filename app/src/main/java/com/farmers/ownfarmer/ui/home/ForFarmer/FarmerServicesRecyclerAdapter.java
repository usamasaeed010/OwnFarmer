package com.farmers.ownfarmer.ui.home.ForFarmer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.farmers.ownfarmer.MainActivity;
import com.farmers.ownfarmer.R;
import com.farmers.ownfarmer.retrofit.ApiClient;
import com.farmers.ownfarmer.retrofit.ApiService;
import com.farmers.ownfarmer.ui.MyProducts.DataModel.DeleteDataList;
import com.farmers.ownfarmer.ui.MyServices.DataModel.MyServicesDataModel;
import com.farmers.ownfarmer.ui.editservice.EditServiceActivity;
import com.farmers.ownfarmer.ui.servicedetail.ServiceDetailFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FarmerServicesRecyclerAdapter extends RecyclerView.Adapter<FarmerServicesRecyclerAdapter.MyViewHolder>  {


    Activity activity;
    private List<MyServicesDataModel> servicesDataModel;
    ProgressDialog progress;

    public FarmerServicesRecyclerAdapter(Activity activity, List<MyServicesDataModel> servicesDataModel) {
        this.activity = activity;
        this.servicesDataModel = servicesDataModel;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.ly_home_my_product , parent , false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {


        ///// progress dialogue
        progress = new ProgressDialog(activity);
        progress.setMessage("Please Wait");
        progress.setCancelable(true);


        String service_name = servicesDataModel.get(position).getServiceName();

        if(service_name!=null && !service_name.isEmpty()){
            holder.myserviceName_TV.setText(service_name);
        } else{
            holder.myserviceName_TV.setText("N/A");
        }


        if(servicesDataModel.get(position).getServicePrice()!=null && !servicesDataModel.get(position).getServicePrice().isEmpty()){
            holder.myServicePrice_TV.setText("Price : Rs. "+servicesDataModel.get(position).getServicePrice());
        } else{
            holder.myServicePrice_TV.setText("Price : Rs. N/A");
        }


        Glide.with(activity)
                .load(activity.getResources().getString(R.string.image_url) + servicesDataModel.get(position).getServiceImage())
                .apply(RequestOptions.placeholderOf(R.drawable.splash))
                .apply(RequestOptions.skipMemoryCacheOf(false))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA))
                .into((ImageView) holder.myserviceImage_IV);



        holder.add_cart_button_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String service_id = servicesDataModel.get(position).getServiceId();

                ServiceDetailFragment serviceDetailFragment = new ServiceDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("service_id", service_id);
                serviceDetailFragment.setArguments(bundle);
                FragmentTransaction transaction = ((MainActivity) activity).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, serviceDetailFragment ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return servicesDataModel.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView myserviceName_TV, myServicePrice_TV,myServiceUnit_TV;
        ImageView myserviceImage_IV;
        Button add_cart_button_product;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            myserviceImage_IV = itemView.findViewById(R.id.img_home_all_farmer_home);
            myserviceName_TV = itemView.findViewById(R.id.product_name_tv_home);
            myServicePrice_TV = itemView.findViewById(R.id.product_price_tv_home);
            myServiceUnit_TV = itemView.findViewById(R.id.farmer_descript_tv_home);
            add_cart_button_product = itemView.findViewById(R.id.add_cart_button_product);


        }
    }




}