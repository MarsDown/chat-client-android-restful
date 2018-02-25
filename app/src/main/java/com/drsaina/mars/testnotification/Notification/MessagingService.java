package com.drsaina.mars.testnotification.Notification;

/**
 * Created by mars on 9/4/2017.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ViewGroup;

import com.drsaina.mars.testnotification.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived ( RemoteMessage remoteMessage ) {
        Log.d ( "marslog", "messagId::" + "onMessageReceived" );

//        if ( remoteMessage.getNotification ( ).getBody ( ) != null ) {
//            if ( remoteMessage.getData ( ).get ( "type" ).equals ( "NewConsulation" ) ||
//                    remoteMessage.getData ( ).get ( "type" ).equals ( "ReminderConsulation" ) ) {
//                int messageId = Integer.parseInt ( remoteMessage.getData ( ).get ( "id" ).toString ( ) );
//                Log.d ( "mars", "messagId::" + messageId );

        int messageId = - 1, chatId = - 1;
        if ( remoteMessage.getData ( ) != null ) {
            messageId = Integer.parseInt ( remoteMessage.getData ( ).get ( "messageId" ) );
            chatId = Integer.parseInt ( remoteMessage.getData ( ).get ( "chatId" ) );
        }

        GoMain ( " پیام جدید", "پیام جدید", chatId );

        Intent broadcast = new Intent ( "New_Message" );
        broadcast.putExtra ( "messageId", messageId );
        sendBroadcast ( broadcast );

    }

    private void GoMain ( String messageBody, String title, int messageId ) {
        ShowNotification ( messageBody, title, null, messageId );
    }

    private void ShowNotification ( String messageBody, String title, PendingIntent endActivity, int chatId ) {

        NotificationManager notificationManager =
                ( NotificationManager ) getSystemService ( Context.NOTIFICATION_SERVICE );

        notificationManager.cancel ( chatId );

        Uri defaultSoundUri = RingtoneManager.getDefaultUri ( RingtoneManager.TYPE_NOTIFICATION );
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder ( this )
                .setSmallIcon ( R.mipmap.ic_launcher )
                .setContentTitle ( title )
                .setContentText ( messageBody )
                .setAutoCancel ( true )
                .setSound ( defaultSoundUri );

        notificationManager.notify ( chatId, notificationBuilder.build ( ) );


    }

}