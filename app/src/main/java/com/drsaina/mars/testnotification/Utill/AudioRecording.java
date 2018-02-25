package com.drsaina.mars.testnotification.Utill;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by mars on 2/21/2018.
 */
interface AudioUtilsListener {
    void onRecordError();
}

public class AudioRecording {

    private MediaRecorder mediaRecorder;
    private Context context;

    public AudioRecording ( Context context ) {
        this.context = context;
    }
    public void stopRecord ( ) {
        mediaRecorder.start ();
        mediaRecorder.release ();
        mediaRecorder=null;
    }

    public void startRecord (String outputPath ) {

        PackageManager pManager = context.getPackageManager ( );
        if ( pManager.hasSystemFeature ( PackageManager.FEATURE_MICROPHONE ) ) {
            File file = new File ( outputPath );
            if ( ! file.exists ( ) )
                file.mkdirs ();

            mediaRecorder = new MediaRecorder ( );

            mediaRecorder.setAudioSource ( MediaRecorder.AudioSource.MIC );
            mediaRecorder.setOutputFormat ( MediaRecorder.OutputFormat.AAC_ADTS );
            mediaRecorder.setAudioEncoder ( MediaRecorder.AudioEncoder.AAC );
            mediaRecorder.setAudioEncodingBitRate ( 16000 );
            mediaRecorder.setAudioSamplingRate ( 48000 );
            mediaRecorder.setAudioChannels ( 1 );

            // Setup the output location
            mediaRecorder.setOutputFile ( outputPath );

            // Start the recording
            try {
                mediaRecorder.prepare ( );
                mediaRecorder.start ( );
            } catch ( IOException e ) {

            }
        } else {
            Toast.makeText ( context, "این دستگاه قابلیت ضبط صوت ندارد!", Toast.LENGTH_LONG ).show ( );
        }
    }

}
