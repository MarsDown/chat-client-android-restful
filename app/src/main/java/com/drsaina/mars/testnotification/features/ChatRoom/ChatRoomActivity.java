package com.drsaina.mars.testnotification.features.ChatRoom;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.drsaina.mars.testnotification.General;
import com.drsaina.mars.testnotification.Interface.DownloadListener;
import com.drsaina.mars.testnotification.Interface.OnUpdateDuration;
import com.drsaina.mars.testnotification.Data.remot.Model.CoreChatRoom;
import com.drsaina.mars.testnotification.Data.remot.Model.CoreMessage;
import com.drsaina.mars.testnotification.Data.remot.Model.CoreUser;
import com.drsaina.mars.testnotification.Data.remot.Model.MessagesView;
import com.drsaina.mars.testnotification.R;
import com.drsaina.mars.testnotification.Utill.AudioRecording;
import com.drsaina.mars.testnotification.Utill.CircleImageView;
import com.drsaina.mars.testnotification.Utill.FileUtil;
import com.drsaina.mars.testnotification.Utill.LazyLoaderRecycler;
import com.github.lassana.recorder.AudioRecorder;
import com.github.lassana.recorder.AudioRecorderBuilder;
import com.koushikdutta.ion.Ion;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.drsaina.mars.testnotification.General.DIR_APP;

public class ChatRoomActivity extends AppCompatActivity implements ChatRoomContract.View, ChatRoomAdapterListener {

    String TAG = "marsi";

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_TAKE_PHOTO = 2;

    private File actualImage;
    private File compressedImage;

    ChatRoomAdapter chatRoomAdapter;
    RecyclerView ryChatRoom;

    ChatRoomPresenter mPresenter;

    CircleImageView avatar;
    TextView txtChatRoomName;

    AppCompatImageButton btnBack, btnSendMessage;
    ImageButton btnSendFile;

    LinearLayout lyTextToolbar, lySoundRecordToolbar;

    EditText ediMessageText;

