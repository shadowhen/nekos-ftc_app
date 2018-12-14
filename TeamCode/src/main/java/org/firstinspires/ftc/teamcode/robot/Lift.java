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

    private DcMotor liftMotor;

    /**
     * Initializes the hardware
     * @param hwMap Hardware Map
     */
    public void init(HardwareMap hwMap) {
        liftMotor = hwMap.get(DcMotor.class, "motor_lift");
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
