package iosazzach.hackaday.pwm_robot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    PWM pwm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SeekBar leftPwmBar = (SeekBar) findViewById(R.id.leftSeekBar);
        leftPwmBar.setOnSeekBarChangeListener(new LeftPwmBarListener());

        SeekBar rightPwmBar = (SeekBar) findViewById(R.id.rightSeekBar);
        rightPwmBar.setOnSeekBarChangeListener(new RightPwmBarListener());

        pwm = PWM.getPWM();
    }

    private class LeftPwmBarListener implements SeekBar.OnSeekBarChangeListener {
        int progress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            TextView value = (TextView) findViewById(R.id.leftValueText);
            value.setText(Integer.toString(progress));

            this.progress = progress;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            pwm.setLeftPulseWidth(1.5e-3 + (progress - 50) / 100e3);
        }
    }

    private class RightPwmBarListener implements SeekBar.OnSeekBarChangeListener {
        int progress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            TextView value = (TextView) findViewById(R.id.rightValueText);
            value.setText(Integer.toString(progress));

            this.progress = progress;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            pwm.setRightPulseWidth(1.5e-3 + (progress - 50) / 100e3);
        }
    }

    public void play(View view) {
        pwm.start();
    }

    public void stop(View view) {
        pwm.stop();
    }
}
