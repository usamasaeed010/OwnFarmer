package com.farmers.ownfarmer.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.farmers.ownfarmer.Languages.ui.BaseActivity;
import com.farmers.ownfarmer.MainActivity;
import com.farmers.ownfarmer.R;
import com.farmers.ownfarmer.retrofit.ApiClient;
import com.farmers.ownfarmer.retrofit.ApiService;
import com.farmers.ownfarmer.ui.login.DataModel.LoginDataList;
import com.farmers.ownfarmer.ui.login.DataModel.LoginDataModel;
import com.farmers.ownfarmer.ui.signup.SignUpActivity;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInActivity extends BaseActivity {

    Button login_BTN;
    TextView signup_TV;
    EditText email_ET, password_ET;

    String defLanguage;

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

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
        progress = new ProgressDialog(LogInActivity.this);
        progress.setMessage("Please Wait");
        progress.setCancelable(true);


        ////// initialization
        login_BTN = findViewById(R.id.login_button_btn);
        signup_TV = findViewById(R.id.dont_have_account_login_tv);
        email_ET = findViewById(R.id.login_email_or_phone_et);
        password_ET = findViewById(R.id.login_password_et);



        if(defLanguage.equals("ur")){

            email_ET.setGravity(Gravity.END);
            email_ET.setGravity(Gravity.RIGHT);
            password_ET.setGravity(Gravity.END);
            password_ET.setGravity(Gravity.RIGHT);

        } else{

            email_ET.setGravity(Gravity.START);
            email_ET.setGravity(Gravity.LEFT);
            password_ET.setGravity(Gravity.START);
            password_ET.setGravity(Gravity.LEFT);

        }



        login_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(validate()){

                    progress.show();
                    loginUser(email_ET.getText().toString(),
                            password_ET.getText().toString());

                }

            }
        });



        signup_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(intent);

            }
        });


    }


    ///////////////// Function used to log in user
    private void loginUser(String email, String password) {

        ApiService apiService = ApiClient.getClient(LogInActivity.this).create(ApiService.class);
        Call<LoginDataList> call = apiService.loginUser(email, password);

        call.enqueue(new Callback<LoginDataList>() {
            @Override
            public void onResponse(Call<LoginDataList> call, final Response<LoginDataList> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LoginDataList loginDataList = response.body();
                        if (loginDataList != null) {

                            int status = loginDataList.getStatus();

                            if(status == 0){
                                progress.dismiss();
                                Toast.makeText(LogInActivity.this, loginDataList.getMessage(), Toast.LENGTH_LONG).show();
                            }else if(status == 1) {

                                LoginDataModel userLoginModel = loginDataList.getIsLogin();

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

                                Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                                startActivity(intent);


                            } else{
                                progress.dismiss();
                                Toast.makeText(LogInActivity.this, "Connecting ....", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            progress.dismiss();
                            Toast.makeText(LogInActivity.this, "Connecting ....", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<LoginDataList> call, Throwable t) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        Toast.makeText(LogInActivity.this, "Connecting ...", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }




    /////// validation
    private boolean validate(){

        if(email_ET.getText().toString().trim()==null || email_ET.getText().toString().trim().isEmpty()){
            Toast.makeText(LogInActivity.this,"Email is Required",Toast.LENGTH_LONG).show();
        }else if(password_ET.getText().toString().trim()==null || password_ET.getText().toString().trim().isEmpty()){
            Toast.makeText(LogInActivity.this,"Password is Required",Toast.LENGTH_LONG).show();
        }else {
            return true;
        }
        return false;

    }


}