package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

/**
 * This class implements the mineral sweeper which sweeps up the minerals from the ground into
 * the dumper.
 * @author Henry
 * @version 1.1
 */
public class Sweeper {

    private DcMotor liftMotor;
    private DcMotor sliderMotor;

    // Sweeps the minerals in and out
    private CRServo sweeperServo;

    /**
     * Initializes the hardware on the sweeper class
     * @param hwMap Hardware Map
     */
    public void init(HardwareMap hwMap) {
        sweeperServo = hwMap.get(CRServo.class, HardwareName.CRSERVO_SWEEPER);
        liftMotor = hwMap.get(DcMotor.class, HardwareName.MOTOR_SWEEPER_LIFT);
        sliderMotor = hwMap.get(DcMotor.class, HardwareName.MOTOR_HORIZONTAL_SLIDER);

        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        sweeperServo.setPower(0.0);
        liftMotor.setPower(0.0);
        sliderMotor.setPower(0.0);
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

    /**
     * Set the power of the silder power
     * @param power Slider Power
     */
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

    /**
     * Gets the slider motor's current power
     * @return Slider Motor's Power
     */
    public double getSliderPower() {
        return sliderMotor.getPower();
    }
}
