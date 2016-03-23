package iosazzach.hackaday.pwm_robot;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * Created by samuel on 12/29/15.
 */

public class PWM {

    public static final double CENTER_LEFT = 1.5e-3;
    public static final double RANGE_LEFT = 0.5e-3;

    public static final double CENTER_RIGHT = 1.5e-3;
    public static final double RANGE_RIGHT = 0.5e-3;

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
    private double leftPulseWidth = 0.0015;
    private double rightPulseWidth = 0.0015;

    private int samples;
    private short[] pwmBuffer;

    private PWM() {
        samples = (int) (sampleRate * period);
        pwmBuffer = new short[samples * 2];

        audioTrack = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                sampleRate,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                pwmBuffer.length * 2,
                AudioTrack.MODE_STATIC);

        updateBuffer();
    }

    public void start() {
        audioTrack.setLoopPoints(0, samples, -1);

        audioTrack.play();
    }

    public void stop() {
        audioTrack.pause();
    }

    public void setLeftPulseWidth(double leftPulseWidth) {
        if(leftPulseWidth > period) {
            this.leftPulseWidth = period;
        }
        else {
            this.leftPulseWidth = leftPulseWidth;
        }

        updateBuffer();
    }

    public void setRightPulseWidth(double rightPulseWidth) {
        if(rightPulseWidth > period) {
            this.rightPulseWidth = period;
        }
        else {
            this.rightPulseWidth = rightPulseWidth;
        }

        updateBuffer();
    }

    private void updateBuffer() {
        int leftOnSamples = (int) (sampleRate * leftPulseWidth);
        int rightOnSamples = (int) (sampleRate * rightPulseWidth);

        for(int i = 0; i < samples; i++) {
            if(i <= leftOnSamples) {
                pwmBuffer[i * 2] = Short.MAX_VALUE;
            }
            else {
                pwmBuffer[i * 2] = Short.MIN_VALUE;
            }

            if(i <= rightOnSamples) {
                pwmBuffer[i * 2 + 1] = Short.MAX_VALUE;
            }
            else {
                pwmBuffer[i * 2 + 1] = Short.MIN_VALUE;
            }
        }

        audioTrack.write(pwmBuffer, 0, pwmBuffer.length);
    }
}
