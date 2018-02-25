package com.drsaina.mars.testnotification.Data.remot.service;

import com.drsaina.mars.testnotification.Data.remot.Model.CoreUser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by mars on 2/9/2018.
 */

public interface ProfileService {

    @POST("saveUser")
    Call <CoreUser> registerUser ( @Body CoreUser user);

    @POST("getUser")
    @FormUrlEncoded
    Call <CoreUser> getProfile ( @Field ( "userId" ) Integer UserId);

    @POST("updateUser")
    @FormUrlEncoded
    Call <CoreUser> updateProfile ( @Field ( "userId" ) Integer UserId,@Field ( "Token" ) String token);

}
