package com.farmers.ownfarmer.ui.product;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.farmers.ownfarmer.ui.services.DataModel.ServicesDataModel;

import java.util.ArrayList;
import java.util.List;

class ProductsRecyclerAdapter extends RecyclerView.Adapter<ProductsRecyclerAdapter.MyViewHolder> implements Filterable {


    Activity activity;
    private List<ProductDataModel> productsDataModel;
    private List<ProductDataModel> searchProductsDataModelFull;

    public ProductsRecyclerAdapter(Activity activity, List<ProductDataModel> productsDataModel) {
        this.activity = activity;
        this.productsDataModel = productsDataModel;
        this.searchProductsDataModelFull = productsDataModel;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.products_recycler_item , parent , false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {


        if(productsDataModel.get(position).getProductName()!=null && !productsDataModel.get(position).getProductName().isEmpty()){
            holder.productName_TV.setText(productsDataModel.get(position).getProductName());
        } else{
            holder.productName_TV.setText("N/A");
        }

        if(productsDataModel.get(position).getProductPrice()!=null && !productsDataModel.get(position).getProductPrice().isEmpty()){
            holder.productPrice_TV.setText("Rs. "+productsDataModel.get(position).getProductPrice());
        } else{
            holder.productPrice_TV.setText("N/A");
        }


        Glide.with(activity)
                .load(activity.getResources().getString(R.string.image_url) + productsDataModel.get(position).getProductImage())
                .apply(RequestOptions.placeholderOf(R.drawable.splash))
                .apply(RequestOptions.skipMemoryCacheOf(false))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA))
                .into((ImageView) holder.productImage_IV);



        holder.viewDetail_BTN.setOnClickListener(new View.OnClickListener() {
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


    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<ProductDataModel> filteredList = new ArrayList<>();
                if (charSequence == null || charSequence.length() == 0){
                    filteredList.addAll(searchProductsDataModelFull);

                }
                else {
                    String filterPattern = charSequence.toString().toLowerCase().trim();

                    for(ProductDataModel items : searchProductsDataModelFull){
                        if (items.getProductName().toLowerCase().contains(filterPattern))
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

                searchProductsDataModelFull.clear();
                searchProductsDataModelFull.addAll((List)filterResults.values);
                notifyDataSetChanged();

            }
        };
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView productName_TV, productPrice_TV;
        Button viewDetail_BTN;
        ImageView productImage_IV;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            //// initialization
            productName_TV = itemView.findViewById(R.id.cat_product_name_tv);
            productPrice_TV = itemView.findViewById(R.id.cat_product_price_tv);
            viewDetail_BTN = itemView.findViewById(R.id.view_detail_btn);
            productImage_IV = itemView.findViewById(R.id.cat_product_img);

        }
    }



}