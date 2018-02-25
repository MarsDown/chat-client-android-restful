package com.drsaina.mars.testnotification.Data.local.db;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.drsaina.mars.testnotification.Data.local.db.model.messagesDao;
import com.drsaina.mars.testnotification.Data.local.db.model.messages;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by mars on 2/17/2018.
 */

public class AppDbHelper {

    public static List <messages> getListMessage ( ) {
        messagesDao dao = DbContext.newSession ( ).getMessagesDao ( );
        QueryBuilder <messages> qb = dao.queryBuilder ( );
        List <messages> list = qb.where ( messagesDao.Properties.MessageFetch.eq ( false ) ).list ( );
        changeFetch ( list );

        return list;
    }

    public static void changeFetch ( List <messages> list ) {
        for ( messages item : list ) {
            item.setMessageFetch ( true );
            updateMessage ( item );
        }
    }

    public static long insertMessage ( messages message ) {
        messagesDao dao = DbContext.newSession ( ).getMessagesDao ( );
        return dao.insert ( message );
    }

    public static void updateMessage ( messages message ) {
        messagesDao dao = DbContext.newSession ( ).getMessagesDao ( );
        dao.update ( message );
    }

    public static List <messages> fetchAllData ( ) {
        messagesDao dao = DbContext.newSession ( ).getMessagesDao ( );
        return dao.loadAll ( );
    }

    public static List <messages> getListMessageByQuestionId ( String questionId ) {
        messagesDao dao = DbContext.newSession ( ).getMessagesDao ( );
        QueryBuilder <messages> qb = dao.queryBuilder ( );
        return qb.where ( messagesDao.Properties.MessageQuestionId.eq ( questionId ) ).build ( ).list ( );
    }

    public static void deleteMessage ( Context context, Long key ) {
        messagesDao dao = DbContext.newSession ( ).getMessagesDao ( );
//        dao.delete ( message );
        dao.deleteByKey ( key );

        Intent broadcast = new Intent ( "Update_Message" );
        broadcast.putExtra ( "messageId", Integer.parseInt ( key + "" ) );

//        String path = "";
//        if ( message.getMessageFileAttach ( ) != null && ! message.getMessageFileAttach ( ).equals ( "" ) && message.getMessageType ( ).equals ( "sound" ) )
//            path = message.getMessageFileAttach ( );
//
//        broadcast.putExtra ( "path", path );

        context.sendBroadcast ( broadcast );

    }

    public static Long lastMessageId ( ) {
        messagesDao dao = DbContext.newSession ( ).getMessagesDao ( );
        messages unique = dao.queryBuilder ( ).orderAsc ( ).build ( ).unique ( );
        return unique.getId ( );

    }


}
