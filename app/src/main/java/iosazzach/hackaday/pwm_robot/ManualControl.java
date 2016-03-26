package iosazzach.hackaday.pwm_robot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class ManualControl extends AppCompatActivity {

    MotorController motorController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_control);

        SeekBar leftPwmBar = (SeekBar) findViewById(R.id.leftSeekBar);
        leftPwmBar.setOnSeekBarChangeListener(new LeftPwmBarListener());

        SeekBar rightPwmBar = (SeekBar) findViewById(R.id.rightSeekBar);
        rightPwmBar.setOnSeekBarChangeListener(new RightPwmBarListener());

        motorController = MotorController.getMotorController();
    }

    private class LeftPwmBarListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            TextView value = (TextView) findViewById(R.id.leftValueText);

            double velocity = (progress - 50) / 50.0;

            value.setText(Double.toString(velocity));

            motorController.setLeftVelocity(velocity);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }

    private class RightPwmBarListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            TextView value = (TextView) findViewById(R.id.rightValueText);

            double velocity = (progress - 50) / 50.0;

            value.setText(Double.toString(velocity));

            motorController.setRightVelocity(velocity);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }

    public void play(View view) {
        motorController.start();
    }

    public void stop(View view) {
        motorController.stop();
    }
}
