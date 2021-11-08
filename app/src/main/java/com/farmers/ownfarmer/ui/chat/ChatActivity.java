package com.farmers.ownfarmer.ui.chat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.farmers.ownfarmer.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class ChatActivity extends AppCompatActivity {

    //////// Declaration of Variable,ImageView,TextView,SharedPreferences etc////////
    private static ChatActivity instance;

    String AutoId;
    ArrayList<String> thumbnailPath = new ArrayList<>();

    SharedPreferences apptheme_SharedPreferences, user_SharedPreference;


    ImageView ivClose, ivProfile;
//    ArrayList<ImageFile>  list1;

    RecyclerView mMessageRecycler;

    private ChatRecyclerViewAdapter mMessageAdapter;

    EditText etPost;

    TextView tvEnd, tvTargetName;

    String chat_status,current_theme,target_user_name,file_type, target_user_product_id, target_user_id, target_user_image, current_login_user_name, current_login_user_emmail, current_login_user_id, current_login_img,message,media_IMG_Name;

    FirebaseAuth firebaseAuth;

    FirebaseUser firebaseUser;

    DatabaseReference userExistenceReference,reference;

    List<ChatDataModel> mchats;

    List<String> chat_Media;


//    ArrayList<AudioFile> list;
    File imgFile;

    ArrayList<String> filePathList = new ArrayList<>();

    ////Files
    File files;

    String Firebase_Server_Time = ServerValue.TIMESTAMP.toString();

    // Uri indicates, where the image will be picked from
    private Uri filePath;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;

    StorageReference storageReference;

    private static final int REQUEST_MEDIA = 100;

//    private MediaItem mMediaItem;

    public static final String PREF_USERNAME = "user";
    public static final String PREF_USEREMAIL = "email";
    public static final String PREF_USER_ID = "1";
    public static final String PREF_PHONE = "phine";
    public static final String PREF_IMAGE = "imgback";
    public static final String PREFERENCE = "preference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        /////// Shared Preferences (Initialization) => For Local Data Storage  ////////
        apptheme_SharedPreferences = getSharedPreferences("App_theme_info", MODE_PRIVATE);
        current_theme = apptheme_SharedPreferences.getString("current_theme", null);

        /////// Initialization of declared Variables above ////////
        etPost = findViewById(R.id.post_tag_edit_text);
        ivClose = findViewById(R.id.back_arrow);
        tvEnd = findViewById(R.id.end);
        tvTargetName = findViewById(R.id.chat_target_name);
        ivProfile = findViewById(R.id.profile_image2_chat);
        mMessageRecycler = findViewById(R.id.reyclerview_message_list_ok);

        //// get token from sp
        user_SharedPreference = getSharedPreferences("user_data", MODE_PRIVATE);
        current_login_user_name = user_SharedPreference.getString("user_name", "");
        current_login_user_emmail = user_SharedPreference.getString("user_email", "");
        current_login_user_id = user_SharedPreference.getString("user_id", "");
        current_login_img = user_SharedPreference.getString("user_image", "");


        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        Intent intent = getIntent();

        target_user_name = intent.getStringExtra("target_user_name");
        target_user_id = intent.getStringExtra("target_user_id");
        target_user_product_id = intent.getStringExtra("target_user_product_id");
        target_user_image = intent.getStringExtra("target_user_image");
        chat_status = intent.getStringExtra("chat_status");



        Glide.with(ChatActivity.this)
                .load(getResources().getString(R.string.image_url) + target_user_image)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_baseline_person_24))
                .apply(RequestOptions.skipMemoryCacheOf(false))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA))
                .into((ImageView) ivProfile);


        tvTargetName.setText(target_user_name);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userExistenceReference = FirebaseDatabase.getInstance().getReference().child("Users");

        if(chat_status==null || chat_status.contains("Pending"))
        {
            chat_status = "Pending";
        }


        if (firebaseAuth.getCurrentUser() == null) {

        } else {
            VerifyUserExistence();
        }



        ///// for reciveing msgs /////
        readMesssage();


        mMessageRecycler.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                mMessageRecycler.scrollToPosition(mchats.size() - 1);

            }
        });


        etPost.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                if (s != null) {
                    if (s.toString().isEmpty()) {
                        tvEnd.setVisibility(View.GONE);
                    } else {
                        tvEnd.setVisibility(View.VISIBLE);
                    }
                }
            }
        });



        tvEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                message = etPost.getText().toString();
                if (!TextUtils.isEmpty(message)) {
                    Calendar c = Calendar.getInstance();


                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = df.format(c.getTime());


                    sendmessage(target_user_product_id, current_login_user_name, current_login_img, target_user_name, target_user_image, current_login_user_id, target_user_id, formattedDate, message);
                    etPost.setText("");
                } else {
                    etPost.setError("Empty not allowed");
                }
            }
        });


        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));

        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.green_borders, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.green_borders));
        }

    }





    public void sendmessage(String order, String sender, String sender_img, String receiver, String recevier_img, String senderUid, String receiverUid, String time, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        AutoId = reference.push().getKey();


//        DatabaseReference newDatabaseReference = reference.child("ChatMessages").push();
//      String id =  newDatabaseReference.getKey();
        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("isSeen", false);
        hashMap.put("messageFrom", senderUid);
        hashMap.put("messageId", AutoId);
        hashMap.put("mediaUrl", "");
        hashMap.put("mediaName", "");
        hashMap.put("mediaThumbnail", "");
        hashMap.put("mediaThumbnailName", "");
        hashMap.put("messageText", message);
        hashMap.put("messageTime", ServerValue.TIMESTAMP);
        hashMap.put("messageTo", receiverUid);
        hashMap.put("messageType", "TEXT");


        HashMap<String, Object> hashMap12 = new HashMap<>();

        hashMap12.put("isSeen", false);

        hashMap12.put("messageFrom", senderUid);
        hashMap12.put("messageId", AutoId);
        hashMap12.put("mediaUrl", "");
        hashMap12.put("mediaName", "");
        hashMap12.put("mediaThumbnail", "");
        hashMap12.put("mediaThumbnailName", "");
        hashMap12.put("messageText", message);
        hashMap12.put("messageTime", ServerValue.TIMESTAMP);
        hashMap12.put("messageTo", receiverUid);
        hashMap12.put("messageType", "TEXT");


        HashMap<String, Object> hashMap1 = new HashMap<>();

        hashMap1.put("senderName", sender);
        hashMap1.put("senderImage", sender_img);
        hashMap1.put("receiverName", receiver);
        hashMap1.put("recevierImage", recevier_img);


        hashMap1.put("isSeen", false);
        hashMap1.put("messageFrom", senderUid);
        hashMap1.put("messageId", AutoId);
        hashMap1.put("chatStatus",chat_status);
        hashMap1.put("mediaUrl", "");
        hashMap1.put("mediaName", "");
        hashMap1.put("mediaThumbnail", "");
        hashMap1.put("mediaThumbnailName", "");
        hashMap1.put("messageText", message);
        hashMap1.put("messageTime", ServerValue.TIMESTAMP);
        hashMap1.put("messageTo", receiverUid);
        hashMap1.put("messageType", "TEXT");


        HashMap<String, Object> hashMap10 = new HashMap<>();

        hashMap10.put("senderName", sender);
        hashMap10.put("chatStatus",chat_status);
        hashMap10.put("senderImage", sender_img);
        hashMap10.put("receiverName", receiver);
        hashMap10.put("recevierImage", recevier_img);
        hashMap10.put("isSeen", false);
        hashMap10.put("messageFrom", senderUid);
        hashMap10.put("messageId", AutoId);
        hashMap10.put("mediaUrl", "");
        hashMap10.put("mediaName", "");
        hashMap10.put("mediaThumbnail", "");
        hashMap10.put("mediaThumbnailName", "");
        hashMap10.put("messageText", message);
        hashMap10.put("messageTime", ServerValue.TIMESTAMP);
        hashMap10.put("messageTo", receiverUid);
        hashMap10.put("messageType", "TEXT");


        reference.child("ChatInbox").child(senderUid).child(receiverUid).setValue(hashMap1);

        reference.child("ChatInbox").child(receiverUid).child(senderUid).setValue(hashMap10);

        reference.child("ChatMessages").child(senderUid).child(receiverUid).child(AutoId).setValue(hashMap);
        reference.child("ChatMessages").child(receiverUid).child(senderUid).child(AutoId).setValue(hashMap12);

        readMesssage();

    }

    private void readMesssage() {

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("ChatInbox");
        DatabaseReference queryRef = reference1.child(current_login_user_id).child(target_user_id);
        queryRef.child("isSeen").setValue(true);
        mchats = new ArrayList<>();
        chat_Media = new ArrayList<String>();
        reference = FirebaseDatabase.getInstance().getReference("ChatMessages");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mchats.clear();
                chat_Media.clear();


                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    String mvalue = itemSnapshot.getKey();
                    if (mvalue != null) {
                        for (DataSnapshot itemSnapshot2 : itemSnapshot.getChildren()) {
                            String myv = itemSnapshot2.getKey();
//                                ChatDataModel c = itemSnapshot2.getValue(ChatDataModel.class);
//                                mchats.add(c);
                            //     Iterable<DataSnapshot> matchSnapShot = itemSnapshot2.getChildren();
                            for (DataSnapshot match : itemSnapshot2.getChildren()) {
                                if (mvalue.equals(current_login_user_id) && myv.equals(target_user_id)) {
                                    ChatDataModel c = match.getValue(ChatDataModel.class);
                                    mchats.add(c);
                                    Log.e("TGED", "User key" + match.getKey());
                                    Log.e("TGED", "User ref" + match.getRef().toString());
                                    Log.e("TGED", "User val" + match.getValue().toString());
                                    Log.e("TGED", "current_login_user_id " + current_login_user_id);
                                    Log.e("TGED", "target_user_id " + target_user_id);

                                    if (!c.getMessageFrom().equals(current_login_user_id)) {
                                        match.getRef().child("isSeen").setValue(true);
                                        //  DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("ChatInbox");
//                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("ChatInbox").child(current_login_user_id).child(target_user_id);
//                                        reference2.child("isSeen").setValue(false);
//                                        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("ChatInbox").child(target_user_id).child(current_login_user_id);
//                                        reference3.child("isSeen").setValue(true);
//                                        Log.e("TGED", "tFirebaseDatabase ==>>>>>    " + FirebaseDatabase.getInstance().getReference("ChatInbox").child(target_user_id).child(current_login_user_id).child("isSeen").setValue(true));
                                    }


                                    ////// for Media //////////////
                                    if (c.getMessageType().equals("IMAGE")) {
                                        chat_Media.add(c.getMediaUrl());
                                    } else if (c.getMessageType().equals("AUDIO")) {
                                        chat_Media.add(c.getMediaUrl());
                                    } else if (c.getMessageType().equals("VIDEO")) {
                                        chat_Media.add(c.getMediaUrl());
                                    }

                                }
                            }
                        }

//                        reference = FirebaseDatabase.getInstance().getReference("ChatInbox");
//
//                        DatabaseReference queryRef = reference.child(current_login_user_id).child(target_user_id);


//                        queryRef.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                    queryRef.child("isSeen").setValue(true);
////                                    snapshot.child("isSeen").getRef().setValue(true);
//
//                                }
//
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });


                    }


                    mMessageAdapter = new ChatRecyclerViewAdapter(ChatActivity.this, mchats, target_user_image);
                    mMessageRecycler.setAdapter(mMessageAdapter);

                    mMessageAdapter.notifyDataSetChanged();

                }


                mMessageRecycler.scrollToPosition(mchats.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

    public static ChatActivity getInstance() {
        return instance;
    }

}