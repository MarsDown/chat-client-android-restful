package com.drsaina.mars.testnotification.features.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.drsaina.mars.testnotification.features.ChatRoom.ChatRoomActivity;
import com.drsaina.mars.testnotification.Data.remot.Model.CoreUser;
import com.drsaina.mars.testnotification.R;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by mars on 2/14/2018.
 */

public class LoginActivity extends AppCompatActivity implements LoginContract.View {


    LoginPresenter presenter;

    EditText ediPhone;

    Button btnLogin;

    ProgressBar Loader;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_login );

        presenter = new LoginPresenter ( this );
        presenter.setContext ( this );

        ediPhone = findViewById ( R.id.ediphone );

        btnLogin = findViewById ( R.id.btnLogin );

        Loader = findViewById ( R.id.Loader );

        btnLogin.setOnClickListener ( view -> {

            String Token = FirebaseInstanceId.getInstance ( ).getToken ( );

            CoreUser user = new CoreUser ();
            user.setPhone ( ediPhone.getText ( ).toString ( ) );
            user.setToken ( Token );
            user.setName ( "testUser" );

            presenter.registerUser ( user );
        } );


        hideLoader ( );
    }

    @Override
    public void showToast ( String txt ) {

    }

    @Override
    public void showLoader ( ) {
        Loader.setVisibility ( View.VISIBLE );
    }

    @Override
    public void hideLoader ( ) {
        Loader.setVisibility ( View.GONE );

    }

    @Override
    public void goChatRoom ( ) {
        Intent intent = new Intent ( this, ChatRoomActivity.class );
        startActivity ( intent );
        finish ( );
    }

}
