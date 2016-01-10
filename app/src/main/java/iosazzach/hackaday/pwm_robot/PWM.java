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
    private static final int bufferSize = 10000;

    private static final AudioTrack audioTrack = new AudioTrack(
            AudioManager.STREAM_MUSIC,
            sampleRate,
            AudioFormat.CHANNEL_OUT_STEREO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize,
            AudioTrack.MODE_STREAM);

    // Stores a running total of how many frames of audio have been written to the audio track.
    private static int frames;

    // How many discrete pwm values can be produced
    // Higher values result in slower PWM frequencies.
    private static final int pwmResolution = 100;

    // AudioTrack will be written to when playback position is writeThreshold frames away
    // from the total number of frames written.
    private static final int writeThreshold = pwmResolution * 10;

    // Number of pwm pulses stored in pwmBuffer
    // Apparently if this is too small the sound won't play.
    private static final int pwmBufferPulseCount = 100;

    private static short[] pwmBuffer = new short[pwmResolution * pwmBufferPulseCount];

    // Frequency of the pwm signal
    private static final int pwmFreq = sampleRate / pwmResolution;

    // Frequency of changes to pwm signal
    private static final int pwmUpdateFreq = 100;

    private static double leftPwmVal = 0.0;
    private static boolean leftChanged = true;

    private static double rightPwmVal = 0.0;
    private static boolean rightChanged = true;

    private static Timer pwmManager = null;

    public static void start() {
        if(pwmManager == null) {
            leftChanged = true;
            rightChanged = true;

            pwmManager = new Timer();

            // Division converts hz to milliseconds
            pwmManager.scheduleAtFixedRate(new PwmRunner(), 0, 1000 / pwmUpdateFreq);

            Log.d("Start", "Started");
        }
    }

    public static void stop() {
        if(pwmManager != null) {
            pwmManager.cancel();
            pwmManager = null;

            audioTrack.pause();
            audioTrack.flush();
        }
    }

    public static void setPwmLeft(double pwmVal) {
        leftPwmVal = pwmVal;
        leftChanged = true;
    }

    public static void setPwmRight(double pwmVal) {
        rightPwmVal = pwmVal;
        rightChanged = true;
    }

    private static void updateBuffer(int channel, double value) {
        Log.d("Updating Buffer", Double.toString(value));

        int onTime = (int) (pwmResolution * value);
        int offTime = pwmResolution - onTime;

        int i = 0;
        while(i < pwmBuffer.length / 2) {
            int j;

            for(j = i; j < i + offTime && j < pwmBuffer.length / 2; j++) {
                pwmBuffer[i*2 + channel] = Short.MIN_VALUE;
            }
            i = j;

            for(; j < i + onTime && j < pwmBuffer.length / 2; j++) {
                pwmBuffer[i*2 + channel] = Short.MAX_VALUE;
            }
            i = j;
        }
    }

    private static class PwmRunner extends TimerTask {

        @Override
        public void run() {
            Log.v("Running", Integer.toString(audioTrack.getPlayState()));

            if(leftChanged || rightChanged) {
                if(leftChanged) {
                    updateBuffer(0, leftPwmVal);

                    leftChanged = false;
                }
                if(rightChanged) {
                    updateBuffer(1, rightPwmVal);

                    rightChanged = false;
                }

                audioTrack.pause();
                audioTrack.flush();

                // TODO Deal with written. I think that I can get away with ignoring it for now.
                int written = audioTrack.write(pwmBuffer, 0, pwmBuffer.length);
                frames = written / 2;

                Log.d("Written", Integer.toString(written));

                audioTrack.play();
            }
            else if(audioTrack.getPlaybackHeadPosition() + writeThreshold >= frames) {
                int written = audioTrack.write(pwmBuffer, 0, pwmBuffer.length);
                frames += written / 2;

                Log.v("Updated Buffer", Integer.toString(written));
            }
        }
    }
}