    @Override
    protected void attachBaseContext ( Context newBase ) {
        try {
            super.attachBaseContext ( CalligraphyContextWrapper.wrap ( newBase ) );
        } catch ( Exception ex ) {
            super.attachBaseContext ( newBase );
        }
    }

    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data ) {
        super.onActivityResult ( requestCode, resultCode, data );

        if ( requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK ) {
            Uri imageUri = Uri.parse ( mCurrentPhotoPath );
            File file = new File ( imageUri.getPath ( ) );
            actualImage = file;
        }

        if ( requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK ) {
            if ( data == null ) {
                return;
            }
            try {
                actualImage = FileUtil.from ( this, data.getData ( ) );
            } catch ( IOException e ) {
            }
        }
        mPresenter.setContextMessage ( actualImage.getAbsolutePath ( ) );
        mPresenter.saveMessage ( "image" );
    }

    BroadcastReceiver broadcastReceiver;

    public void initBroadCastReciver ( ) {

        broadcastReceiver = new BroadcastReceiver ( ) {
            @Override
            public void onReceive ( Context context, Intent intent ) {
                String action = intent.getAction ( );
                if ( action.equalsIgnoreCase ( "New_Message" ) ) {
                    int messageId = intent.getIntExtra ( "messageId", - 1 );
                    mPresenter.onLoadLastMessage ( messageId );
                } else if ( action.equalsIgnoreCase ( "Update_Message" ) ) {
                    int messageId = intent.getIntExtra ( "messageId", - 1 );
                    if ( chatRoomAdapter != null )
                        chatRoomAdapter.updateStateDownload ( messageId, "send" );
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter ( );
        intentFilter.addAction ( "New_Message" );
        intentFilter.addAction ( "Update_Message" );
        registerReceiver ( broadcastReceiver, intentFilter );
    }

    String mCurrentPhotoPath;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_chat_room );

        ryChatRoom = findViewById ( R.id.listchatroom );
        avatar = findViewById ( R.id.avatar );
        txtChatRoomName = findViewById ( R.id.txtChatRoomName );

        //Imagebutton
        btnBack = findViewById ( R.id.btnBack );
        btnSendMessage = findViewById ( R.id.btnSendMessage );
        btnSendMessage.setTag ( "start_record" );
        btnSendFile = findViewById ( R.id.btnSendFile );

        btnSendMessage.setOnClickListener ( view -> mPresenter.onClickSendMessage ( btnSendMessage.getTag ( ).toString ( ) ) );

        btnSendFile.setOnClickListener ( view -> {
            Intent intent1 = new Intent ( );
            intent1.setType ( "*/*" );
            intent1.setAction ( Intent.ACTION_GET_CONTENT );
            startActivityForResult ( Intent.createChooser ( intent1, "انتخاب فایل" ), PICK_IMAGE_REQUEST );
        } );


        boolean isPermissionsGranted = getRxPermissions ( ).isGranted ( RECORD_AUDIO );

        if ( ! isPermissionsGranted ) {
            getRxPermissions ( ).request ( RECORD_AUDIO )
                                .subscribe ( granted -> {
                                    if ( granted ) {
                                    } else {
                                        return;
                                    }
                                }, throwable -> throwable.printStackTrace ( ) );
        }

        lySoundRecordToolbar = findViewById ( R.id.lySoundRecord );
        lyTextToolbar = findViewById ( R.id.lyTextToolbar );

        ediMessageText = findViewById ( R.id.ediMessageText );


        AudioRecorder recorder = AudioRecorderBuilder.with ( this )
                                                     .fileName ( DIR_APP + "/bahram.aac" )
                                                     .config ( AudioRecorder.MediaRecorderConfig.DEFAULT )
                                                     .loggable ( )
                                                     .build ( );
//

        RxPermissions permissions = new RxPermissions ( this );

        mPresenter = new ChatRoomPresenter ( this, permissions, recorder );
        mPresenter.init ( 5, General.sharedPreferencesData.getUser ( ).getUserId ( ) );

        int WidthDp = ( getApplicationContext ( ).getResources ( ).getDisplayMetrics ( ).widthPixels / 2 );
        WidthDp += ( WidthDp / 3 ) * 2;

        List <MessagesView> li = new ArrayList <> ( );
        chatRoomAdapter = new ChatRoomAdapter ( this, li, this, WidthDp );

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager ( this, LinearLayoutManager.VERTICAL, true );
        ryChatRoom.setAdapter ( chatRoomAdapter );
        ryChatRoom.setLayoutManager ( linearLayoutManager );
        mPresenter.onLoadChatRoomMessage ( 0, 10 );
        mPresenter.getChatRoomInfo ( );

        ryChatRoom.addOnScrollListener ( new LazyLoaderRecycler ( linearLayoutManager ) {
            @Override
            public void loadMore ( int firstVisibleItem, int visibleItemCount, int totalItemCount ) {
                int skip = totalItemCount / 10;
                mPresenter.onLoadChatRoomMessage ( skip + 1, 10 );
            }
        } );

        initBroadCastReciver ( );

        btnBack.setOnClickListener ( view -> {
            finish ( );
        } );

        ediMessageText.addTextChangedListener ( new TextWatcher ( ) {
            @Override
            public void beforeTextChanged ( CharSequence s, int start, int count, int after ) {
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onTextChanged ( CharSequence s, int start, int before, int count ) {
                mPresenter.onChangeEditText ( ediMessageText.getText ( ).toString ( ) );
            }

            @Override
            public void afterTextChanged ( Editable s ) {

            }
        } );

        ediMessageText.setOnKeyListener ( ( v, keyCode, event ) -> false );

    }

    private RxPermissions mPermissions;

    private RxPermissions getRxPermissions ( ) {
        if ( mPermissions == null ) {
            mPermissions = new RxPermissions ( this );
        }
        return mPermissions;
    }

    @Override
    public void showToast ( String txt ) {
        Toast.makeText ( this, txt, Toast.LENGTH_LONG ).show ( );
    }

    @Override
    public void showMessages ( List <MessagesView> messages ) {
        chatRoomAdapter.addMessages ( messages );
        chatRoomAdapter.notifyDataSetChanged ( );
    }

    @Override
    public void addNewMessage ( MessagesView messagesView ) {
        chatRoomAdapter.AddItem ( messagesView );
        ryChatRoom.scrollToPosition ( 0 );
        resetToolbarMessage ( );
    }

    @Override
    public void showChatRoomInformation ( CoreChatRoom chatRoom ) {
        txtChatRoomName.setText ( chatRoom.getChatName ( ) );
        if ( chatRoom.getChatImageProfile ( ) != null )
            Glide.with ( this ).load ( chatRoom.getChatImageProfile ( ) ).into ( avatar );
    }

    @Override
    public void showTextToolbar ( ) {
//        lyTextToolbar.setVisibility ( View.VISIBLE );

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            btnSendMessage.setImageDrawable ( getDrawable ( R.drawable.ic_reply ) );
        } else if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ) {
            btnSendMessage.setImageDrawable ( ContextCompat.getDrawable ( this, R.drawable.ic_reply ) );
        }

        btnSendFile.setVisibility ( View.GONE );
        btnSendMessage.setTag ( "send_text" );

//        lySoundRecordToolbar.setVisibility ( View.GONE );
    }

    @Override
    public void hideTextToolbar ( ) {

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            btnSendMessage.setImageDrawable ( getDrawable ( R.drawable.ic_record_microphone ) );
        } else if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ) {
            btnSendMessage.setImageDrawable ( ContextCompat.getDrawable ( this, R.drawable.ic_record_microphone ) );
        }

        btnSendFile.setVisibility ( View.VISIBLE );
        btnSendMessage.setTag ( "start_record" );
    }


    @Override
    public void hideNotificaton ( ) {

        NotificationManager notificationManager = ( NotificationManager ) getSystemService ( Context.NOTIFICATION_SERVICE );
        notificationManager.cancel ( 5 );

    }

    @Override
    public void resetToolbarMessage ( ) {
        btnSendFile.setVisibility ( View.VISIBLE );
        ediMessageText.setText ( "" );
        btnSendMessage.setTag ( "start_record" );

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            btnSendMessage.setImageDrawable ( getDrawable ( R.drawable.ic_record_microphone ) );
        } else if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ) {
            btnSendMessage.setImageDrawable ( ContextCompat.getDrawable ( this, R.drawable.ic_record_microphone ) );
        }
    }

    @Override
    public void startRecord ( ) {
        ediMessageText.setText ( "" );
        ediMessageText.setVisibility ( View.GONE );
        btnSendMessage.setTag ( "stop_record" );
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            btnSendMessage.setImageDrawable ( getDrawable ( R.drawable.ic_record_microphone_active ) );
        } else if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ) {
            btnSendMessage.setImageDrawable ( ContextCompat.getDrawable ( this, R.drawable.ic_record_microphone_active ) );
        }
    }

    @Override
    public void stopRecord ( ) {
        ediMessageText.setVisibility ( View.GONE );
        btnSendMessage.setTag ( "send_audio" );
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            btnSendMessage.setImageDrawable ( getDrawable ( R.drawable.ic_reply ) );
        } else if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ) {
            btnSendMessage.setImageDrawable ( ContextCompat.getDrawable ( this, R.drawable.ic_reply ) );
        }
    }

    @Override
    public void playMusic ( ) {

    }

    @Override
    public void stopMusic ( ) {

    }

    @Override
    public void updateInfoSounIsPlay ( int position, int seekValue, String totalTime ) {
        chatRoomAdapter.updateSeekBar ( position, seekValue, totalTime );
    }

    @Override
    public void changeStateIconPlay ( int position, String state ) {
        chatRoomAdapter.updateStatePlay ( position, state );
    }

    @Override
    public void DownloadFile ( int position, MessagesView message, DownloadListener downloadListener ) {
        Log.d ( "mars", "download:" + message.getMessages ( ).getMessagePath ( ) );

        Ion.with ( this )
           .load ( message.getMessages ( ).getMessagePath ( ) )
           .progressHandler ( ( downloaded, total ) -> {
               downloadListener.totalDownloading ( downloaded / 1000 + " / " + total / 1000 + " mb" );
               changeStateIconPlay ( position, "load" );
           })
           .write ( new File ( DIR_APP + "/" + message.getMessages ( ).getMessageId ( ) + ".aac" ) )
           .setCallback ( ( e, result ) -> {

               if ( e != null ) {
                   Log.d ( "mars", "error download:" + e.toString ( ) );
                   Toast.makeText ( this, "خطا در دانلود فایل", Toast.LENGTH_LONG ).show ( );
                   changeStateIconPlay ( position, "download" );
                   return;
               }

               Toast.makeText ( this, "فایل دانلود شد", Toast.LENGTH_LONG ).show ( );
               changeStateIconPlay ( position, "play" );
               chatRoomAdapter.updateStateDownload ( message.getMessages ( ).getMessageId ( ), "Download" );
           });

    }

    @Override
    public void zoomImage ( MessagesView message ) {

    }

    @Override
    public void playSound ( String url, int position ) {
        mPresenter.playMusic ( url, position );

    }


    @Override
    public void pauseSound ( ) {
        mPresenter.stopMusic ( );
    }


}
