package com.drsaina.mars.testnotification.Data.remot;

import android.util.Log;

import com.drsaina.mars.testnotification.Data.remot.service.MessageService;
import com.drsaina.mars.testnotification.Data.remot.Model.CoreMessage;
import com.drsaina.mars.testnotification.General;
import com.drsaina.mars.testnotification.Utill.FileUtil;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.drsaina.mars.testnotification.Data.remot.config.ClientConfig.REST_API_BASE_URL;

/**
 * Created by mars on 2/12/2018.
 */

public class MessageRepository {

    MessageService messageService;

    public MessageRepository ( MessageService messageService ) {
        this.messageService = messageService;
    }

    public void saveMessage ( CoreMessage message, MessageListener.saveMessage listener ) {
        Log.d ( "marsi save messgae web ", message.getMessageId ( ) + ":" + message.getMessageContext ( ) );

        if ( message.getMessageType ( ).equals ( "text" ) ) {
            Call <CoreMessage> call = messageService.saveMessage ( message );
            call.enqueue ( new Callback <CoreMessage> ( ) {
                @Override
                public void onResponse ( Call <CoreMessage> call, Response <CoreMessage> response ) {
                    if ( response != null && response.isSuccess () ) {
                        listener.OnSuccess ( response.body () );
                    } else
                        listener.OnError ( response );
                }

                @Override
                public void onFailure ( Call <CoreMessage> call, Throwable t ) {
                    listener.OnError ( listener );
                }
            } );
        } else {

            File file = new File ( message.getMessagePath ( ) );
            Ion.with ( General.context )
               .load ( REST_API_BASE_URL+"message" )
               .setMultipartParameter ( "chatId", String.valueOf ( message.getChatRoom ( ).getChatId ( ) ) )
               .setMultipartParameter ( "userId", String.valueOf ( message.getUser ( ).getUserId ( ) ) )
               .setMultipartFile ( "filename", FileUtil.getMimeType ( file.getAbsolutePath ( ) ), file )
               .asJsonObject ( )
               .setCallback ( ( e, result ) -> {
                   try {
                       JSONObject responseobj = new JSONObject ( String.valueOf ( result ) );
                       listener.OnSuccess ( message );
                   } catch ( JSONException e1 ) {
                       listener.OnError ( e1 );
                   }
               } );
        }

    }
}
