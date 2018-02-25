package com.drsaina.mars.testnotification.Data.remot.config;

import com.drsaina.mars.testnotification.Data.remot.service.ChatRoomService;
import com.drsaina.mars.testnotification.Data.remot.service.MessageService;
import com.drsaina.mars.testnotification.Data.remot.service.ProfileService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mars on 2/8/2018.
 */

public class RetrofitProvider {

    private ChatRoomService mTService;
    private ProfileService mProfileService;
    private MessageService messageService;

    private Retrofit mRetrofitClient;

    public RetrofitProvider()
    {

//        OkHttpClient httpClient = new OkHttpClient();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(ClientConfig.REST_API_BASE_URL) // set Base URL , should end with '/'
//                .client(httpClient) // add http client
//                .addConverterFactory(GsonConverterFactory.create())//add default converter we use from Gson
//                .build();
//        mTService = retrofit.create(FakeTwitterService.class);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor( chain -> {
            Request original = chain.request();
            String originalPath = original.url().url().getPath();
            if (originalPath.endsWith("/message") || originalPath.endsWith("refreshtoken")) {
                return chain.proceed(original);
            } else {

                //build request
                Request.Builder requestBuilder = original.newBuilder();
                //add header for all of the request
                requestBuilder.addHeader("Accept", "application/json");
                //check is user logged in , if yes should add authorization header to every request
//                    if (mAppPreferenceTools.isAuthorized()) {
//                        requestBuilder.addHeader("Authorization", "bearer " + mAppPreferenceTools.getAccessToken());
//                    }
                requestBuilder.method(original.method(), original.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        } );
        Gson gson = new GsonBuilder ()
                .registerTypeAdapter(Date.class, new UTCDateTypeAdapter())
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ClientConfig.REST_API_BASE_URL) // set Base URL , should end with '/'
                .client(httpClient.build ()) // add http client
                .addConverterFactory(GsonConverterFactory.create(gson))//add gson converter
                .build();

        mTService = retrofit.create(ChatRoomService.class);
        mProfileService = retrofit.create(ProfileService.class);
        messageService = retrofit.create ( MessageService.class );

    }

    public ChatRoomService getChatRoomService () {
        return mTService;
    }

    public Retrofit getRetrofitClient() {
        return mRetrofitClient;
    }

    public ProfileService getProfileService ( ) {
        return mProfileService;
    }

    public MessageService getMessageService ( ) {
        return messageService;
    }
}
