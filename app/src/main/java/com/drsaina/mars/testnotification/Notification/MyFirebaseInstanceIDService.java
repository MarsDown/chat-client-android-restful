package com.drsaina.mars.testnotification.Notification;

/**
 * Created by mars on 9/4/2017.
 */

import android.util.Log;

import com.drsaina.mars.testnotification.Data.remot.config.RetrofitProvider;
import com.drsaina.mars.testnotification.Data.remot.service.ProfileService;
import com.drsaina.mars.testnotification.General;
import com.drsaina.mars.testnotification.Data.remot.Model.CoreUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

//    private static final String TAG = "mars";
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic("All");
        Log.d ( "marslog","refreshedToken:"+refreshedToken );
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {

        RetrofitProvider provider = new RetrofitProvider ( );
        ProfileService profileService = provider.getProfileService ( );
        Call <CoreUser> call = profileService.updateProfile ( General.sharedPreferencesData.getUser ( ).getUserId ( ), token );
        call.enqueue ( new Callback <CoreUser> ( ) {
            @Override
            public void onResponse ( Call <CoreUser> call, Response<CoreUser> response ) {
            }

            @Override
            public void onFailure ( Call <CoreUser> call, Throwable t ) {

            }
        } );


    }

}