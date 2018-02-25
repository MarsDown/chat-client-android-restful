package com.drsaina.mars.testnotification.Data.remot.Model;

/**
 * Created by mars on 1/18/2018.
 */

public class MessagesView {

    private CoreMessage messages;
    private String stateDownload;
    private String stateUpload;
    private String date;
    private boolean forMe;

    private AudioInfo audioInfo;

    public AudioInfo getAudioInfo ( ) {
        return audioInfo;
    }

    public void setAudioInfo ( AudioInfo audioInfo ) {
        this.audioInfo = audioInfo;
    }

    public boolean isForMe ( ) {
        return forMe;
    }

    public void setForMe ( boolean forMe ) {
        this.forMe = forMe;
    }

    public String getDate ( ) {
        return date;
    }

    public void setDate ( String date ) {
        this.date = date;
    }

    public MessagesView ( ) {
    }

    public MessagesView ( CoreMessage message) {
        this.messages = message;
    }

    public String getStateDownload ( ) {
        return stateDownload;
    }

    public void setStateDownload ( String stateDownload ) {
        this.stateDownload = stateDownload;
    }

    public String getStateUpload ( ) {
        return stateUpload;
    }

    public void setStateUpload ( String stateUpload ) {
        this.stateUpload = stateUpload;
    }

    public CoreMessage getMessages ( ) {
        return messages;
    }

    public void setMessages ( CoreMessage messages ) {
        this.messages = messages;
    }
}
