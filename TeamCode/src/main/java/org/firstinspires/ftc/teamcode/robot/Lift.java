package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class Lift {

    private DcMotor liftMotor;

    public void init(HardwareMap hwMap) {
        liftMotor = hwMap.get(DcMotor.class, "motor_lift");
    }

    public void setLiftPower(double power) {
        liftMotor.setPower(power);
    }

    public DcMotor getLiftMotor() {
        return liftMotor;
    }
}
