package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * This class implements the robotic function known as the lift.
 * @author Henry
 * @version 1.0
 */
public class Lift {

    private static final double GEAR_DIAMETER_MM = 3.0;
    private static final double COUNTS_PER_REV = 1440;
    private static final double COUNTS_PER_MM = (COUNTS_PER_REV / (GEAR_DIAMETER_MM * Math.PI));

    private DcMotor liftMotor;

    /**
     * Initializes the hardware
     * @param hwMap Hardware Map
     */
    public void init(HardwareMap hwMap) {
        liftMotor = hwMap.get(DcMotor.class, "motor_lift");

        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        liftMotor.setPower(Math.abs(0));
    }

    public static int convertDistanceToTarget(double distance) {
        return (int)(distance * COUNTS_PER_MM);
    }

    /**
     * Set the power of the lift motor
     * @param power Power
     */
    public void setLiftPower(double power) {
        liftMotor.setPower(power);
    }

    public DcMotor getLiftMotor() {
        return liftMotor;
    }

    /**
     * Returns the power of the lift motor
     * @return Lift Motor Power
     */
    public double getLiftPower() {
        return liftMotor.getPower();
    }
}
