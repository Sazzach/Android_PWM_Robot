package iosazzach.hackaday.pwm_robot;

/**
 * Created by samuel on 3/15/16.
 */

public class MotorController {

    private static MotorController motorController;

    public static MotorController getMotorController() {
        if(motorController == null) {
            motorController = new MotorController();
        }

        return motorController;
    }



    private PWM pwm;

    private MotorController() {
        pwm = PWM.getPWM();
    }

    public void start() {
        pwm.start();
    }

    public void stop() {
        pwm.stop();
    }

    /**
     * Sets the velocity of the left motor.
     *
     * @param leftVelocity Velocity value from -1 to 1
     */
    public void setLeftVelocity(double leftVelocity) {
        pwm.setLeftPulseWidth(PWM.CENTER_LEFT + PWM.RANGE_LEFT * -leftVelocity);
    }

    /**
     * Sets the velocity of the right motor.
     *
     * @param rightVelocity Velocity value from -1 to 1
     */
    public void setRightVelocity(double rightVelocity) {
        pwm.setRightPulseWidth(PWM.CENTER_RIGHT + PWM.RANGE_RIGHT * rightVelocity);
    }
}
