package com.drsaina.mars.testnotification.Data.remot.service;

import com.drsaina.mars.testnotification.Data.remot.Model.CoreChatRoom;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by mars on 2/8/2018.
 */

public interface ChatRoomService {

//    @Multipart
//    @POST("user/profile/image")
//    Call <ResponseBody> uploadUserProfileImage ( @PartMap Map <String, RequestBody> map );
//
//    @GET("Modules/mConsultation/Mobile/GetDoctorLoginStatus.aspx")
//    Call <StateDoctor> getDoctorState ( @Query("dguid") String doctorGuid );
//
//    @POST("getMessages")
//    @FormUrlEncoded
//    Call <List<CoreMessage>> getMessages ( @Field( "chatId" ) Integer UserId);

    @POST("getChatRoomInfo")
    @FormUrlEncoded
    Call <CoreChatRoom> getChatRoomInfo ( @Field( "chatId" ) Integer chatId);

//
//    @POST("saveMessage")
//    Call <CoreMessage> saveMessage( @Body CoreMessage message);
//
//    @Multipart
//    @POST("message")
//    Call <String> saveMessage(@Part("filename") MultipartBody.Part file);

//    @POST("savaMessage")
//    Call <CoreUser> saveMessage( @Body CoreUser message);

//    @POST("/")
//    Call <ResponseBody> postImage ( @Part("image") MultipartBody.Part image,
//                                    @Part("name") RequestBody name,
//                                    @Part("type") RequestBody typeMessage, @Part("qtext") RequestBody context,
//                                    @Part("uguid") RequestBody userGuid, @Part("qguid") RequestBody questionGuid );


}
