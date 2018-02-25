package com.drsaina.mars.testnotification.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.drsaina.mars.testnotification.Data.local.db.AppDbHelper;
import com.drsaina.mars.testnotification.Data.local.db.model.messages;
import com.drsaina.mars.testnotification.Data.remot.MessageListener;
import com.drsaina.mars.testnotification.Data.remot.MessageRepository;
import com.drsaina.mars.testnotification.Data.remot.Model.CoreChatRoom;
import com.drsaina.mars.testnotification.Data.remot.Model.CoreMessage;
import com.drsaina.mars.testnotification.Data.remot.Model.CoreUser;
import com.drsaina.mars.testnotification.Data.remot.config.RetrofitProvider;
import com.drsaina.mars.testnotification.Data.remot.service.MessageService;
import com.drsaina.mars.testnotification.Utill.ConvertType;


/**
 * Created by mars on 1/15/2018.
 */
public class SyncMessageChatRoomService extends Service {

    List <messages> queueListMessage = new ArrayList <> ( );
    MessageRepository messageRepository;
    private boolean start = false;

    @Override
    public int onStartCommand ( Intent intent, int flags, int startId ) {

        Log.d ( "marsi ", " onStartCommand" );

        RetrofitProvider provider = new RetrofitProvider ( );
        MessageService messageService = provider.getMessageService ( );
        messageRepository = new MessageRepository ( messageService );
        new Thread ( ) {
            public void run ( ) {
                while (true) {
                    if ( queueListMessage.isEmpty ( ) ) {
                        List <messages> messageList = LoadMessage ( );
                        if ( ! messageList.isEmpty ( ) ) {
                            for ( messages msg : messageList ) {
                                if ( ! queueListMessage.contains ( msg ) ) {
                                    queueListMessage.add ( msg );
                                }
                            }
                            saveMessage ( );
                        }
                    }
                }
            }
        }.start ( );


        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind ( Intent intent ) {
        return null;
    }

    public List <messages> LoadMessage ( ) {
        return AppDbHelper.getListMessage ( );
    }

    public void saveMessage ( ) {
        for ( messages item : queueListMessage ) {
            messageRepository.saveMessage ( ConvertType.toCoreMessage ( item ), new MessageListener.saveMessage ( ) {
                @Override
                public void OnSuccess ( CoreMessage message ) {
                    message.setMessageState ( "send" );
                    AppDbHelper.deleteMessage ( getApplicationContext ( ), item.getId ( ) );
                    queueListMessage.remove ( item );
                }

                @Override
                public void OnError ( Object object ) {
                    queueListMessage.remove ( item );
                    item.setMessageFetch ( false );
                    AppDbHelper.updateMessage ( item );
                }
            } );
        }
    }

}
