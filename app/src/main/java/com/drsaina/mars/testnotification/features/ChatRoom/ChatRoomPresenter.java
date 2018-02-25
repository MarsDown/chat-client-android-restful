package com.drsaina.mars.testnotification.features.ChatRoom;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.drsaina.mars.testnotification.Data.local.db.AppDbHelper;
import com.drsaina.mars.testnotification.Data.remot.MessageListener;
import com.drsaina.mars.testnotification.Data.remot.MessageRepository;
import com.drsaina.mars.testnotification.Data.remot.Model.AudioInfo;
import com.drsaina.mars.testnotification.Data.remot.service.ChatRoomService;
import com.drsaina.mars.testnotification.Data.remot.service.MessageService;
import com.drsaina.mars.testnotification.Data.remot.config.RetrofitProvider;
import com.drsaina.mars.testnotification.General;
import com.drsaina.mars.testnotification.Data.remot.Model.CoreChatRoom;
import com.drsaina.mars.testnotification.Data.remot.Model.CoreUser;
import com.drsaina.mars.testnotification.Data.remot.Model.MessagesView;
import com.drsaina.mars.testnotification.Data.remot.Model.CoreMessage;
import com.drsaina.mars.testnotification.Utill.AudioRecording;
import com.drsaina.mars.testnotification.Utill.ConvertType;
import com.drsaina.mars.testnotification.Utill.FileUtil;
import com.github.lassana.recorder.AudioRecorder;
import com.koushikdutta.ion.Ion;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by mars on 2/12/2018.
 */

public class ChatRoomPresenter implements ChatRoomContract.Presenter {

    private String errorServer = "خطا در اتصال به سرور";
    private String TAG = "marsi";

    private ChatRoomContract.View viewLayer;
    private ChatRoomService chatRoomService;


    private MessageService messageService;
    private String contextMessage;

    private int chatId, userId;

    private MessageRepository repository;
    private RxPermissions mPermissions;
    AudioRecorder recorder;

    MediaPlayer mediaPlayer;
    private Handler handler;
    private Runnable runnable;

    public ChatRoomPresenter ( ChatRoomContract.View viewLayer, RxPermissions mPermissions, AudioRecorder recorder ) {
        this.viewLayer = viewLayer;
        this.mPermissions = mPermissions;

        RetrofitProvider provider = new RetrofitProvider ( );
        this.chatRoomService = provider.getChatRoomService ( );
        this.messageService = provider.getMessageService ( );
        this.recorder = recorder;
        repository = new MessageRepository ( messageService );

    }

    public void init ( int chatId, int userId ) {
        this.chatId = chatId;
        this.userId = userId;
    }

    @Override
    public void subscribe ( ) {

    }

    @Override
    public void unsubscribe ( ) {

    }

    @Override
    public void onViewAttached ( ChatRoomContract.View view ) {

    }

    @Override
    public void onLoadChatRoomMessage ( Integer skip, Integer take ) {

        Call <List <CoreMessage>> call = messageService.getMessages ( chatId, take, skip );
        call.enqueue ( new Callback <List <CoreMessage>> ( ) {
            @Override
            public void onResponse ( Call <List <CoreMessage>> call, Response <List <CoreMessage>> response ) {
                if ( response != null ) {

                    List <CoreMessage> body = response.body ( );
                    List <MessagesView> list = new ArrayList <> ( );
                    for ( CoreMessage message : body ) {
                        MessagesView msgView = new MessagesView ( );

                        if ( message.getUser ( ).getUserId ( ) == General.sharedPreferencesData.getUser ( ).getUserId ( ) ) {
                            msgView.setForMe ( true );
                            message.setMessageState ( "read" );

                        } else {
                            msgView.setForMe ( false );
                        }

                        if ( message.getMessageType ( ).equals ( "audio" ) ) {
                            AudioInfo audioInfo = new AudioInfo ( );
                            audioInfo.setStatePlay ( "download" );
                            msgView.setAudioInfo ( audioInfo );
                        }

                        msgView.setStateDownload ( "noDownload" );
                        msgView.setMessages ( message );
                        list.add ( msgView );
                    }
                    viewLayer.showMessages ( list );

                } else {
                    viewLayer.showToast ( errorServer );
                }
            }

            @Override
            public void onFailure ( Call <List <CoreMessage>> call, Throwable t ) {
                viewLayer.showToast ( "errorServer" );
            }
        } );
    }

