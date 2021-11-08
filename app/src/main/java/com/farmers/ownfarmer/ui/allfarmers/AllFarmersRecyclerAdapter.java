package com.farmers.ownfarmer.ui.allfarmers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.farmers.ownfarmer.R;
import com.farmers.ownfarmer.ui.allfarmers.DataModel.FarmersDataModel;
import com.farmers.ownfarmer.ui.home.FarmerDataActivity;

import java.util.ArrayList;
import java.util.List;


class AllFarmersRecyclerAdapter extends RecyclerView.Adapter<AllFarmersRecyclerAdapter.MyViewHolder>  {


    Activity activity;
    private List<FarmersDataModel> farmersDataModel = new ArrayList<>();
    ProgressDialog progress;

    public AllFarmersRecyclerAdapter(Activity activity, List<FarmersDataModel> farmersDataModel) {
        this.activity = activity;
        this.farmersDataModel = farmersDataModel;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.all_farmers_recycler_item , parent , false);
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, FarmerDataActivity.class);
                intent.putExtra("farmer_Name",farmersDataModel.get(position).getUserName());
                intent.putExtra("farmer_email",farmersDataModel.get(position).getUserEmail());
                intent.putExtra("farmer_phone",farmersDataModel.get(position).getUserPhone());
                intent.putExtra("farmer_address",farmersDataModel.get(position).getUserAddress());
                activity.startActivity(intent);

//                String product_id = farmersDataModel.get(position).getProductId();
//
//                ProductDetailFragment productDetailFragment = new ProductDetailFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("product_id", product_id);
//                productDetailFragment.setArguments(bundle);
//                FragmentTransaction transaction = ((MainActivity) activity).getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.nav_host_fragment, productDetailFragment ); // give your fragment container id in first parameter
//                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
//                transaction.commit();

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

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            //// initialization
            farmerImage_IV = itemView.findViewById(R.id.all_farmers_image_iv);
            farmerName_TV = itemView.findViewById(R.id.farmer_name_tv);
            farmerPhone_TV = itemView.findViewById(R.id.farmer_phone_tv);
            farmerAddress_TV = itemView.findViewById(R.id.farmer_address_tv);
        }
    }
}