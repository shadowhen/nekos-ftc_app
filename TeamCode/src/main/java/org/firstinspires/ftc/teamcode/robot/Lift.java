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
    private DcMotor landerMotor;

    /**
     * Initializes the hardware
     * @param hwMap Hardware Map
     */
    public void init(HardwareMap hwMap) {
        liftMotor = hwMap.get(DcMotor.class, "motor_lift");
        landerMotor = hwMap.get(DcMotor.class, "motor_lander");

        //liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //landerMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Uncomment the line below if the motor uses an encoder.
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //landerMotor.setTargetPosition(0);
        //landerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

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

    /**
     * Set the power of the lander motor
     * @param power Power
     */
    public void setLanderPower(double power) {
        landerMotor.setPower(power);
    }

    public DcMotor getLiftMotor() {
        return liftMotor;
    }

    public DcMotor getLanderMotor() {
        return landerMotor;
    }

    /**
     * Returns the power of the lift motor
     * @return Lift Motor Power
     */
    public double getLiftPower() {
        return liftMotor.getPower();
    }

    /**
     * Returns the power of the lander motor
     * @return Lander Motor Power
     */
    public double getLanderPower() {
        return landerMotor.getPower();
    }
}
