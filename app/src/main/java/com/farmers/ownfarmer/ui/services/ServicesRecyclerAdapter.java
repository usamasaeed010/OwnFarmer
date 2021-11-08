package com.farmers.ownfarmer.ui.services;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.farmers.ownfarmer.MainActivity;
import com.farmers.ownfarmer.R;
import com.farmers.ownfarmer.ui.product.DataModel.ProductDataModel;
import com.farmers.ownfarmer.ui.productdetail.ProductDetailFragment;
import com.farmers.ownfarmer.ui.servicedetail.ServiceDetailFragment;
import com.farmers.ownfarmer.ui.services.DataModel.ServicesDataModel;

import java.util.ArrayList;
import java.util.List;

class ServicesRecyclerAdapter extends RecyclerView.Adapter<ServicesRecyclerAdapter.MyViewHolder> implements Filterable {


    Activity activity;
    private List<ServicesDataModel> servicesDataModel;
    private List<ServicesDataModel> searchServiceDataModelFull;

    public ServicesRecyclerAdapter(Activity activity, List<ServicesDataModel> servicesDataModel) {
        this.activity = activity;
        this.servicesDataModel = servicesDataModel;
        this.searchServiceDataModelFull = servicesDataModel;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.services_recyclewr_item , parent , false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        String service_name = servicesDataModel.get(position).getServiceName();

        if(service_name!=null && !service_name.isEmpty()){
            holder.serviceName_TV.setText(service_name);
        }


        Glide.with(activity)
                .load(activity.getResources().getString(R.string.image_url) + servicesDataModel.get(position).getServiceImage())
                .apply(RequestOptions.placeholderOf(R.drawable.splash))
                .apply(RequestOptions.skipMemoryCacheOf(false))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA))
                .into((ImageView) holder.serviceImage_IV);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
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


    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<ServicesDataModel> filteredList = new ArrayList<>();
                if (charSequence == null || charSequence.length() == 0){
                    filteredList.addAll(searchServiceDataModelFull);

                }
                else {
                    String filterPattern = charSequence.toString().toLowerCase().trim();

                    for(ServicesDataModel items : searchServiceDataModelFull){
                        if (items.getServiceName().toLowerCase().contains(filterPattern))
                        {
                            filteredList.add(items);
                        }
                    }
                }

                FilterResults results = new FilterResults();

                results.values = filteredList;

                return  results;

            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                searchServiceDataModelFull.clear();
                searchServiceDataModelFull.addAll((List)filterResults.values);
                notifyDataSetChanged();

            }
        };
    }
    

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView serviceName_TV;
        ImageView serviceImage_IV;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            //// initialization
            serviceName_TV = itemView.findViewById(R.id.service_name_tv);
            serviceImage_IV = itemView.findViewById(R.id.service_image);

        }
    }



}