    @Override
    public void onLoadLastMessage ( Integer messageId ) {

        Call <CoreMessage> call = messageService.loadLastMessage ( messageId );
        call.enqueue ( new Callback <CoreMessage> ( ) {
            @Override
            public void onResponse ( Call <CoreMessage> call, Response <CoreMessage> response ) {
                if ( response != null && response.isSuccess ( ) ) {
                    {
                        CoreMessage body = response.body ( );
                        MessagesView messagesView = new MessagesView ( );
                        messagesView.setMessages ( body );
                        messagesView.setForMe ( false );

                        if ( body.getMessageType ( ).equals ( "audio" ) ) {
                            AudioInfo audioInfo = new AudioInfo ( );
                            audioInfo.setStatePlay ( "download" );
                            messagesView.setAudioInfo ( audioInfo );
                        }

                        messagesView.setStateDownload ( "noDownload" );

                        viewLayer.addNewMessage ( messagesView );
                        viewLayer.hideNotificaton ( );
                    }
                }
            }

            @Override
            public void onFailure ( Call <CoreMessage> call, Throwable t ) {
                int a = 5;
            }
        } );
    }

    public void setContextMessage ( String contextMessage ) {
        this.contextMessage = contextMessage;
    }

    @Override
    public void saveMessage ( String type ) {

        MessagesView messagesView = new MessagesView ( );

        CoreChatRoom chatRoom = new CoreChatRoom ( );
        chatRoom.setChatId ( chatId );

        CoreUser user = new CoreUser ( );
        user.setUserId ( userId );

        CoreMessage message = new CoreMessage ( );
        message.setUser ( user );
        message.setChatRoom ( chatRoom );
        message.setMessageType ( type );
        message.setMessageState ( "" );

        if ( type.equals ("text") ) {
            message.setMessageContext ( contextMessage );
        } else if ( type.equals ("audio") ) {
            message.setMessagePath ( contextMessage );
            AudioInfo audioInfo = new AudioInfo ();
            audioInfo.setStatePlay ("play");
            messagesView.setAudioInfo ( audioInfo );
        } else {
            message.setMessagePath ( contextMessage );
        }

        long l = AppDbHelper.insertMessage ( ConvertType.toMessaage ( message ) );
        message.setMessageId ( Integer.parseInt ( l + "" ) );
        messagesView.setMessages ( message );
        messagesView.setForMe ( true );
        viewLayer.addNewMessage ( messagesView );
    }

    @Override
    public void getChatRoomInfo ( ) {
        Call <CoreChatRoom> call = chatRoomService.getChatRoomInfo ( chatId );
        call.enqueue ( new Callback <CoreChatRoom> ( ) {
            @Override
            public void onResponse ( Call <CoreChatRoom> call, Response <CoreChatRoom> response ) {
                if ( response != null && response.isSuccess ( ) ) {
                    viewLayer.showChatRoomInformation ( response.body ( ) );
                } else viewLayer.showToast ( errorServer );
            }

            @Override
            public void onFailure ( Call <CoreChatRoom> call, Throwable t ) {
                viewLayer.showToast ( errorServer );
            }
        } );
    }

    @Override
    public void onChangeEditText ( String text ) {

        if ( text.length ( ) != 0 )
            viewLayer.showTextToolbar ( );
        else
            viewLayer.hideTextToolbar ( );

        contextMessage = text;
    }

    @Override
    public void onClickSendMessage ( String tag ) {

        switch (tag) {
            case "start_record":
                startRecord ( );
                break;
            case "stop_record":
                stopRecord ( );
                break;
            case "send_audio":
                sendSound ( );
                break;
            case "send_text":
                sendText ( );
                break;
        }

    }

    private void sendSound ( ) {
        saveMessage ( "audio" );
    }

    private void sendText ( ) {
        saveMessage ( "text" );
    }


