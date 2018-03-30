package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class DriveBot extends Bot {

    private double driveSpeedLimit = 1.00;

    public DriveBot(){
        super();
    }

    @Override
    public void init(HardwareMap ahwMap, Telemetry _telemetry){
        super.init(ahwMap, _telemetry);

        for (int i = 0; i < getMotorDrive().length; i++) {
            getMotorDrive()[i].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            getMotorDrive()[i].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            getMotorDrive()[i].setPower(0.0);
        }

        telemetry.addData(">", "Press START button to run the robot");
        telemetry.update();
    }

    @Override
    public void setDrivePower(double leftPower, double rightPower) {
        // Limits the drive power in specific range
        leftPower  = Range.clip(leftPower,  -getDriveSpeedLimit(), getDriveSpeedLimit());
        rightPower = Range.clip(rightPower, -getDriveSpeedLimit(), getDriveSpeedLimit());

        super.setDrivePower(leftPower, rightPower);
    }

    public double getDriveSpeedLimit() {
        return driveSpeedLimit;
    }

    public void setDriveSpeedLimit(double driveSpeedLimit) {
        this.driveSpeedLimit = driveSpeedLimit;
    }
}
