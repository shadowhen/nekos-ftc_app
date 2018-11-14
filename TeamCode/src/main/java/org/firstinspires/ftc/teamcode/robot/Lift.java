package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class Lift {

    public final static double MIN_LIFT_SERVO_POSITION = 0.0;
    public final static double MAX_LIFT_SERVO_POSITION = 1.0;

    private DcMotor liftMotor;
    private Servo liftServo;

    public void init(HardwareMap hwMap) {
        liftMotor = hwMap.get(DcMotor.class, "motor_lift");
        liftServo = hwMap.get(Servo.class, "servo_lift");
    }

    public void setLiftPower(double power) {
        liftMotor.setPower(power);
    }

    public void setLiftPosition(double position) {
        position = Range.clip(position, MIN_LIFT_SERVO_POSITION, MAX_LIFT_SERVO_POSITION);
        liftServo.setPosition(position);
    }
}
