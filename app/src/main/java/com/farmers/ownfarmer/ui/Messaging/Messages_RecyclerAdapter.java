package com.farmers.ownfarmer.ui.Messaging;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.farmers.ownfarmer.R;
import com.farmers.ownfarmer.ui.chat.ChatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.farmers.ownfarmer.ui.chat.ChatActivity.PREFERENCE;


class Messages_RecyclerAdapter extends RecyclerSwipeAdapter<Messages_RecyclerAdapter.MyViewHolder> {

    //////// Declaration of Variable,ImageView,TextView,SharedPreferences,Firebase etc////////
    Activity context;

    List<MessageUserList> dataModelArrayList;

    SharedPreferences mSharedPreferences;

    /////////////////// For Chat Section ////////////////////
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    String  current_login_user_emmail, current_login_user_id,chat_status;
    String target_user_name, target_user_product_id, target_user_id, target_user_image;

    SharedPreferences user_SharedPreference;

    String user_id,userEmail,user_token;


    public Messages_RecyclerAdapter(Activity context, List<MessageUserList> dataModelArrayList) {
        this.context = context;
        this.dataModelArrayList = dataModelArrayList;
        user_SharedPreference = context.getSharedPreferences("user_data", MODE_PRIVATE);
        userEmail = user_SharedPreference.getString("user_email", "");
        user_id = user_SharedPreference.getString("user_id", "");
        user_token = user_SharedPreference.getString("token","");
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.messages_recycler_item_design, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        mSharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);

        //////////////////////// For FireBase Chat /////////////////////////

        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");

        MessageUserList  chat = dataModelArrayList.get(position);

        if(!user_id.equals(dataModelArrayList.get(position).getMessageFrom())) {
            holder.tvName.setText(chat.getSenderName());
            Glide.with(context)
                    .load("" + context.getResources().getString(R.string.image_url) + chat.getSenderImage())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_baseline_person_24))
                    .apply(RequestOptions.skipMemoryCacheOf(false))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA))
                    .into((ImageView) holder.ivUserImage);

        }
        else
        {
            holder.tvName.setText(chat.getReceiverName());
            Glide.with(context)
                    .load("" + context.getResources().getString(R.string.image_url) + chat.getRecevierImage())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_baseline_person_24))
                    .apply(RequestOptions.skipMemoryCacheOf(false))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA))
                    .into((ImageView) holder.ivUserImage);

        }

      if(chat.getMessageType()!=null) {
          if (chat.getMessageType().equals("IMAGE")) {
              holder.tvLastMessage.setText("IMAGE");
          } else if (chat.getMessageType().equalsIgnoreCase("VIDEO")) {
              holder.tvLastMessage.setText("VIDEO");

          } else if (chat.getMessageType().equalsIgnoreCase("AUDIO")) {
              holder.tvLastMessage.setText("AUDIO");

          } else {
              holder.tvLastMessage.setText(chat.getMessageText());
          }
      }
        String time = longToDateString(chat.getMessageTime(), "HH:mm a");

        chat_status = chat.getChatStatus();

        holder.tvLastMessageTime.setText(time);
//        Log.e("TGED", "LAST SEEN:  " + chat.getSeen());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /////////// Get Info of Product owner for chat //////////////

                if(!user_id.equals(dataModelArrayList.get(position).getMessageFrom()))
                {
                    target_user_name = dataModelArrayList.get(position).getSenderName();
                    target_user_id = dataModelArrayList.get(position).getMessageFrom();
                    target_user_image = dataModelArrayList.get(position).getSenderImage();
                    target_user_product_id = "";
                }
                else
                {
                    target_user_name = dataModelArrayList.get(position).getReceiverName();
                    target_user_id = dataModelArrayList.get(position).getMessageTo();
                    target_user_image = dataModelArrayList.get(position).getRecevierImage();
                    target_user_product_id = "";
                }
                SignInWIthFirebase(userEmail, userEmail);
//                if (mSharedPreferences.contains(PREF_SKIP_LOGIN)) {
//
//                    current_login_user_emmail = mSharedPreferences.getString(PREF_USEREMAIL, "");
//                    current_login_user_id = mSharedPreferences.getString(PREF_Firebase_Password, "");
//
////                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("ChatInbox").child(current_login_user_id).child(target_user_id);
////                    reference2.child("isSeen").setValue(true);
////                    DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("ChatInbox").child(target_user_id).child(current_login_user_id);
////                    reference3.child("isSeen").setValue(false);
//                    ///////////////// For FireBase Chat Sign in /////////////
//                    SignInWIthFirebase(current_login_user_emmail, current_login_user_id);
//
//
//                } else {
//                    Intent i = new Intent(context, LoginActivity.class);
//                    context.startActivity(i);
//                }
            }
        });
        Log.e("TGED", "CAHTIS>: " + chat.isSeen());
        if (!chat.isSeen()) {
            holder.tvBadge.setVisibility(View.VISIBLE);
        } else {
            holder.tvBadge.setVisibility(View.GONE);

        }
    }


    private void SignInWIthFirebase(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("target_user_id", target_user_id);
                if (target_user_name != null && !target_user_name.isEmpty()) {
                    intent.putExtra("target_user_name", target_user_name);
                } else {
                    intent.putExtra("target_user_name", target_user_name);
                }
                intent.putExtra("target_user_product_id", target_user_product_id);
                intent.putExtra("target_user_image", target_user_image);
                intent.putExtra("chat_status",chat_status);
                context.startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e.getMessage() != null && e.getMessage().equals("The email address is already in use by another account.")) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("target_user_id", target_user_id);
                    if (target_user_name != null && !target_user_name.isEmpty()) {
                        intent.putExtra("target_user_name", target_user_name);
                    } else {
                        intent.putExtra("target_user_name", target_user_name);
                    }

                    intent.putExtra("target_user_product_id", target_user_product_id);
                    intent.putExtra("target_user_image", target_user_image);
                    intent.putExtra("chat_status",chat_status);
                    context.startActivity(intent);

                } else {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.bag_Swipe_RV;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //////// Declaration of Variable,ImageView,TextView,SharedPreferences,Firebase etc////////
        LinearLayout linearLayout;

        ImageView ivUserImage;

        TextView tvName, tvLastMessage, tvLastMessageTime, tvBadge;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            //////// Initialization of Variable,ImageView,TextView,SharedPreferences,Firebase etc////////
            linearLayout = itemView.findViewById(R.id.linear);
            ivUserImage = itemView.findViewById(R.id.circleImage_user_message);
            tvName = itemView.findViewById(R.id.user_name_msg);
            tvLastMessage = itemView.findViewById(R.id.user_last_msg);
            tvLastMessageTime = itemView.findViewById(R.id.last_msg_time);
            tvBadge = itemView.findViewById(R.id.cart_badge);

        }
    }


    public static String longToDateString(long timestamp, String format) {
        return DateFormat.format(format, new Date(timestamp)).toString();
    }

    public void filterList( List<MessageUserList> filterdNames) {
        this.dataModelArrayList = filterdNames;
        notifyDataSetChanged();
    }
}
