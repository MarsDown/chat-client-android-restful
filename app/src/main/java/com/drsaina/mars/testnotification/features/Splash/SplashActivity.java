package com.drsaina.mars.testnotification.features.Splash;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.drsaina.mars.testnotification.features.ChatRoom.ChatRoomActivity;
import com.drsaina.mars.testnotification.features.Login.LoginActivity;
import com.drsaina.mars.testnotification.R;

public class SplashActivity extends AppCompatActivity implements SplashContract.View {


    private SplashPresenter presenter;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_splash );

        presenter = new SplashPresenter ( this );

        new Handler ( ).postDelayed ( ( ) -> {
            /* Create an Intent that will ControllerRecorder the Menu-Activity. */
            presenter.init ( );
        }, 1 );

    }

    @Override
    public void showToast ( String txt ) {

    }

    @Override
    public void openChatRoomActivity ( ) {
        Intent intent = new Intent ( this, ChatRoomActivity.class );
        startActivity ( intent );
        finish ( );
    }

    @Override
    public void openLoginActivity ( ) {
        Intent intent = new Intent ( this, LoginActivity.class );
        startActivity ( intent );
        finish ( );
    }
}
