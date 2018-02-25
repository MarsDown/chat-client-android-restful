package com.drsaina.mars.testnotification.features.ChatRoom;

import com.drsaina.mars.testnotification.features.Base.IPresenter;
import com.drsaina.mars.testnotification.features.Base.IView;
import com.drsaina.mars.testnotification.Data.remot.Model.CoreChatRoom;
import com.drsaina.mars.testnotification.Data.remot.Model.MessagesView;
import com.drsaina.mars.testnotification.Data.remot.Model.CoreMessage;

import java.util.List;

/**
 * Created by mars on 2/12/2018.
 */
public interface ChatRoomContract {

    interface View extends IView <Presenter> {
        void showMessages ( List <MessagesView> messages );

        void addNewMessage ( MessagesView messagesView );

        void showChatRoomInformation ( CoreChatRoom chatRoom );

        void showTextToolbar ( );

        void hideTextToolbar ( );

        void hideNotificaton ( );

        void resetToolbarMessage ( );

        void startRecord ( );

        void stopRecord ( );

        void playMusic ( );

        void stopMusic ( );

        void updateInfoSounIsPlay(int position,int seekValue , String totalTime);
        void changeStateIconPlay(int position,String state);
    }

    interface Presenter extends IPresenter <View> {
        void onLoadChatRoomMessage ( Integer skip, Integer take );

        void onLoadLastMessage ( Integer messageId );

        void saveMessage ( String type );

        void getChatRoomInfo ( );

        void onChangeEditText ( String text );

        void onClickSendMessage ( String tag );

        void playMusic ( String url,int position );

        void stopMusic ( );
    }
}
