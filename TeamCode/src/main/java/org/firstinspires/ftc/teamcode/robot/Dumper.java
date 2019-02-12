package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * This class implements the robotic function for the dumper which the dumps the mineral(s) into
 * the lander.
 * @author Henry
 * @version 1.1
 */
public class Dumper {

    public static final double MIN_POWER = -0.25;
    public static final double MAX_POWER = 0.25;

    private DcMotor motorDumper;

    /**
     * Intalizes the hardware on the Dumper class
     * @param hwMap Hardware Map
     */
    public void init(HardwareMap hwMap) {
        motorDumper = hwMap.get(DcMotor.class, "motor_dumper");
    }

    /**
     * Sets the power of the motor dumper
     * @param power Motor Power
     */
    public void setPower(double power) {
        motorDumper.setPower(power);
    }

    /**
     * Gets the power of the motor dumper
     * @return Motor Power
     */
    public double getPower() {
        return motorDumper.getPower();
    }
}
