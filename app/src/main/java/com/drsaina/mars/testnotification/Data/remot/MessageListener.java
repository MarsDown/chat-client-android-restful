package com.drsaina.mars.testnotification.Data.remot;

import com.drsaina.mars.testnotification.Data.remot.Model.CoreMessage;

import java.util.List;

/**
 * Created by mars on 2/17/2018.
 */

public interface MessageListener {

    interface onLoadListMessages{
        public void OnSuccess( List<CoreMessage> messages);
        public void OnError(Object object);
    }

    interface onLoadMessage
    {
        public void OnSuccess( CoreMessage message);
        public void OnError(Object object);
    }

    interface saveMessage
    {
        public void OnSuccess( CoreMessage message);
        public void OnError(Object object);
    }



}
