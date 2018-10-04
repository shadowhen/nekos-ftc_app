package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class AutoBot extends DriveBot {

    private static final double COUNTS_PER_REV = 1120;
    private static final double WHEEL_DIAMETER_MM = 31.2;
    private static final double COUNTS_PER_MM = (COUNTS_PER_REV) / (WHEEL_DIAMETER_MM * Math.PI);

    private ElapsedTime timer;

    @Override
    public void init(HardwareMap ahwMap, Telemetry atelemetry) {
        super.init(ahwMap, atelemetry);

        timer = new ElapsedTime();

        // Reset the encoders and allow the drive motors to run with encoder
        setDriveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public static int convertDistanceToPosition(double distance) {
        return (int)(distance * COUNTS_PER_MM);
    }

    public void moveByEncoder(double speed, double leftDist, double rightDist, double timeoutS) {
        int newLeftTarget = convertDistanceToPosition(leftDist);
        int newRightTarget = convertDistanceToPosition(rightDist);

        // Turns on RUN_TO_TARGET mode
        setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);

        motorDriveLeftFront.setTargetPosition(motorDriveLeftFront.getCurrentPosition() + newLeftTarget);
        motorDriveLeftRear.setTargetPosition(motorDriveLeftRear.getCurrentPosition() + newLeftTarget);
        motorDriveRightFront.setTargetPosition(motorDriveRightFront.getCurrentPosition() + newRightTarget);
        motorDriveRightRear.setTargetPosition(motorDriveRightRear.getCurrentPosition() + newRightTarget);

        timer.reset();
        timer.startTime();

        while ((motorDriveLeftFront.isBusy() && motorDriveLeftRear.isBusy() && motorDriveRightFront.isBusy() && motorDriveRightRear.isBusy()) && (timer.seconds() < timeoutS)) {
            setDrivePower(speed, speed);
        }

        // Stop the drive motors
        setDrivePower(0, 0);

        // Turns off RUN_TO_TARGET mode
        setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
