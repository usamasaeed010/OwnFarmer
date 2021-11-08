package com.farmers.ownfarmer.ui.Messaging;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.farmers.ownfarmer.MainActivity;
import com.farmers.ownfarmer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MessageActivity extends AppCompatActivity {

    //////// Declaration of Variable,ImageView,TextView,SharedPreferences,Firebase etc////////
    Activity activity = MessageActivity.this;

    ImageView ivClose;

    RecyclerView messageRecyclerView;

    Messages_RecyclerAdapter mMessageAdapter;

    SharedPreferences apptheme_SharedPreferences, user_SharedPreference;

    String current_theme;
    /////////////////////// FIREBASSE ////////////////////////////
    FirebaseAuth firebaseAuth;

    FirebaseUser firebaseUser;

    DatabaseReference userExistenceReference;

    DatabaseReference reference;

    List<MessageUserList> mchats;


    String current_login_user_name, current_login_user_emmail, current_login_user_id, current_login_img;


    public static final String PREF_SKIP_LOGIN = "skip_login";
    public static final String PREF_ENCODED_TOKEN = "token";
    public static final String PREF_Firebase_Password = "firebasePass";
    public static final String PREF_DEVICE_TOKEN = "devicetoken";
    public static final String PREF_ONTIMETOKEN = "onetoken";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_message);


        ///////////////////////////////////////////////
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.green_background, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.green_background));
        }


        /////// Initialization of declared Variables above ////////
        ivClose = findViewById(R.id.toolbarback);



//        ///////// Click Listeners ///////
//        ivToolbar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MessageActivity.this, NewChatActivity.class);
//                startActivity(intent);
//            }
//        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(intent);
                finish();
            }
        });


        //////////////////// for firebase chat //////////////////

        user_SharedPreference = getSharedPreferences("user_data", MODE_PRIVATE);
        current_login_user_name = user_SharedPreference.getString("user_name", "");
        current_login_user_emmail = user_SharedPreference.getString("user_email", "");
        current_login_user_id = user_SharedPreference.getString("user_id", "");
        current_login_img = user_SharedPreference.getString("user_image", "");
        messageRecyclerView = findViewById(R.id.messages_RecyclerView);

        messageRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userExistenceReference = FirebaseDatabase.getInstance().getReference().child("Users");


        if (firebaseAuth.getCurrentUser() == null) {
            //  Toast.makeText(getApplicationContext(), "Login", Toast.LENGTH_SHORT).show();

        } else {
            VerifyUserExistence();
        }


        readMesssage();


    }

    ///////// Filter in list ///////
    private void filter(String text) {
        //new array list that will hold the filtered data
        List<MessageUserList> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (MessageUserList s : mchats) {
            //if the existing elements contains the search input
            if (s.getReceiverName().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        mMessageAdapter.filterList(filterdNames);
    }


    private void readMesssage() {
        mchats = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("ChatInbox");

        DatabaseReference queryRef = reference.child(current_login_user_id);


        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchats.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    MessageUserList chat = snapshot.getValue(MessageUserList.class);


                    mchats.add(chat);

                    mMessageAdapter = new Messages_RecyclerAdapter(MessageActivity.this, mchats);
                    messageRecyclerView.setAdapter(mMessageAdapter);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

       startActivity(intent);
       finish();
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }

    private void VerifyUserExistence() {
        String userId = firebaseUser.getUid();
        userExistenceReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("name").exists()) {
                    //     Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //  Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();

            }
        });
    }


}