    private void startRecord ( ) {

        recorder.start ( new AudioRecorder.OnStartListener ( ) {
            @Override
            public void onStarted ( ) {
                viewLayer.startRecord ( );

            }

            @Override
            public void onException ( Exception e ) {

            }
        } );


    }

    private void stopRecord ( ) {


        if ( recorder.isRecording ( ) ) {
            recorder.pause ( new AudioRecorder.OnPauseListener ( ) {
                @Override
                public void onPaused ( String activeRecordFileName ) {
                    Log.d ( TAG, "onPaused: " + activeRecordFileName );
                    contextMessage = activeRecordFileName;
                    viewLayer.stopRecord ( );
                }

                @Override
                public void onException ( Exception e ) {
                    int a = 0;
                }
            } );
        }

    }


    @Override
    public void playMusic ( String url, int position ) {

        stopMusic ( );

        if ( mediaPlayer == null )
            mediaPlayer = new MediaPlayer ( );

        mediaPlayer.setAudioStreamType ( AudioManager.STREAM_MUSIC );

        if ( url != null && ! url.equals ( "" ) ) {
            try {
                File file = new File ( url );
                if ( file.exists ( ) ) {
                    mediaPlayer.setDataSource ( file.getAbsolutePath ( ) );
                    viewLayer.playMusic ( );
                } else {
                    stopMusic ( );
                }
                mediaPlayer.prepareAsync ( );

            } catch ( IllegalStateException e ) {
            } catch ( IOException e ) {
            }

            mediaPlayer.setOnCompletionListener ( mp -> {
                if ( mp.getCurrentPosition ( ) > 0 ) {
                    mediaPlayer.reset ( );
                    if ( handler != null && runnable != null )
                        handler.removeCallbacks ( runnable );

                    viewLayer.changeStateIconPlay ( - 1, "play" );
                }
                stopMusic ( );
            } );

            mediaPlayer.setOnPreparedListener ( mp -> {
                mediaPlayer.start ( );
                viewLayer.updateInfoSounIsPlay ( position, 0, String.valueOf ( mp.getDuration ( ) ) );
                viewLayer.changeStateIconPlay ( position, "stop" );
                handler = new Handler ( );

                runnable = new Runnable ( ) {
                    @Override
                    public void run ( ) {
                        int totaltime = 0, mCurrentPosition = - 1;
                        if ( mp != null ) {

                            totaltime = mp.getDuration ( );
                            Log.d ( TAG, "playMusic: totaltime:" + totaltime );

                            if ( totaltime > 0 ) {
                                mCurrentPosition = mp.getCurrentPosition ( );
                                int pro = ( mCurrentPosition * 100 ) / totaltime;
                                Log.d ( TAG, "playMusic: update:" + pro );
                                viewLayer.updateInfoSounIsPlay ( position, pro, milliSecondsToTimer ( mCurrentPosition ) + "/" + milliSecondsToTimer ( totaltime ) );
                            }

                        }

                        if ( totaltime > 0 && mCurrentPosition <= totaltime )
                            handler.postDelayed ( this, 1 );
                    }
                };

                handler.postDelayed ( runnable, 1 );
            } );
        } else {
            stopMusic ( );
        }
    }

    public String milliSecondsToTimer ( int milliseconds ) {
        String finalTimerString = "";
        String secondsString = "";


        // Convert total duration into time
        int hours = ( int ) ( milliseconds / ( 1000 * 60 * 60 ) );
        int minutes = ( int ) ( milliseconds % ( 1000 * 60 * 60 ) ) / ( 1000 * 60 );
        int seconds = ( int ) ( ( milliseconds % ( 1000 * 60 * 60 ) ) % ( 1000 * 60 ) / 1000 );
        // Add hours if there
        if ( hours > 0 ) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if ( seconds < 10 ) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }


    @Override
    public void stopMusic ( ) {

        if ( mediaPlayer != null && mediaPlayer.isPlaying ( ) ) {
            mediaPlayer.stop ( );
            mediaPlayer.release ();
            mediaPlayer = null;

            if ( handler != null && runnable != null )
                handler.removeCallbacks ( runnable );

            viewLayer.changeStateIconPlay ( - 1, "play" );
        }


    }

}
