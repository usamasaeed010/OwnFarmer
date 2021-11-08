package com.farmers.ownfarmer.firebase;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;


//the class extending FirebaseInstanceIdService
public class MyFirebaseInstanceIdService extends FirebaseMessagingService {

    //this method will be called
    //when the token is generated
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);


        //now we will have the token
      //  String token=   FirebaseInstanceId.getInstance().getInstanceId().getResult().getToken();
        String token = FirebaseInstanceId.getInstance().getToken();

        //for now we are displaying the token in the log
        //copy it as this method is called only when the new token is generated
        //and usually new token is only generated when the app is reinstalled or the data is cleared
        android.util.Log.d("MyRefreshedToken", token);

        android.util.Log.d("NEW_TOKEN",s);
    }


}
