package com.drsaina.mars.testnotification.Data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.drsaina.mars.testnotification.Data.remot.Model.CoreUser;

/**
 * Created by mars on 2/14/2018.
 */

public class SharedPreferencesData {

    private static final String PREFERENCES_FILE = "testNotification";

    private final SharedPreferences mPrefs;
    private final String userLogin = "userLogin";


    public SharedPreferencesData ( Context context ) {
        mPrefs = context.getSharedPreferences ( "sinagramars", Context.MODE_PRIVATE );
    }

    public String readSharedSetting ( String settingName, String defaultValue ) {
        return mPrefs.getString ( settingName, defaultValue );
    }

    public void saveSharedSetting ( String key, String value ) {
        SharedPreferences.Editor editor = mPrefs.edit ( );
        editor.putString ( key, value );
        editor.apply ( );
        editor.commit ( );
    }

    public void changeStateUser ( boolean state ) {
        SharedPreferences.Editor edit = mPrefs.edit ( );
        edit.putBoolean ( userLogin, state );
        edit.apply ( );
        edit.commit ( );
    }

    public void updateUser ( CoreUser user ) {
        SharedPreferences.Editor edit = mPrefs.edit ( );
        edit.putString ( "name", user.getName ( ) );
        edit.putInt ( "userId", user.getUserId ( ) );
        edit.putString ( "phone", user.getPhone ( ) );
        edit.putString ( "userName", user.getUsername ( ) );
        edit.apply ();
        edit.commit ();
    }

    public CoreUser getUser ( ) {
        CoreUser user = new CoreUser ( );
        user.setName ( mPrefs.getString ( "name", "" ) );
        user.setPhone ( mPrefs.getString ( "phone", "" ) );
        user.setUsername ( mPrefs.getString ( "userName", "" ) );
        user.setUserId ( mPrefs.getInt ( "userId", - 1 ) );

        return user;
    }

    public boolean IsUserLogin ( ) {
        return mPrefs.getBoolean ( userLogin, false );
    }


}
