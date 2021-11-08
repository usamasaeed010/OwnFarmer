package com.farmers.ownfarmer.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.farmers.ownfarmer.MainActivity;
import com.farmers.ownfarmer.R;
import com.farmers.ownfarmer.ui.home.DataModel.HomeServiceDataModel;
import com.farmers.ownfarmer.ui.servicedetail.ServiceDetailFragment;
import com.farmers.ownfarmer.ui.services.DataModel.ServicesDataModel;

import java.util.List;
import java.util.Locale;

class LatestServicesRecyclerAdapter extends RecyclerView.Adapter<LatestServicesRecyclerAdapter.MyViewHolder> {

    private Activity activity;
    private List<HomeServiceDataModel> servicesDataModel;

    public LatestServicesRecyclerAdapter(Activity activity, List<HomeServiceDataModel> servicesDataModel) {
        this.activity = activity;
        this.servicesDataModel = servicesDataModel;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.latest_services_recycler_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {


        String service_name = servicesDataModel.get(position).getServiceName();

        if(service_name!=null && !service_name.isEmpty()){
            holder.textView.setText(service_name);
        }

        Glide.with(activity)
                .load(activity.getResources().getString(R.string.image_url) + servicesDataModel.get(position).getServiceImage())
                .apply(RequestOptions.placeholderOf(R.drawable.splash))
                .apply(RequestOptions.skipMemoryCacheOf(false))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA))
                .into((ImageView) holder.imageView);



        holder.btn_detail_service.setOnClickListener(new View.OnClickListener() {
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

        public ImageView imageView;
        public TextView textView;
        Button btn_detail_service;
        CardView cvPopularCategory;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.itemImage);
            textView = itemView.findViewById(R.id.tv_category_name_recyclerItem);
            cvPopularCategory=itemView.findViewById(R.id.cvPopularCategory);
            btn_detail_service = itemView.findViewById(R.id.add_cart_button_service);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
