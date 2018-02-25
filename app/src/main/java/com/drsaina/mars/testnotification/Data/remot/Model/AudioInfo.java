package com.drsaina.mars.testnotification.Data.remot.Model;

/**
 * Created by mars on 2/22/2018.
 */

public class AudioInfo {

    private String totalTime;
    private int seekbarValue;
    private String statePlay;

    public String getStatePlay ( ) {
        return statePlay;
    }

    public void setStatePlay ( String statePlay ) {
        this.statePlay = statePlay;
    }

    public String getTotalTime ( ) {
        return totalTime;
    }

    public void setTotalTime ( String totalTime ) {
        this.totalTime = totalTime;
    }

    public int getSeekbarValue ( ) {
        return seekbarValue;
    }

    public void setSeekbarValue ( int seekbarValue ) {
        this.seekbarValue = seekbarValue;
    }
}
