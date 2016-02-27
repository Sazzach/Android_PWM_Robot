package iosazzach.hackaday.pwm_robot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SeekBar pwmBar = (SeekBar) findViewById(R.id.seekBar);
        pwmBar.setOnSeekBarChangeListener(new PwmBarListener());
    }

    private class PwmBarListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            TextView value = (TextView) findViewById(R.id.textView);
            value.setText(Integer.toString(progress));

            PWM.setFreq(480 + 48 * progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            PWM.updateSound();
        }
    }

    public void play(View view) {
        PWM.start();
    }

    public void stop(View view) {
        PWM.stop();
    }
}
