package com.farmers.ownfarmer.ui.chat;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.farmers.ownfarmer.R;

import java.util.Date;
import java.util.List;

import static com.farmers.ownfarmer.ui.chat.ChatActivity.PREFERENCE;
import static com.farmers.ownfarmer.ui.chat.ChatActivity.PREF_USER_ID;


public class ChatRecyclerViewAdapter extends RecyclerView.Adapter implements MediaPlayer.OnPreparedListener {
    //////// Declaration of Variable,ImageView,TextView,SharedPreferences etc////////
    Activity activity;

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;

    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    List<ChatDataModel> dataModelArrayList;

    private MediaPlayer mPlayer = null;

   private LayoutInflater mLayoutInflater;

    String user_id,img;

    SharedPreferences user_SharedPreference;

    private static String tempFilterAudioName = "tempAudio.3gp";

    //////// Class Constructor ////////
    public ChatRecyclerViewAdapter(Activity activity, List<ChatDataModel> dataModelArrayList, String img) {
        this.activity = activity;
        this.dataModelArrayList = dataModelArrayList;
        user_SharedPreference = activity.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        user_id = user_SharedPreference.getString("user_id", "");
        mLayoutInflater = LayoutInflater.from(activity);
        this.img = img;

    }

    // Inflates the appropriate layout according to the ViewType.
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {

            case VIEW_TYPE_MESSAGE_SENT:
                return new SentMessageHolder(mLayoutInflater.inflate(R.layout.item_message_sent,
                        parent, false));
            case VIEW_TYPE_MESSAGE_RECEIVED:
                return new ReceivedMessageHolder(mLayoutInflater.inflate(R.layout
                        .item_message_received, parent, false));
            default:
                return null;
        }


    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


        switch (getItemViewType(position)) {

            case VIEW_TYPE_MESSAGE_SENT:
                SentMessageHolder senderMsgViewHolder = (SentMessageHolder) holder;

                MediaController mc = new MediaController(activity);

                ChatDataModel chat = dataModelArrayList.get(position);

                if (chat.getMessageType().equals("TEXT")) {
                    senderMsgViewHolder.tvMessage.setText(chat.getMessageText());

                }

                String time = longToDateString(chat.getMessageTime(), "HH:mm a");

                senderMsgViewHolder.tvTime.setText(time);

                break;

            case VIEW_TYPE_MESSAGE_RECEIVED:


                ReceivedMessageHolder receiverMsgViewHolder = (ReceivedMessageHolder) holder;

                MediaController mc2 = new MediaController(activity);

                ChatDataModel chat2 = dataModelArrayList.get(position);

                Glide.with(activity)
                        .load(activity.getResources().getString(R.string.image_url) + img)
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_baseline_person_24))
                        .apply(RequestOptions.skipMemoryCacheOf(false))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA))
                        .into((ImageView) ((ReceivedMessageHolder) holder).ivUserProfile);


                ///////// Checks for Message type Text/Audio/Video ///////
                if (chat2.getMessageType().equals("TEXT")) {
                    receiverMsgViewHolder.tvMessage.setText(chat2.getMessageText());
                }


                String time2 = longToDateString(chat2.getMessageTime(), "HH:mm a");

                break;
        }
    }


    @Override

    public int getItemCount() {
        return dataModelArrayList.size();
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();

    }


    public class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        ///////// Declaration for TextView,ImageView etc///////
        TextView tvMessage;
        ImageView ivUserProfile;



        ReceivedMessageHolder(View itemView) {
            super(itemView);

            ///////// Initializaiton for TextView,ImageView etc///////
            tvMessage = itemView.findViewById(R.id.text_message_body_received);
            ivUserProfile = itemView.findViewById(R.id.image_message_profile);

        }


    }

    public class SentMessageHolder extends RecyclerView.ViewHolder {
        ///////// Declaration for TextView,ImageView etc///////
        TextView tvMessage, tvTime;

RelativeLayout relativeLayout;
        SentMessageHolder(View itemView) {
            super(itemView);

            ///////// Initialization for TextView,ImageView etc///////
            tvMessage = itemView.findViewById(R.id.text_message_body_send);
            tvTime = itemView.findViewById(R.id.text_message_time_send);
        }

    }


    @Override
    public int getItemViewType(int position) {

        if (dataModelArrayList.get(position).getMessageFrom().equals(user_id)) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }

    }

    ///////// Conversion of Long value to a date ///////
    public static String longToDateString(long timestamp, String format) {
        return DateFormat.format(format, new Date(timestamp)).toString();
    }

}
