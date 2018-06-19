package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;

/**
 * This class extends the Bot to have drive motors and be able to drive around.
 */
public class DriveBot extends Bot {

    protected DcMotor[] motorLeftDrive = new DcMotor[2];
    protected DcMotor[] motorRightDrive = new DcMotor[2];

    protected DcMotor motorArm;

    public static final double DRIVE_SPEED = 0.3; // Speed for drive motors using encoders
    public static final double ARM_SPEED = 0.3;   // Speed for moving the arm

    public DriveBot() {
        this(null, null);
    }

    public DriveBot(HardwareMap hwMap, Telemetry telemetry) {
        super();

        if (hwMap != null && telemetry != null) {
            init(hwMap, telemetry);
        }
    }

    @Override
    public void init(HardwareMap ahwMap, Telemetry atelemetry) {
        hwMap = ahwMap;
        telemetry = atelemetry;

        // Get hardware references from the configuration file on the robot controller phone
        motorLeftDrive[0] = hwMap.get(DcMotor.class, "motor-left-front");
        motorLeftDrive[1] = hwMap.get(DcMotor.class, "motor-left-rear");
        motorRightDrive[0] = hwMap.get(DcMotor.class, "motor-right-front");
        motorRightDrive[1] = hwMap.get(DcMotor.class, "motor-right-rear");

        motorArm = hwMap.get(DcMotor.class, "motor-arm");

        // Reverse the right drive motors so the robot can tank drive
        motorRightDrive[0].setDirection(DcMotorSimple.Direction.REVERSE);
        motorRightDrive[1].setDirection(DcMotorSimple.Direction.REVERSE);

        setDrivePower(0.0, 0.0);
    }

    /**
     * Set the power of the left and right drive motors
     * @param leftPower  Left drive motors' power
     * @param rightPower Right drive motors' power
     */
    public void setDrivePower(double leftPower, double rightPower) {

        for (DcMotor motor: motorLeftDrive) {
            motor.setPower(leftPower);
        }
        for (DcMotor motor: motorRightDrive) {
            motor.setPower(rightPower);
        }
    }

    public double getArmPower() {
        return motorArm.getPower();
    }

    public void setArmPower(double armPower) {
        motorArm.setPower(armPower);
    }

    public DcMotor getMotorArm() {
        return motorArm;
    }
}
