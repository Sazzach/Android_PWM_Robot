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

    private static double freq = 1000;
    private static double periodLength = sampleRate / freq;

    public static void setFreq(double freq) {
        Log.d("SampleRate", Integer.toString(sampleRate));

        PWM.freq = freq;
        periodLength = sampleRate / freq;
    }

    public static void initSound() {

        for(int i = 0; i < bufferSize; i++) {
            audioBuffer[i] = (short) (Short.MAX_VALUE * Math.sin((Math.PI * 2 * freq * i) / sampleRate));
        }

        initialized = true;
    }

    public static void updateSound() {
        initSound();
        audioTrack.stop();
        audioTrack.write(audioBuffer, 0, bufferSize);
        audioTrack.setLoopPoints(0, (int) (periodLength * 100), -1);
        audioTrack.play();
    }

    public static void start() {
        // TODO Find out what is causing sound artifacts at 71
        
        if(audioTrack.getPlayState() != AudioTrack.PLAYSTATE_PLAYING) {
            String message;

            if(audioTrack.getState() == AudioTrack.STATE_NO_STATIC_DATA) {
                initSound();

                audioTrack.write(audioBuffer, 0, bufferSize);

                message = "Init";
            }
            else {
                audioTrack.pause();

                initSound();

                audioTrack.write(audioBuffer, 0, bufferSize);

                audioTrack.reloadStaticData();

                message = "No Init";
            }

            Log.d("init", message);

            int result = audioTrack.setLoopPoints(0, (int) (periodLength * 100), -1);
            if(result == AudioTrack.SUCCESS) {
                message = "Success";
            }
            else if(result == AudioTrack.ERROR_BAD_VALUE) {
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
