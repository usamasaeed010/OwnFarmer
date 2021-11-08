package com.farmers.ownfarmer.ui.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.farmers.ownfarmer.Languages.ui.BaseActivity;
import com.farmers.ownfarmer.MainActivity;
import com.farmers.ownfarmer.R;
import com.farmers.ownfarmer.retrofit.ApiClient;
import com.farmers.ownfarmer.retrofit.ApiService;
import com.farmers.ownfarmer.ui.login.DataModel.LoginDataList;
import com.farmers.ownfarmer.ui.login.DataModel.LoginDataModel;
import com.farmers.ownfarmer.ui.login.LogInActivity;
import com.farmers.ownfarmer.ui.signup.DataModel.SignupDataList;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends BaseActivity {

    TextView toolbar_TV_name;

    String defLanguage;

    Button register_BTN;

    EditText name_ET, email_ET, phone_ET, address_ET, password_ET, confirmPassword_ET;

    ProgressDialog progress;

    RadioGroup userType_RG;

    RadioButton typeFarmer_RB, typeCustomer_RB;

    String userType = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0);
            defLanguage = locale.getLanguage();
        } else {
            //noinspection deprecation
            locale = getResources().getConfiguration().locale;
            defLanguage = locale.getLanguage();
        }


        ///// progress dialogue
        progress = new ProgressDialog(SignUpActivity.this);
        progress.setMessage("Please Wait");
        progress.setCancelable(true);


        ///////toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_activity);
        toolbar_TV_name =  findViewById(R.id.toolbar_TV_fragment_name);
        //////
        toolbar_TV_name.setTextColor(getResources().getColor(R.color.white));
        toolbar_TV_name.setText("");
        //////



        //// initialization
        name_ET = findViewById(R.id.signup_full_name_et);
        email_ET = findViewById(R.id.signup_email_et);
        phone_ET = findViewById(R.id.signup_number_et);
        address_ET = findViewById(R.id.signup_address_et);
        password_ET = findViewById(R.id.signup_password_et);
        confirmPassword_ET = findViewById(R.id.signup_confirm_password_et);
        register_BTN = findViewById(R.id.signup_btn_register);
        userType_RG = findViewById(R.id.user_type_rg);
        typeFarmer_RB = findViewById(R.id.farmer_rb);
        typeCustomer_RB = findViewById(R.id.customer_rb);



        if(defLanguage.contains("ur")){
            toolbar_TV_name.setText("پیچھے");
            toolbar.setNavigationIcon(R.drawable.ic_arrow_forward_white);
        } else{
            toolbar_TV_name.setText("Back");
            toolbar.setNavigationIcon(R.drawable.ic_back_arrow_white);
        }



        if(defLanguage.equals("ur")){

            name_ET.setGravity(Gravity.END);
            name_ET.setGravity(Gravity.RIGHT);
            email_ET.setGravity(Gravity.END);
            email_ET.setGravity(Gravity.RIGHT);
            phone_ET.setGravity(Gravity.END);
            phone_ET.setGravity(Gravity.RIGHT);
            address_ET.setGravity(Gravity.END);
            address_ET.setGravity(Gravity.RIGHT);
            password_ET.setGravity(Gravity.END);
            password_ET.setGravity(Gravity.RIGHT);
            confirmPassword_ET.setGravity(Gravity.END);
            confirmPassword_ET.setGravity(Gravity.RIGHT);

        } else{

            name_ET.setGravity(Gravity.START);
            name_ET.setGravity(Gravity.LEFT);
            email_ET.setGravity(Gravity.START);
            email_ET.setGravity(Gravity.LEFT);
            phone_ET.setGravity(Gravity.START);
            phone_ET.setGravity(Gravity.LEFT);
            address_ET.setGravity(Gravity.START);
            address_ET.setGravity(Gravity.LEFT);
            password_ET.setGravity(Gravity.START);
            password_ET.setGravity(Gravity.LEFT);
            confirmPassword_ET.setGravity(Gravity.START);
            confirmPassword_ET.setGravity(Gravity.LEFT);

        }



        ///////////////////// Radio Button Click Listener ////////////////
        userType_RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = (RadioButton) radioGroup.findViewById(i);

                View radioButton = rb.findViewById(i);
                int index = userType_RG.indexOfChild(radioButton);

                // Add logic here
                switch (index) {
                    case 0: // Farmer button
                        userType = "1";
                        break;
                    case 1: // Customer button
                        userType = "2";
                        break;
                }

            }
        });



        register_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(userType.equals("0")){

                    Toast.makeText(SignUpActivity.this, "Please Select User Type First.", Toast.LENGTH_LONG).show();

                } else{
                    if(validate()){

                        progress.show();
                        signupUser(name_ET.getText().toString(), email_ET.getText().toString(), password_ET.getText().toString(), phone_ET.getText().toString(), address_ET.getText().toString(), userType);

                    }
                }




            }
        });


    }



    ///////////////// Function used to sign up user
    private void signupUser(String name, String email, String password, String phone, String address, String user_type) {

        ApiService apiService = ApiClient.getClient(SignUpActivity.this).create(ApiService.class);
        Call<SignupDataList> call = apiService.signupUser(name, email, password, phone, address, user_type);

        call.enqueue(new Callback<SignupDataList>() {
            @Override
            public void onResponse(Call<SignupDataList> call, final Response<SignupDataList> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SignupDataList signupDataList = response.body();
                        if (signupDataList != null) {

                            int status = signupDataList.getStatus();

                            if(status == 0){
                                progress.dismiss();
                                Toast.makeText(SignUpActivity.this, signupDataList.getMessage(), Toast.LENGTH_LONG).show();
                            }else if(status == 1) {

                                LoginDataModel userLoginModel = signupDataList.getIsLogin();

                                /////// Shared Preference
                                SharedPreferences autologinsharedPreferences = getSharedPreferences("autologin_patient", MODE_PRIVATE);
                                SharedPreferences.Editor autologinedit = autologinsharedPreferences.edit();

                                autologinedit.putString("log_status", "login");

                                autologinedit.apply();

                                SharedPreferences patientDataSP = getSharedPreferences("user_data", MODE_PRIVATE);
                                SharedPreferences.Editor patientEdit = patientDataSP.edit();

                                patientEdit.putString("user_id", userLoginModel.getUserId());
                                patientEdit.putString("user_type", userLoginModel.getUserType());
                                patientEdit.putString("user_name", userLoginModel.getUserName());
                                patientEdit.putString("user_email", userLoginModel.getUserEmail());
                                patientEdit.putString("user_phone", userLoginModel.getUserPhone());
                                patientEdit.putString("user_address", userLoginModel.getUserAddress());
                                patientEdit.putString("user_image", userLoginModel.getUserImage());

                                patientEdit.apply();

                                progress.dismiss();

                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                startActivity(intent);


                            } else{
                                progress.dismiss();
                                Toast.makeText(SignUpActivity.this, "Connecting ....", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            progress.dismiss();
                            Toast.makeText(SignUpActivity.this, "Connecting ....", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<SignupDataList> call, Throwable t) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        Toast.makeText(SignUpActivity.this, "Connecting ...", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }






    /////// validation
    private boolean validate(){

        if(name_ET.getText().toString().trim()==null || name_ET.getText().toString().trim().isEmpty()){
            Toast.makeText(SignUpActivity.this,"Name is Required",Toast.LENGTH_LONG).show();
        } else if(email_ET.getText().toString().trim()==null || email_ET.getText().toString().trim().isEmpty()){
            Toast.makeText(SignUpActivity.this,"Email is Required",Toast.LENGTH_LONG).show();
        } else if(phone_ET.getText().toString().trim()==null || phone_ET.getText().toString().trim().isEmpty()){
            Toast.makeText(SignUpActivity.this,"Phone Number is Required",Toast.LENGTH_LONG).show();
        } else if(address_ET.getText().toString().trim()==null || address_ET.getText().toString().trim().isEmpty()){
            Toast.makeText(SignUpActivity.this,"Address is Required",Toast.LENGTH_LONG).show();
        } else if(password_ET.getText().toString().trim()==null || password_ET.getText().toString().trim().isEmpty()){
            Toast.makeText(SignUpActivity.this,"Password is Required",Toast.LENGTH_LONG).show();
        } else if(confirmPassword_ET.getText().toString().trim()==null || confirmPassword_ET.getText().toString().trim().isEmpty()){
            Toast.makeText(SignUpActivity.this,"Confirm Password is Required",Toast.LENGTH_LONG).show();
        } else if(!confirmPassword_ET.getText().toString().matches(password_ET.getText().toString())){
            Toast.makeText(SignUpActivity.this,"Password must be same",Toast.LENGTH_LONG).show();
        } else {
            return true;
        }
        return false;

    }


}