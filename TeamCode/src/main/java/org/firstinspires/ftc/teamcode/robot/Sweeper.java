package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Sweeper {

    private DcMotor sweeperMotor;
    private DcMotor liftMotor;
    private DcMotor sliderMotor;

    public void init(HardwareMap hwMap) {
        sweeperMotor = hwMap.get(DcMotor.class, "motor_sweeper");
        sliderMotor = hwMap.get(DcMotor.class, "motor_sweeper_slider");
        liftMotor = hwMap.get(DcMotor.class, "motor_sweeper_lift");
    }

    public void setSweeperPower(double power) {
        sweeperMotor.setPower(power);
    }

    public void setSliderPower(double power) {
        sliderMotor.setPower(power);
    }

    public void setLiftPower(double power) {
        liftMotor.setPower(power);
    }
}
