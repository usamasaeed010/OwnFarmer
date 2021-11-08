package com.farmers.ownfarmer.ui.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.farmers.ownfarmer.R;
import com.farmers.ownfarmer.ui.allfarmers.DataModel.FarmersDataModel;

import java.util.ArrayList;
import java.util.List;


class HomeAllFarmerAdapter extends RecyclerView.Adapter<HomeAllFarmerAdapter.MyViewHolder>  {


    Activity activity;
    private List<FarmersDataModel> farmersDataModel = new ArrayList<>();
    ProgressDialog progress;

    public HomeAllFarmerAdapter(Activity activity, List<FarmersDataModel> farmersDataModel) {
        this.activity = activity;
        this.farmersDataModel = farmersDataModel;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_home_all_farmars , parent , false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        ///// progress dialogue
        progress = new ProgressDialog(activity);
        progress.setMessage("Please Wait");
        progress.setCancelable(true);

        if(farmersDataModel.get(position).getUserName()!=null && !farmersDataModel.get(position).getUserName().isEmpty()){
            holder.farmerName_TV.setText(farmersDataModel.get(position).getUserName());
        }
        else{
            holder.farmerName_TV.setText("N/A");
        }

        String email = farmersDataModel.get(position).getUserEmail();
        if(farmersDataModel.get(position).getUserPhone()!=null && !farmersDataModel.get(position).getUserPhone().isEmpty()){
            holder.farmerPhone_TV.setText(farmersDataModel.get(position).getUserPhone());
        } else{
            holder.farmerPhone_TV.setText("N/A");
        }

        if(farmersDataModel.get(position).getUserAddress()!=null && !farmersDataModel.get(position).getUserAddress().isEmpty()){
            holder.farmerAddress_TV.setText(farmersDataModel.get(position).getUserAddress());
        } else{
            holder.farmerAddress_TV.setText("N/A");
        }


        Glide.with(activity)
                .load(activity.getResources().getString(R.string.image_url) + farmersDataModel.get(position).getUserImage())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_baseline_person_24))
                .apply(RequestOptions.skipMemoryCacheOf(false))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA))
                .into((ImageView) holder.farmerImage_IV);

        holder.add_cart_button_farmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,FarmerDataActivity.class);
                intent.putExtra("farmer_Name",farmersDataModel.get(position).getUserName());
                intent.putExtra("farmer_email",farmersDataModel.get(position).getUserEmail());
                intent.putExtra("farmer_phone",farmersDataModel.get(position).getUserPhone());
                intent.putExtra("farmer_address",farmersDataModel.get(position).getUserAddress());
                activity.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return farmersDataModel.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView farmerImage_IV;
        TextView farmerName_TV, farmerPhone_TV, farmerAddress_TV;
        Button add_cart_button_farmer;
        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            //// initialization
            farmerImage_IV = itemView.findViewById(R.id.img_home_all_farmer);
            farmerName_TV = itemView.findViewById(R.id.farmer_name_tv_home);
            farmerPhone_TV = itemView.findViewById(R.id.farmer_phone_tv_home);
            farmerAddress_TV = itemView.findViewById(R.id.farmer_address_tv_home);
            add_cart_button_farmer = itemView.findViewById(R.id.add_cart_button_farmer);

        }
    }
}