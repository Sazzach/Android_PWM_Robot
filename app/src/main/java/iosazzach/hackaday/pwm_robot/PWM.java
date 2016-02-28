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

    // Instance of pwm
    private static PWM pwm;

    // Only one pwm controller is desired so use singleton.
    public static PWM getPWM() {
        if(pwm == null) {
            pwm = new PWM();
        }

        return pwm;
    }



    private final int sampleRate = AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC);

    private AudioTrack audioTrack;

    // In seconds.
    private final double period = 0.02;
    private double pulseWidth = 0.0015;

    private short[] pwmBuffer;

    private PWM() {
        pwmBuffer = new short[(int) (sampleRate * period)];

        audioTrack = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                pwmBuffer.length * 2,
                AudioTrack.MODE_STATIC);

        updateBuffer();

        audioTrack.setLoopPoints(0, pwmBuffer.length, -1);
    }

    public void start() {
        audioTrack.play();
    }

    public void stop() {
        audioTrack.pause();
    }

    public void setPulseWidth(double pulseWidth) {
        assert(pulseWidth <= sampleRate);

        this.pulseWidth = pulseWidth;

        updateBuffer();
    }

    private void updateBuffer() {
        int onLength = (int) (sampleRate * pulseWidth);

        for(int i = 0; i < pwmBuffer.length; i++) {
            if(i <= onLength) {
                pwmBuffer[i] = Short.MAX_VALUE;
            }
            else {
                pwmBuffer[i] = Short.MIN_VALUE;
            }
        }

        audioTrack.write(pwmBuffer, 0, pwmBuffer.length);
    }
}
