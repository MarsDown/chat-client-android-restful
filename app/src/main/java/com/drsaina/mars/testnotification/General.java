package com.drsaina.mars.testnotification;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import com.drsaina.mars.testnotification.Data.local.SharedPreferencesData;
import com.drsaina.mars.testnotification.Data.local.db.AppDbHelper;
import com.drsaina.mars.testnotification.Data.local.db.DbContext;
import com.drsaina.mars.testnotification.Service.SyncMessageChatRoomService;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by mars on 2/12/2018.
 */

public class General extends Application {

    public enum ChatContenType {Sound, Images, Text, PaymentView, ScoreView, DateMessage}

    public static SharedPreferencesData sharedPreferencesData;

    public static Context context;

    public static final String SDCARD = Environment.getExternalStorageDirectory ( ).getAbsolutePath ( );

    public static final String DIR_APP = SDCARD + "/Saina";

    @Override
    public void onCreate ( ) {
        super.onCreate ( );

        FirebaseMessaging.getInstance ( ).subscribeToTopic ( "All" );

        sharedPreferencesData = new SharedPreferencesData ( this );

        context = getApplicationContext ( );

        CalligraphyConfig.initDefault ( new CalligraphyConfig.Builder ( )
                .setDefaultFontPath ( "fonts/IRANSansMobile.ttf" )
                .setFontAttrId ( R.attr.fontPath )
                .build ( ) );
        DbContext dbctx = new DbContext ( this );

//        DbContext.newSession ().getMessagesDao ().deleteAll ();
        if ( ! isMyServiceRunning ( SyncMessageChatRoomService.class ) ) {
            Intent intent = new Intent ( getBaseContext ( ), SyncMessageChatRoomService.class );
            startService ( intent );
        }

    }

    private boolean isMyServiceRunning ( Class <?> serviceClass ) {
        ActivityManager manager = ( ActivityManager ) getSystemService ( Context.ACTIVITY_SERVICE );
        for ( ActivityManager.RunningServiceInfo service : manager.getRunningServices ( Integer.MAX_VALUE ) ) {
            if ( serviceClass.getName ( ).equals ( service.service.getClassName ( ) ) ) {
                return true;
            }
        }
        return false;
    }
}
