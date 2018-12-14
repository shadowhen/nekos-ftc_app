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

    /**
     * Initializes the hardware
     * @param hwMap Hardware Map
     */
    public void init(HardwareMap hwMap) {
        sweeperMotor = hwMap.get(DcMotor.class, "motor_sweeper");
        sliderMotor = hwMap.get(DcMotor.class, "motor_sweeper_slider");
        liftMotor = hwMap.get(DcMotor.class, "motor_sweeper_lift");
    }

    /**
     * Set the sweeper power
     * @param power Power
     */
    public void setSweeperPower(double power) {
        sweeperMotor.setPower(power);
    }

    /**
     * Set the slider power
     * @param power Power
     */
    public void setSliderPower(double power) {
        sliderMotor.setPower(power);
    }

    /**
     * Set the lift power
     * @param power Power
     */
    public void setLiftPower(double power) {
        liftMotor.setPower(power);
    }

    /**
     * Returns the sweeper motor
     * @return Sweeper Motor
     */
    public DcMotor getSweeperMotor() {
        return sweeperMotor;
    }

    /**
     * Returns the lift motor
     * @return Lift Motor
     */
    public DcMotor getLiftMotor() {
        return liftMotor;
    }

    /**
     * Returns the slider motor
     * @return Slider Motor
     */
    public DcMotor getSliderMotor() {
        return sliderMotor;
    }
}
