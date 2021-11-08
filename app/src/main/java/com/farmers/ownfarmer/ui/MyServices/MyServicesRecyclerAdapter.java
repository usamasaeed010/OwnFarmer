package com.farmers.ownfarmer.ui.MyServices;

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
import com.farmers.ownfarmer.ui.MyServices.DataModel.MyServicesDataModel;
import com.farmers.ownfarmer.ui.editproduct.EditProductActivity;
import com.farmers.ownfarmer.ui.editservice.EditServiceActivity;
import com.farmers.ownfarmer.ui.servicedetail.ServiceDetailFragment;
import com.farmers.ownfarmer.ui.services.DataModel.ServicesDataModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class MyServicesRecyclerAdapter extends RecyclerView.Adapter<MyServicesRecyclerAdapter.MyViewHolder>  {


    Activity activity;
    private List<MyServicesDataModel> servicesDataModel;
    ProgressDialog progress;

    public MyServicesRecyclerAdapter(Activity activity, List<MyServicesDataModel> servicesDataModel) {
        this.activity = activity;
        this.servicesDataModel = servicesDataModel;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.my_services_recycler_item , parent , false);
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



        //// delete_service
        holder.deleteService_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progress.show();

                String service_id = servicesDataModel.get(position).getServiceId();

                deleteMyService(service_id);
                activity.overridePendingTransition(0,0);
                activity.finish();
            }
        });




        /// edit service
        holder.editService_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, EditServiceActivity.class);
                intent.putExtra("service_id", servicesDataModel.get(position).getServiceId());
                activity.startActivity(intent);

            }
        });




    }

    @Override
    public int getItemCount() {
        return servicesDataModel.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView myserviceName_TV, myServicePrice_TV, deleteService_TV, editService_TV;
        ImageView myserviceImage_IV;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            //// initialization
            myserviceImage_IV = itemView.findViewById(R.id.my_service_image_iv);
            myserviceName_TV = itemView.findViewById(R.id.my_service_title_tv);
            myServicePrice_TV = itemView.findViewById(R.id.my_service_price_tv);
            deleteService_TV = itemView.findViewById(R.id.delete_tv);
            editService_TV = itemView.findViewById(R.id.edit_tv);

        }
    }



    ///////////////// Function used to delete my service
    private void deleteMyService(String serviceID) {

        ApiService apiService = ApiClient.getClient(activity).create(ApiService.class);
        Call<DeleteDataList> call = apiService.deleteMyService(serviceID);

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