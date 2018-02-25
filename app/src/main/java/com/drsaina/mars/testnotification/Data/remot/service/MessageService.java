package com.drsaina.mars.testnotification.Data.remot.service;

import com.drsaina.mars.testnotification.Data.remot.Model.CoreMessage;
import com.drsaina.mars.testnotification.Data.remot.Model.StateDoctor;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * Created by mars on 2/8/2018.
 */

public interface MessageService {

    @Multipart
    @POST("user/profile/image")
    Call <ResponseBody> uploadUserProfileImage ( @PartMap Map <String, RequestBody> map );

    @GET("Modules/mConsultation/Mobile/GetDoctorLoginStatus.aspx")
    Call <StateDoctor> getDoctorState ( @Query("dguid") String doctorGuid );

    @POST("getMessages")
    @FormUrlEncoded
    Call <List<CoreMessage>> getMessages ( @Field("chatId") Integer chatId,@Field ( "take" ) Integer take,@Field ( "skip") Integer skip );

    @POST("loadLastMessage")
    @FormUrlEncoded
    Call <CoreMessage> loadLastMessage ( @Field("messageId") Integer messageId );

    @POST("saveMessage")
    Call <CoreMessage> saveMessage ( @Body CoreMessage message );

    @Multipart
    @POST("message")
    Call <String> saveMessage ( @Part("filename") MultipartBody.Part file );

//    @POST("savaMessage")
//    Call <CoreUser> saveMessage( @Body CoreUser message);

//    @POST("/")
//    Call <ResponseBody> postImage ( @Part("image") MultipartBody.Part image,
//                                    @Part("name") RequestBody name,
//                                    @Part("type") RequestBody typeMessage, @Part("qtext") RequestBody context,
//                                    @Part("uguid") RequestBody userGuid, @Part("qguid") RequestBody questionGuid );


}
