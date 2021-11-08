package com.farmers.ownfarmer.ui.editproduct;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.content.CursorLoader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.farmers.ownfarmer.Languages.ui.BaseActivity;
import com.farmers.ownfarmer.MainActivity;
import com.farmers.ownfarmer.R;
import com.farmers.ownfarmer.retrofit.ApiClient;
import com.farmers.ownfarmer.retrofit.ApiService;
import com.farmers.ownfarmer.ui.Utility;
import com.farmers.ownfarmer.ui.addproduct.AddProductActivity;
import com.farmers.ownfarmer.ui.addproduct.DataModel.AddDataList;
import com.farmers.ownfarmer.ui.editservice.DataModel.EditDataList;
import com.farmers.ownfarmer.ui.editservice.EditServiceActivity;
import com.farmers.ownfarmer.ui.productdetail.DataModel.ProductDetailDataList;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProductActivity extends BaseActivity {

    TextView toolbar_TV_name;

    String defLanguage;

    ProgressDialog progress;

    SharedPreferences user_preference;
    String user_id, product_id;

    ImageView product_IV, upload_IV;
    EditText productName_ET, productPrice_ET, productUnit_ET, productDescription_ET;
    Button updateProduct_BTN;

    ///////////For Image
    Uri selectedThumbnail;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0;
    Bitmap thumbnail;
    ///Gallery
    private static int RESULT_LOAD_IMAGE = 1;
    int check_upload_image = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0);
            defLanguage = locale.getLanguage();
        } else {
            //noinspection deprecation
            locale = getResources().getConfiguration().locale;
            defLanguage = locale.getLanguage();
        }

        ///////toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_activity);
        toolbar_TV_name =  findViewById(R.id.toolbar_TV_fragment_name);
        //////
        toolbar_TV_name.setTextColor(getResources().getColor(R.color.white));


        ///////////////////////////////////////////////////// Back
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) { EditProductActivity.super.finish(); }});
        ///////////////////////////////////////////////////////


        if(defLanguage.contains("en")){
            toolbar_TV_name.setText("Edit Product");
            //////
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        } else{
            toolbar_TV_name.setText("سروس شامل کریں");
            //////
            toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_forward_24);
        }


        ///// progress dialogue
        progress = new ProgressDialog(EditProductActivity.this);
        progress.setMessage("Please Wait");
        progress.setCancelable(true);


        //// check user type
        user_preference = getSharedPreferences("user_data", MODE_PRIVATE);
        user_id = user_preference.getString("user_id", null);



        /////// initialization
        product_IV = findViewById(R.id.product_img);
        upload_IV = findViewById(R.id.update_pic_IMG_product);
        productName_ET = findViewById(R.id.edit_product_name_et);
        productPrice_ET = findViewById(R.id.edit_product_price_et);
        productUnit_ET = findViewById(R.id.edit_product_unit_et);
        productDescription_ET = findViewById(R.id.edit_product_description_et);
        updateProduct_BTN = findViewById(R.id.edit_product_btn);


        /////////get Values Throuh Intent
        Intent intent = getIntent();
        product_id = intent.getStringExtra("product_id");



        /// get image
        upload_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedThumbnail == null) {
                    requestPermission();
                    selectImage();
                } else{
                    selectImage();
                }

            }
        });




        progress.show();
        getProductDetail(product_id);




        //// update product
        updateProduct_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validation()){

                    progress.show();

                    updateProduct(productName_ET.getText().toString(), productPrice_ET.getText().toString(), productUnit_ET.getText().toString(), productDescription_ET.getText().toString(), product_id);

                }

            }
        });



    }



    /////////////////////////// edit product
    public void updateProduct(String p_name, String p_price, String p_unit, String p_description, String p_id){

        ApiService apiService = ApiClient.getClient(EditProductActivity.this).create(ApiService.class);
        Call<EditDataList> call = apiService.updateProduct(p_name, p_price, p_unit, p_description, p_id);

        call.enqueue(new Callback<EditDataList>() {

            @Override
            public void onResponse(Call<EditDataList> call, Response<EditDataList> response) {
                final EditDataList editProductList = response.body();

                if (editProductList != null) {

                    int status = editProductList.getStatus();

                    if (status == 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.dismiss();
                                Toast.makeText(EditProductActivity.this, editProductList.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } else if(status == 1) {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                progress.dismiss();

                                Toast.makeText(EditProductActivity.this, editProductList.getMessage(), Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(EditProductActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.dismiss();
                                Toast.makeText(EditProductActivity.this, "Please Check Your Internet Connection.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress.dismiss();
                            Toast.makeText(EditProductActivity.this, "Please Check Your Internet Connection.", Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<EditDataList> call, Throwable t) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();

                        String msg = t.getMessage().toString();

                        Log.e("error : --->   ", msg);

                        Toast.makeText(EditProductActivity.this, msg, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }







    ///////////////// Function used to get products
    private void getProductDetail(String product_id) {

        ApiService apiService = ApiClient.getClient(EditProductActivity.this).create(ApiService.class);
        Call<ProductDetailDataList> call = apiService.getProductDetail(product_id);

        call.enqueue(new Callback<ProductDetailDataList>() {
            @Override
            public void onResponse(Call<ProductDetailDataList> call, final Response<ProductDetailDataList> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ProductDetailDataList productDataList = response.body();
                        if (productDataList != null) {

                            int status = productDataList.getStatus();

                            if(status == 0){
                                progress.dismiss();
                                Toast.makeText(EditProductActivity.this, productDataList.getMessage(), Toast.LENGTH_LONG).show();
                            }else if(status == 1) {

                                if(productDataList.getProduct()!=null){

                                    if(productDataList.getProduct().getProductImage()!=null && !productDataList.getProduct().getProductImage().isEmpty()){

                                        Glide.with(EditProductActivity.this)
                                                .load(getResources().getString(R.string.image_url) + productDataList.getProduct().getProductImage())
                                                .apply(RequestOptions.placeholderOf(R.drawable.splash))
                                                .apply(RequestOptions.skipMemoryCacheOf(false))
                                                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA))
                                                .into((ImageView) product_IV);

                                        selectedThumbnail = Uri.parse(productDataList.getProduct().getProductImage());

                                    } else{

                                        Glide.with(EditProductActivity.this)
                                                .load(getResources().getString(R.string.image_url) + productDataList.getProduct().getProductImage())
                                                .apply(RequestOptions.placeholderOf(R.drawable.splash))
                                                .apply(RequestOptions.skipMemoryCacheOf(false))
                                                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA))
                                                .into((ImageView) product_IV);

                                    }

                                    if(productDataList.getProduct().getProductName()!=null && !productDataList.getProduct().getProductName().isEmpty()){
                                        productName_ET.setText(productDataList.getProduct().getProductName());
                                    } else{
                                        productName_ET.setText("N/A");
                                    }

                                    if(productDataList.getProduct().getProductUnit()!=null && !productDataList.getProduct().getProductUnit().isEmpty()){
                                        productUnit_ET.setText(productDataList.getProduct().getProductUnit());
                                    } else{
                                        productUnit_ET.setText("N/A");
                                    }

                                    if(productDataList.getProduct().getProductPrice()!=null && !productDataList.getProduct().getProductPrice().isEmpty()){
                                        productPrice_ET.setText(productDataList.getProduct().getProductPrice());
                                    } else{
                                        productPrice_ET.setText("N/A");
                                    }

                                    if(productDataList.getProduct().getProductDescription()!=null && !productDataList.getProduct().getProductDescription().isEmpty()){
                                        productDescription_ET.setText(productDataList.getProduct().getProductDescription());
                                    } else{
                                        productDescription_ET.setText("N/A");
                                    }

                                }

                                progress.dismiss();

                            } else{
                                progress.dismiss();
                                Toast.makeText(EditProductActivity.this, "Connecting ....", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            progress.dismiss();
                            Toast.makeText(EditProductActivity.this, "Connecting ....", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<ProductDetailDataList> call, Throwable t) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        Toast.makeText(EditProductActivity.this, "Connecting ...", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }




    /////////// Validation checks on edit texts //////////////////////
    private boolean validation() {

        if (productName_ET.getText().toString().trim()==null || productName_ET.getText().toString().trim().isEmpty()) {
            Toast.makeText(EditProductActivity.this, "Please Enter Product Name", Toast.LENGTH_LONG).show();
            return false;
        } else if (productPrice_ET.getText().toString().trim()==null || productPrice_ET.getText().toString().trim().isEmpty()) {
            Toast.makeText(EditProductActivity.this, "Please Enter Product Price", Toast.LENGTH_LONG).show();
            return false;
        } else if (productUnit_ET.getText().toString().trim()==null || productUnit_ET.getText().toString().trim().isEmpty()) {
            Toast.makeText(EditProductActivity.this, "Please Enter Product Unit", Toast.LENGTH_LONG).show();
            return false;
        } else if (productDescription_ET.getText().toString().trim()==null || productDescription_ET.getText().toString().trim().isEmpty()) {
            Toast.makeText(EditProductActivity.this, "Please Enter Product Description", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }



    /*
     * This method is fetching the absolute path of the image file
     * if you want to upload other kind of files like .pdf, .docx
     * you need to make changes on this method only
     * Rest part will be the same
     * */
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(EditProductActivity.this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    public void requestPermission() {
        XXPermissions.with(EditProductActivity.this)
                //.constantRequest() //Can be set to continue to apply after being rejected until the user authorizes or permanently rejects
                //.permission(Permission.SYSTEM_ALERT_WINDOW, Permission.REQUEST_INSTALL_PACKAGES) //Support request 6.0 floating window permission 8.0 request installation permission
                .permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE) //Automatically get dangerous permissions in the list without specifying permissions
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            //   Toast.makeText(DailyProgressActivity.this, "Get Permissions success", Toast.LENGTH_SHORT).show();
                        } else {
                            //Toasty.error(SubmitStartInActivity.this, "The permission is successfully obtained, and some permissions are not granted normally.", Toast.LENGTH_SHORT, true).show();
                            Toast.makeText(EditProductActivity.this, "The permission is successfully obtained, and some permissions are not granted normally.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if (quick) {

                            Toast.makeText(EditProductActivity.this, "Authorized to be denied permanently, please grant permission manually", Toast.LENGTH_LONG).show();
                            finish();

                            //If it is permanently rejected, jump to the application rights system settings page
                            XXPermissions.gotoPermissionSettings(EditProductActivity.this);
                        } else {

                            Toast.makeText(EditProductActivity.this, "Failed to get permission", Toast.LENGTH_LONG).show();
                            finish();


                        }
                    }
                });

    }

    //////////////////////For Taking Picture Process////////////////////
    private void selectImage() {
        final CharSequence[] items = {"Pick Image From Camera", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProductActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(EditProductActivity.this);

                if (items[item].equals("Pick Image From Camera")) {
                    check_upload_image = 1;
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();
                }
                else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    ///////////////////For  Picture From Camera ////////////
    private void onCaptureImageResult(Intent data) {

        thumbnail = (Bitmap) data.getExtras().get("data");
        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
        selectedThumbnail = getImageUri(EditProductActivity.this, thumbnail);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());

            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        product_IV.setImageBitmap(thumbnail);

    }

    ///////////////////Funcation Used For Get Imge Uri from Bitmap ////////////
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "IMG_" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }

    ///////////////////For Taking Picture From Camera ////////////
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);


        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }


    }



}