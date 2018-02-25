package com.drsaina.mars.testnotification.features.Splash;

import android.content.Context;

import com.drsaina.mars.testnotification.Data.remot.service.ProfileService;
import com.drsaina.mars.testnotification.General;

/**
 * Created by mars on 2/12/2018.
 */

public class SplashPresenter implements SplashContract.Presenter {

    private SplashContract.View viewLayer;
    private ProfileService pServie;

    private String errorServer = "خطا در اتصال به سرور";
    private String contextMessage;

    private Context context;


    public SplashPresenter ( SplashContract.View viewLayer ) {
        this.viewLayer = viewLayer;
    }

    public void setContext ( Context context ) {
        this.context = context;
    }


    @Override
    public void subscribe ( ) {

    }

    @Override
    public void unsubscribe ( ) {

    }

    @Override
    public void onViewAttached ( SplashContract.View view ) {

    }

    @Override
    public void init ( ) {
        boolean isUserLogin = General.sharedPreferencesData.IsUserLogin ( );
        if ( isUserLogin )
            viewLayer.openChatRoomActivity ( );
        else
            viewLayer.openLoginActivity ( );
    }
}
