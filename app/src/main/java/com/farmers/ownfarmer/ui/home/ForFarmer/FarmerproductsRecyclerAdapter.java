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
import com.farmers.ownfarmer.ui.MyProducts.DataModel.MyProductsDataModel;
import com.farmers.ownfarmer.ui.editproduct.EditProductActivity;
import com.farmers.ownfarmer.ui.productdetail.ProductDetailFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FarmerproductsRecyclerAdapter extends RecyclerView.Adapter<FarmerproductsRecyclerAdapter.MyViewHolder>  {


    Activity activity;
    private List<MyProductsDataModel> productsDataModel;
    ProgressDialog progress;

    public FarmerproductsRecyclerAdapter(Activity activity, List<MyProductsDataModel> productsDataModel) {
        this.activity = activity;
        this.productsDataModel = productsDataModel;
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
        

        if(productsDataModel.get(position).getProductName()!=null && !productsDataModel.get(position).getProductName().isEmpty()){
            holder.productName_TV.setText(productsDataModel.get(position).getProductName());
        } else{
            holder.productName_TV.setText("N/A");
        }

        if(productsDataModel.get(position).getProductPrice()!=null && !productsDataModel.get(position).getProductPrice().isEmpty()){
            holder.productPrice_TV.setText("Price : Rs. "+productsDataModel.get(position).getProductPrice());
        } else{
            holder.productPrice_TV.setText("Price : N/A");
        }

        if(productsDataModel.get(position).getProductUnit()!=null && !productsDataModel.get(position).getProductUnit().isEmpty()){
            holder.productPrice_TV.setText("Unit : "+productsDataModel.get(position).getProductUnit());
        } else{
            holder.productPrice_TV.setText("Unit : N/A");
        }


        Glide.with(activity)
                .load(activity.getResources().getString(R.string.image_url) + productsDataModel.get(position).getProductImage())
                .apply(RequestOptions.placeholderOf(R.drawable.splash))
                .apply(RequestOptions.skipMemoryCacheOf(false))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA))
                .into(holder.productImage_IV);



        holder.add_cart_button_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String product_id = productsDataModel.get(position).getProductId();

                ProductDetailFragment productDetailFragment = new ProductDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("product_id", product_id);
                productDetailFragment.setArguments(bundle);
                FragmentTransaction transaction = ((MainActivity) activity).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, productDetailFragment ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return productsDataModel.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage_IV;
        Button add_cart_button_product;
        TextView productName_TV, productPrice_TV, productUnit_TV, delete_TV, edit_TV;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            //// initialization
            productImage_IV = itemView.findViewById(R.id.img_home_all_farmer_home);
            productName_TV = itemView.findViewById(R.id.product_name_tv_home);
            productPrice_TV = itemView.findViewById(R.id.product_price_tv_home);
            productUnit_TV = itemView.findViewById(R.id.farmer_descript_tv_home);
            add_cart_button_product = itemView.findViewById(R.id.add_cart_button_product);
        }
    }

}