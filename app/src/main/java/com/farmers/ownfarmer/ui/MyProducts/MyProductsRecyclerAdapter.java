package com.farmers.ownfarmer.ui.MyProducts;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.farmers.ownfarmer.ui.MyProducts.DataModel.MyProductsDataList;
import com.farmers.ownfarmer.ui.MyProducts.DataModel.MyProductsDataModel;
import com.farmers.ownfarmer.ui.chat.ChatRecyclerViewAdapter;
import com.farmers.ownfarmer.ui.editproduct.EditProductActivity;
import com.farmers.ownfarmer.ui.product.DataModel.ProductDataModel;
import com.farmers.ownfarmer.ui.productdetail.ProductDetailFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class MyProductsRecyclerAdapter extends RecyclerView.Adapter<MyProductsRecyclerAdapter.MyViewHolder>  {


    Activity activity;
    private List<MyProductsDataModel> productsDataModel;
    ProgressDialog progress;

    public MyProductsRecyclerAdapter(Activity activity, List<MyProductsDataModel> productsDataModel) {
        this.activity = activity;
        this.productsDataModel = productsDataModel;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.my_products_recycler_item , parent , false);
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
                .into((ImageView) holder.productImage_IV);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
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



        //// delete product
        holder.delete_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progress.show();

                String product_id = productsDataModel.get(position).getProductId();

                deleteMyProducts(product_id);

            }
        });




        /// edit product
        holder.edit_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, EditProductActivity.class);
                intent.putExtra("product_id", productsDataModel.get(position).getProductId());
                activity.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return productsDataModel.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage_IV;
        TextView productName_TV, productPrice_TV, productUnit_TV, delete_TV, edit_TV;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            //// initialization
            productImage_IV = itemView.findViewById(R.id.my_product_image_iv);
            productName_TV = itemView.findViewById(R.id.my_product_title_tv);
            productPrice_TV = itemView.findViewById(R.id.my_product_price_tv);
            productUnit_TV = itemView.findViewById(R.id.my_product_unit_tv);
            delete_TV = itemView.findViewById(R.id.delete_tv);
            edit_TV = itemView.findViewById(R.id.edit_tv);


        }
    }



    ///////////////// Function used to delete my products
    private void deleteMyProducts(String productID) {

        ApiService apiService = ApiClient.getClient(activity).create(ApiService.class);
        Call<DeleteDataList> call = apiService.deleteMyProduct(productID);

        call.enqueue(new Callback<DeleteDataList>() {
            @Override
            public void onResponse(Call<DeleteDataList> call, final Response<DeleteDataList> response) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DeleteDataList deleteDataList = response.body();
                        if (deleteDataList != null) {

                            int status = deleteDataList.getStatus();

                            if(status == 0){
                                progress.dismiss();
                                Toast.makeText(activity, deleteDataList.getMessage(), Toast.LENGTH_LONG).show();
                            }else if(status == 1) {

                                progress.dismiss();

                                Toast.makeText(activity, deleteDataList.getMessage(), Toast.LENGTH_LONG).show();

                                notifyDataSetChanged();


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
            public void onFailure(Call<DeleteDataList> call, Throwable t) {
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