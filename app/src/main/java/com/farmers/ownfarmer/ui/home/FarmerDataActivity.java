package com.farmers.ownfarmer.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.farmers.ownfarmer.R;
import com.farmers.ownfarmer.ui.chat.ChatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FarmerDataActivity extends AppCompatActivity {

    Button chat_BTN;
    TextView name_TV, email_TV, phoneNumber_TV, address_TV;
    /////firebase
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;


    String target_user_name, target_user_id, target_user_image;
    SharedPreferences user_preference;
    String user_id, user_name, user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_data);


        FirebaseApp.initializeApp(getApplicationContext());

        ///////////firebase
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        chat_BTN = findViewById(R.id.chat_profile_tv_farmer);
        name_TV = findViewById(R.id.name_profile_tv_farmer);
        email_TV = findViewById(R.id.email_profile_tv_farmer);
        phoneNumber_TV = findViewById(R.id.phone_profile_tv_farmer);
        address_TV = findViewById(R.id.my_address_tv_farmer);

        Intent intent = getIntent();
        String farmer_Name = intent.getStringExtra("farmer_Name");
        String farmer_email = intent.getStringExtra("farmer_email");
        String farmer_phone = intent.getStringExtra("farmer_phone");
        String farmer_address = intent.getStringExtra("farmer_address");

        name_TV.setText(farmer_Name);
        email_TV.setText(farmer_email);
        phoneNumber_TV.setText(farmer_phone);
        address_TV.setText(farmer_address);


        //// go to chat
        chat_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall(farmer_phone);
            }
        });
    }

    ///// call
    private void makePhoneCall(String contact){
        if(contact!=null && !contact.isEmpty()){
            Intent intent=new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:"+contact));
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity)getApplicationContext(), new String[]{Manifest.permission.CALL_PHONE},1);
            }
            else
            {
                startActivity(intent);
            }
        }
        else {
            Toast.makeText(getApplicationContext(),"No Contact Number Found",Toast.LENGTH_LONG);
        }
    }

}