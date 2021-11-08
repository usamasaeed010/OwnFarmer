package com.farmers.ownfarmer.firebase;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

//class extending FirebaseMessagingService
@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    SharedPreferences mSharedPreferences;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        if (remoteMessage.getData().size() > 0) {
            //handle the data message here
        }

        //getting the title and the body
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        Log.e("TGED", "TITLE: " + title);
        Log.e("TGED", "body: " + body);
        //then here we can use the title and body to build a notification
        MyNotificationManager.getInstance(getApplicationContext()).display(title, body);


    }

    public class MessageEvent {
        String Message;

        public MessageEvent(String message) {
            Message = message;
        }

        public String getMessage() {
            return Message;
        }

        public void setMessage(String message) {
            Message = message;
        }
    }
}
