package iosazzach.hackaday.pwm_robot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    PWM pwm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SeekBar pwmBar = (SeekBar) findViewById(R.id.seekBar);
        pwmBar.setOnSeekBarChangeListener(new PwmBarListener());

        pwm = PWM.getPWM();
    }

    private class PwmBarListener implements SeekBar.OnSeekBarChangeListener {
        int progress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            TextView value = (TextView) findViewById(R.id.textView);
            value.setText(Integer.toString(progress));

            this.progress = progress;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            pwm.setPulseWidth(1.5e-3 + (progress - 50) / 100e3);
        }
    }

    public void play(View view) {
        pwm.start();
    }

    public void stop(View view) {
        pwm.stop();
    }
}
