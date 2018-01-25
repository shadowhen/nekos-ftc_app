package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class DriveBot extends Bot {

    public DriveBot(){
        super();
    }

    @Override
    public void init(HardwareMap ahwMap, Telemetry _telemetry){
        super.init(ahwMap, _telemetry);

        // Loops through the drive motors and set them to run without encoders
        for (DcMotor driveMotor : driveMotors){
            driveMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            driveMotor.setPower(0);
        }
    }

    /**
     * Get the drive power of a particular motor
     *
     * @param driveMotor Number of the index in the driveMotors array
     * @return Power of Drive Motor
     */
    public double getDrivePower(int driveMotor){
        if (driveMotors[driveMotor] != null){
            return driveMotors[driveMotor].getPower();
        } else {
            return -2; // If you see -2 on driver station, the number in the index does not exist
        }
    }

    /**
     * Get the power of the arm that moves the flaps up and down
     * @return Power of the flapper arm
     */
    public double getArmPower(){
        return flapperArm.getPower();
    }

    /**
     * Get the servo positiion that opens or closes the flaps
     *
     * @param flap Number of the index with servos (flaps)
     * @return Servo position of a particular flap
     */
    public double getFlapPosition(int flap){
        if (flaps[flap] != null) {
            return flaps[flap].getPosition();
        } else {
            return -2; // If you see -2 on driver station, the number in the index does not exist
        }
    }
}
