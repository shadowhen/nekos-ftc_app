package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;


import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ExpermientBot extends AutoBot {

    public ExpermientBot(LinearOpMode linearOpMode) {
        super(linearOpMode);
    }

    @Override
    public void init(HardwareMap ahwMap, Telemetry atelemetry) {
        super.init(ahwMap, atelemetry);

        setDriveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void moveSideways(double speed, double distance, double timeoutS) {
        moveSideways(speed, speed, speed, speed, distance, timeoutS);
    }

    public void moveSideways(double leftFrontSpeed, double leftRearSpeed, double rightFrontSpeed, double rightRearSpeed, double distance, double timeoutS) {
        int newPosition = AutoBot.convertDistanceToPosition(distance);

        setAutoDrive(AutoDrive.SIDEWAYS);
        setDriveTargetPosition(newPosition, newPosition);
        setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);

        timer.reset();

        while (isDriveMotorsBusy() && linearOpMode.opModeIsActive() && timer.seconds() < timeoutS) {
            setDrivePower(leftFrontSpeed, leftRearSpeed, rightFrontSpeed, rightRearSpeed);

            telemetry.addData(">", "running");
            telemetry.update();
        }

        setDrivePower(0, 0);
        setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public double[] autoSpeed(double leftFrontSpeed, double leftRearSpeed, double rightFrontSpeed, double rightRearSpeed) {
        return new double[]{ leftFrontSpeed, leftRearSpeed, rightFrontSpeed, rightRearSpeed };
    }
}
