package com.drsaina.mars.testnotification.features.Login;

import android.content.Context;

import com.drsaina.mars.testnotification.Data.remot.config.RetrofitProvider;
import com.drsaina.mars.testnotification.Data.remot.service.ProfileService;
import com.drsaina.mars.testnotification.General;
import com.drsaina.mars.testnotification.Data.remot.Model.CoreUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mars on 2/12/2018.
 */

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View viewLayer;
    private ProfileService pServie;

    private String errorServer = "خطا در اتصال به سرور";
    private String contextMessage;

    private Context context;


    public LoginPresenter ( LoginContract.View viewLayer ) {
        this.viewLayer = viewLayer;
        RetrofitProvider provider = new RetrofitProvider ( );
        this.pServie = provider.getProfileService ( );
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
    public void onViewAttached ( LoginContract.View view ) {

    }

    @Override
    public void registerUser ( CoreUser user ) {

        viewLayer.showLoader ( );
        Call <CoreUser> call = pServie.registerUser ( user );
        call.enqueue ( new Callback <CoreUser> ( ) {
            @Override
            public void onResponse ( Call <CoreUser> call, Response <CoreUser> response ) {
                if ( response.isSuccess ( ) ) {
                    General.sharedPreferencesData.changeStateUser ( true );
                    General.sharedPreferencesData.updateUser ( response.body ( ) );
                    viewLayer.hideLoader ( );
                    viewLayer.goChatRoom ( );
                }
            }

            @Override
            public void onFailure ( Call <CoreUser> call, Throwable t ) {
                viewLayer.hideLoader ( );
            }
        } );


    }
}
