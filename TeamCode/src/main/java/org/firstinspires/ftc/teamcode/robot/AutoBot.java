package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * This class extends to the DriveBot class to implement autonomous methods such as encoders,
 * target position, and other methods.
 */
public class AutoBot extends DriveBot{

    private static final double COUNTS_PER_MOTOR_REV = 1120;
    private static final double WHEEL_DIAMETER_MM = 3.40;
    private static final double COUNTS_PER_MM = COUNTS_PER_MOTOR_REV / (WHEEL_DIAMETER_MM * Math.PI);

    public AutoBot() {
        super();
    }

    @Override
    public void init(HardwareMap ahwMap, Telemetry atelemetry) {
        super.init(ahwMap, atelemetry);

        resetEncoders(motorLeftDrive);
        resetEncoders(motorRightDrive);
    }

    /**
     * Converts the distance to target position
     * @param distance Distance
     * @return Target position
     */
    public static int convertDistanceToTargetPosition(double distance) {
        return (int)(COUNTS_PER_MM * Math.round(distance));
    }

    /**
     * Moves the robot using distance and drive motors's encoders
     * @param speed     Drive motor speed
     * @param leftDist  Left distance
     * @param rightDist RIght distance
     * @param timeoutS  Timeout
     */
    public void moveByEncoder(double speed, double leftDist, double rightDist, double timeoutS) {
        moveByEncoder(speed, leftDist, rightDist, timeoutS, 1);
    }

    /**
     * Moves the robot using distance and drive motors' encoders
     * @param speed     Drive motor speed
     * @param leftDist  Left distance
     * @param rightDist Right distance
     * @param timeoutS  Timeout
     * @param sleepS    Sleep
     */
    public void moveByEncoder(double speed, double leftDist, double rightDist, double timeoutS, double sleepS) {
        int newLeftTarget = convertDistanceToTargetPosition(leftDist);
        int newRightTarget = convertDistanceToTargetPosition(rightDist);
        boolean reachedTarget = false;

        // Enable RUN_TO_TARGET mode and set the target position of the drive motors
        for (DcMotor motor : motorLeftDrive) {
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setTargetPosition(newLeftTarget + motor.getCurrentPosition());
        }

        for (DcMotor motor : motorRightDrive) {
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setTargetPosition(newRightTarget + motor.getCurrentPosition());
        }

        // Starts the drive motors
        setDrivePower(Math.abs(speed), Math.abs(speed));

        // Allows the robot to timeout properly
        timeoutS += runtime.seconds();

        // While the robot has not reached its target nor timeout has occured, the robot keeps
        // moving until timeout happens or one of the drive motors reach its target.
        while (runtime.seconds() < timeoutS && !reachedTarget) {

            // The robot stops its motors once it reaches its target
            if (AutoBot.isMotorsBusy(motorLeftDrive) && AutoBot.isMotorsBusy(motorRightDrive)) {
                telemetry.addData(">", "Moving by encoders");
                telemetry.addData("Timeout", "%.2f", timeoutS - runtime.seconds());
                telemetry.update();
            } else {
                reachedTarget = true;
            }
        }

        // Zeros the drive motors' power
        setDrivePower(0.0, 0.0);

        // Turns off RUN_TO_POSITION mode
        for (DcMotor motor : motorLeftDrive) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        for (DcMotor motor : motorRightDrive) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        // The robot sleeps or "wait"
        waitTime(sleepS);
    }

    /**
     * Resets the encoders of a given motor array
     * @param motors DcMotors
     */
    public static void resetEncoders(DcMotor[] motors) {
        for (DcMotor motor : motors) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    public static boolean isMotorsBusy(DcMotor[] motors) {
        for (DcMotor motor : motors) {
            if (!motor.isBusy()) {
                return false;
            }
        }

        return true;
    }
}
