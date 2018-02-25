package com.drsaina.mars.testnotification.Utill;

import com.drsaina.mars.testnotification.Data.local.db.model.messages;
import com.drsaina.mars.testnotification.Data.remot.Model.CoreChatRoom;
import com.drsaina.mars.testnotification.Data.remot.Model.CoreMessage;
import com.drsaina.mars.testnotification.Data.remot.Model.CoreUser;

/**
 * Created by mars on 2/17/2018.
 */

public class ConvertType {

    public static CoreMessage toCoreMessage ( messages msg ) {
        CoreMessage coreMessage = new CoreMessage ( );
        coreMessage.setMessageContext ( msg.getMessageContent ( ) );
        coreMessage.setMessagePath ( msg.getMessageFileAttach ( ) );
        coreMessage.setMessageDate ( msg.getMessageDateUpload ( ) );
        coreMessage.setMessageType ( msg.getMessageType ( ) );
        coreMessage.setMessageState ( msg.getMessageState ( ) );

        CoreChatRoom chatRoom = new CoreChatRoom ( );
        chatRoom.setChatId ( Integer.parseInt ( msg.getMessageQuestionId ( ) ) );

        CoreUser user = new CoreUser ( );
        user.setUserId ( Integer.parseInt ( msg.getUserId ( ) ) );

        coreMessage.setChatRoom ( chatRoom );
        coreMessage.setUser ( user );

        return coreMessage;
    }

    public static messages toMessaage ( CoreMessage coreMessage ) {
        messages msg = new messages ( );
        msg.setMessageFileAttach ( coreMessage.getMessagePath ( ) );
        msg.setMessageContent ( coreMessage.getMessageContext ( ) );
        msg.setMessageQuestionId ( String.valueOf ( coreMessage.getChatRoom ( ).getChatId ( ) ) );
        msg.setUserId ( String.valueOf ( coreMessage.getUser ( ).getUserId ( ) ) );
        msg.setMessageType ( coreMessage.getMessageType ( ) );
        msg.setMessageFetch ( false );
        return msg;
    }


}
