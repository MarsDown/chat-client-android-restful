package com.drsaina.mars.testnotification.features.ChatRoom;

import com.drsaina.mars.testnotification.Interface.DownloadListener;
import com.drsaina.mars.testnotification.Data.remot.Model.MessagesView;
import com.drsaina.mars.testnotification.Interface.OnUpdateDuration;


/**
 * Created by mars on 1/11/2018.
 */

public interface ChatRoomAdapterListener {
    void DownloadFile ( int position,MessagesView message, DownloadListener downloadListener );

    void zoomImage ( MessagesView message);

    void playSound ( String url, int position );

    void pauseSound ( );
}
