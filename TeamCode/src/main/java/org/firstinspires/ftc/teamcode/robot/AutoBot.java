package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * This class extends the DriveBot.class to implement autonomous features into the robot, so
 * the robot can drive and do tasks without the intervention of the driver.
 *
 * @author Henry
 */
public class AutoBot extends DriveBot {

    private static final double COUNTS_PER_REV = 1120;
    private static final double WHEEL_DIAMETER_MM = 31.2;
    private static final double COUNTS_PER_MM = (COUNTS_PER_REV) / (WHEEL_DIAMETER_MM * Math.PI);

    private ElapsedTime timer;
    private SensorBot sensors;

    @Override
    public void init(HardwareMap ahwMap, Telemetry atelemetry) {
        super.init(ahwMap, atelemetry);

        sensors = new SensorBot();
        timer = new ElapsedTime();

        // Reset the encoders and allow the drive motors to run with encoder
        setDriveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Initializes the sensors by fetching hardware configuration from robot controller
        sensors.init(hwMap);
    }

    /**
     * Converts the distance of millimeters to target position
     * @param distance
     * @return target position
     */
    public static int convertDistanceToPosition(double distance) {
        return (int)(distance * COUNTS_PER_MM);
    }

    /**
     * Moves the robot at specified distances for left motors and right motors
     * @param speed     Drive speed
     * @param leftDist  Left Distance
     * @param rightDist Right Distance
     * @param timeoutS  Timeout in seconds
     */
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

    /**
     * Turns the robot by angle
     * @param speed    Speed
     * @param angle    Turn angle
     * @param offset   Offset angle
     * @param timeoutS Timeout seconds
     */
    public void turnByGyro(double speed, int angle, int offset, double timeoutS) {
        int targetAngle = sensors.getGyro().getHeading() + angle;
        int angleToTarget;
        int currentHeading;
        boolean turnRight = (angle > 0);

        // When a gyro sensor's heading goes to 360, the heading loops back to zero. Since
        // the target angle can go over 360, the target angle would be sustracted by 180
        // to make up the heading looping back to zero from 180.
        if (targetAngle > 360) {
            targetAngle = targetAngle - 360;
        }

        timer.reset();
        do {
            // Get the angle distance to the target angle
            currentHeading = sensors.getGyro().getHeading();
            angleToTarget = currentHeading - targetAngle;

            // Right side of the target angle
            if (angleToTarget + offset > targetAngle) {
                if (turnRight) {
                    setDrivePower(speed, -speed);
                } else {
                    setDrivePower(-speed, speed);
                }
            }

            // :eft side of the target angle
            if (angleToTarget - offset < targetAngle) {
                if (turnRight) {
                    setDrivePower(-speed, speed);
                } else {
                    setDrivePower(speed, -speed);
                }
            }

            telemetry.addData("target", targetAngle);
            telemetry.addData("current", currentHeading);
            telemetry.addData("distance", angleToTarget);
            telemetry.addData("speed", speed);
            telemetry.update();
        } while (Math.abs(angleToTarget) < offset && timer.seconds() < timeoutS);

        // Stops the robot from turning
        setDrivePower(0, 0);
    }

    public SensorBot getSensors() {
        return sensors;
    }
}
