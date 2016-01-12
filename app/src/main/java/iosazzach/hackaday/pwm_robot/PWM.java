package iosazzach.hackaday.pwm_robot;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by samuel on 12/29/15.
 */

public class PWM {

    // Sample rate of pwm signal.
    // ~ 50kHz
    private static final int sampleRate = AudioTrack.getNativeOutputSampleRate(
            AudioManager.STREAM_MUSIC);

    // Size of internal buffer of audio stream
    private static final int bufferSize = 50000;

    private static final AudioTrack audioTrack = new AudioTrack(
            AudioManager.STREAM_MUSIC,
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize,
            AudioTrack.MODE_STATIC);

    private static final short[] audioBuffer = new short[bufferSize];

    private static boolean initialized = false;

    private static int freq = 1000;
    private static int periodLength = sampleRate / freq;

    public static void initSound() {

        for(int i = 0; i < bufferSize; i++) {
            audioBuffer[i] = (short) (Short.MAX_VALUE * Math.sin((Math.PI * 2 * freq * i) / sampleRate));
        }

        initialized = true;
    }

    public static void start() {
        if(audioTrack.getPlayState() != audioTrack.PLAYSTATE_PLAYING) {

            if(audioTrack.getState() == audioTrack.STATE_NO_STATIC_DATA) {
                initSound();

                audioTrack.write(audioBuffer, 0, bufferSize);
            }
            else {
                audioTrack.reloadStaticData();
            }

            String message;

            int result = audioTrack.setLoopPoints(0, periodLength * 100, -1);
            if(result == audioTrack.SUCCESS) {
                message = "Success";
            }
            else if(result == audioTrack.ERROR_BAD_VALUE) {
                message = "Error bad value";
            }
            else {
                message = "Error invalid operation";
            }

            Log.d("Loop", message);

            audioTrack.play();
        }
        else {
            Log.d("Invalid", "Invalid");
        }
    }

    public static void stop() {
        audioTrack.pause();
    }
}
