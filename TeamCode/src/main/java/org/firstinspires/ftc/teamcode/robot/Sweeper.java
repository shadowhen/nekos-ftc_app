package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * This class implements the mineral sweeper which sweeps up the minerals from the ground into
 * the dumper.
 * @author Henry
 * @version 1.0
 */
public class Sweeper {

    private DcMotor liftMotor;
    private DcMotor sliderMotor;

    // Sweeps the minerals in and out
    private CRServo sweeperServo;

    /**
     * Initializes the hardware
     * @param hwMap Hardware Map
     */
    public void init(HardwareMap hwMap) {
        sweeperServo = hwMap.get(CRServo.class, "servo_sweeper");
        liftMotor = hwMap.get(DcMotor.class, "motor_sweeper_lift");
        sliderMotor = hwMap.get(DcMotor.class, "motor_sweeper_slider");

        sweeperServo.setPower(0.0);
    }

    /**
     * Get the lift motor
     * @return Lift Motor
     */
    public DcMotor getLiftMotor() {
        return liftMotor;
    }

    public DcMotor getSliderMotor() {
        return sliderMotor;
    }

    /**
     * Get the sweeper servo
     * @return Sweeper Servo
     */
    public CRServo getSweeperServo() {
        return sweeperServo;
    }

    /**
     * Set the sweeper power
     * @param power Power
     */
    public void setSweeperPower(double power) {
        sweeperServo.setPower(power);
    }

    /**
     * Set the lift power
     * @param power Power
     */
    public void setLiftPower(double power) {
        liftMotor.setPower(power);
    }

    public void setSliderPower(double power) {
        sliderMotor.setPower(power);
    }

    /**
     * Get the sweeper motor's power
     * @return Sweeper motor's power
     */
    public double getSweeperPower() {
        return sweeperServo.getPower();
    }

    /**
     * Get the sweeper lift motor's power
     * @return Sweeper lift motor's power
     */
    public double getLiftPower() {
        return liftMotor.getPower();
    }

    public double getSliderPower() {
        return sliderMotor.getPower();
    }
}
