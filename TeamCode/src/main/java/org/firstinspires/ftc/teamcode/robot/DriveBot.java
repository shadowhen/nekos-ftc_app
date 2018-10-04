package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class DriveBot {

    protected HardwareMap hwMap;
    protected Telemetry telemetry;

    // Drive motors
    protected DcMotor motorDriveLeftFront;
    protected DcMotor motorDriveLeftRear;
    protected DcMotor motorDriveRightFront;
    protected DcMotor motorDriveRightRear;

    public void init(HardwareMap ahwMap, Telemetry atelemetry) {
        hwMap = ahwMap;
        telemetry = atelemetry;

        // Get hardware references from the robot controller's configuration for hardware devices
        motorDriveLeftFront  = hwMap.get(DcMotor.class, "motor_drive_lf");
        motorDriveLeftRear   = hwMap.get(DcMotor.class, "motor_drive_lr");
        motorDriveRightFront = hwMap.get(DcMotor.class, "motor_drive_rf");
        motorDriveRightRear  = hwMap.get(DcMotor.class, "motor_drive_rr");

        // Reverses the right drive motors' direction
        motorDriveRightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        motorDriveRightRear.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void addData() {
        if (telemetry != null) {
            addData(telemetry);
        }
    }

    public void addData(Telemetry telemetry) {
        telemetry.addData("LF Power", motorDriveLeftFront.getPower());
        telemetry.addData("LR Power", motorDriveLeftRear.getPower());
        telemetry.addData("RF Power", motorDriveRightFront.getPower());
        telemetry.addData("RR Power", motorDriveRightRear.getPower());
    }

    public void setDriveMode(DcMotor.RunMode runMode) {
        motorDriveLeftFront.setMode(runMode);
        motorDriveLeftRear.setMode(runMode);
        motorDriveRightFront.setMode(runMode);
        motorDriveRightRear.setMode(runMode);
    }

    public void setDrivePower(double leftPower, double rightPower) {
        motorDriveLeftFront.setPower(leftPower);
        motorDriveLeftRear.setPower(leftPower);
        motorDriveRightFront.setPower(rightPower);
        motorDriveRightRear.setPower(rightPower);
    }

    public void setDrivePowerSideways(double leftPower, double rightPower) {
        motorDriveLeftFront.setPower(-leftPower);
        motorDriveLeftRear.setPower(leftPower);
        motorDriveRightFront.setPower(rightPower);
        motorDriveRightRear.setPower(-rightPower);
    }
}
