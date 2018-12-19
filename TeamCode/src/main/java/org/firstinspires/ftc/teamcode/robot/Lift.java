package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * This class implements the robotic function known as the lift.
 * @author Henry
 * @version 1.0
 */
public class Lift {

    private static final double WHEEL_DIAMETER_MM = 0.0;
    private static final double COUNTS_PER_REV = 1440;
    private static final double COUNTS_PER_MM = (COUNTS_PER_REV / (WHEEL_DIAMETER_MM * Math.PI));

    private DcMotor liftMotor;

    /**
     * Initializes the hardware
     * @param hwMap Hardware Map
     */
    public void init(HardwareMap hwMap) {
        liftMotor = hwMap.get(DcMotor.class, "motor_lift");

        // Uncomment the line below if the motor uses an encoder.
        //liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /**
     * Set the lift power
     * @param power Power
     */
    public void setLiftPower(double power) {
        liftMotor.setPower(power);
    }

    /**
     * Returns the lift motor
     * @return Lift Motor
     */
    public DcMotor getLiftMotor() {
        return liftMotor;
    }
}
