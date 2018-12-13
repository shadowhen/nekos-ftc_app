package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * This class implements the mineral sweeper which sweeps up the minerals from the ground into
 * the dumper.
 * @author Henry
 * @version 1.0
 */
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

    public DcMotor getSweeperMotor() {
        return sweeperMotor;
    }

    public DcMotor getLiftMotor() {
        return liftMotor;
    }

    public DcMotor getSliderMotor() {
        return sliderMotor;
    }
}
