package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * This class extends the DriveBot.class to implement autonomous features into the robot, so
 * the robot can drive and do tasks without the intervention of the driver.
 *
 * @author Henry
 * @version 1.0
 */
public class AutoBot extends DriveBot {

    private static final double COUNTS_PER_REV = 1120;
    private static final double WHEEL_DIAMETER_MM = 100;
    private static final double COUNTS_PER_MM = (COUNTS_PER_REV) / (WHEEL_DIAMETER_MM * Math.PI);

    private ElapsedTime timer;
    private SensorBot sensors;

    private LinearOpMode linearOpMode;

    public AutoBot() {
        this(null);
    }

    public AutoBot(LinearOpMode linearOpMode) {
        this.linearOpMode = linearOpMode;
    }

    @Override
    public void init(HardwareMap ahwMap, Telemetry atelemetry) {
        super.init(ahwMap, atelemetry);

        sensors = new SensorBot();
        timer = new ElapsedTime();

        // Reset the encoders and allow the drive motors to run with encoders
        setDriveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Initializes the sensors by fetching hardware configuration from robot controller
        sensors.init(hwMap);
    }

    /**
     * Converts the distance of millimeters to target position
     *
     * @param distance Distance in millimeters
     * @return Position Counts
     */
    public static int convertDistanceToPosition(double distance) {
        return (int)(distance * COUNTS_PER_MM);
    }

    /**
     * The robot travels at a specified distance using its drive motors' encoders, and
     *
     * @param speed     Drive speed
     * @param leftDist  Left Distance
     * @param rightDist Right Distance
     * @param timeoutS  Timeout in seconds
     */
    public void moveByEncoder(double speed, double leftDist, double rightDist, double timeoutS) {
        // Convert the distance values into target values
        int newLeftTarget = convertDistanceToPosition(leftDist);
        int newRightTarget = convertDistanceToPosition(rightDist);

        // Drive motors receive a new target position
        motorDriveLeftFront.setTargetPosition(motorDriveLeftFront.getCurrentPosition() + newLeftTarget);
        motorDriveLeftRear.setTargetPosition(motorDriveLeftRear.getCurrentPosition() + newLeftTarget);
        motorDriveRightFront.setTargetPosition(motorDriveRightFront.getCurrentPosition() + newRightTarget);
        motorDriveRightRear.setTargetPosition(motorDriveRightRear.getCurrentPosition() + newRightTarget);

        // Turns on RUN_TO_TARGET mode on the drive motors
        setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);

        speed = Range.clip(Math.abs(speed), 0.0, 1.0);
        timer.reset();

        // The robot drives to the desired target position
        while (isDriveMotorsBusy() && (timer.seconds() < timeoutS) && linearOpMode.opModeIsActive()) {
            setDrivePower(Math.abs(speed), Math.abs(speed));

            telemetry.addData("current pos", "%d %d", motorDriveLeftFront.getCurrentPosition(), motorDriveRightFront.getCurrentPosition());
            telemetry.addData("target pos", "%d %d", newLeftTarget, newRightTarget);
            telemetry.update();
        }

        // Stop the drive motors
        setDrivePower(0, 0);

        // Turns off RUN_TO_TARGET mode
        setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /**
     * Very similar to moveByEncoder method. This method utilzes the gyro sensor to keep the robot
     * from veering off. For an example, the robot is set to 0.0 degrees, and while the robot
     * travels to its destination by distance, the robot adjusts its current heading to match
     * specified angle using proportional coefficient to determine the left and right motors'
     * speed.
     * .
     * @param speed    Speed
     * @param distance Distance
     * @param angle    Angle
     * @param pCoeff   Proportional Coefficient
     */
    public void moveByGyroDrive(double speed, double distance, double angle, double pCoeff) {
        double error;
        double steer;
        double max;
        double leftSpeed;
        double rightSpeed;
        int moveCount = convertDistanceToPosition(distance);

        motorDriveLeftFront.setTargetPosition(motorDriveLeftFront.getCurrentPosition() + moveCount);
        motorDriveLeftRear.setTargetPosition(motorDriveLeftRear.getCurrentPosition() + moveCount);
        motorDriveRightFront.setTargetPosition(motorDriveRightFront.getCurrentPosition() + moveCount);
        motorDriveRightRear.setTargetPosition(motorDriveRightRear.getCurrentPosition() + moveCount);

        setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);
        speed = Range.clip(speed, 0.0, 1.0);
        setDrivePower(speed, speed);
        while (isDriveMotorsBusy() && linearOpMode.opModeIsActive()) {
            error = sensors.getError(angle);
            steer = sensors.getSteer(error, pCoeff);

            if (distance < 0.0) {
                steer *= -1;
            }
            leftSpeed = speed - steer;
            rightSpeed = speed + steer;

            max = Math.max(Math.abs(leftSpeed), Math.abs(rightSpeed));
            if (max > 1.0) {
                leftSpeed /= max;
                rightSpeed /= max;
            }

            setDrivePower(leftSpeed, rightSpeed);
        }

        setDrivePower(0,0);
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

            // Left side of the target angle
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

    /**
     *
     * @param speed
     * @param angle
     * @param pCoeff
     * @return
     */
    public boolean onHeading(double speed, double angle, double pCoeff) {
        double error;
        double steer;
        boolean onTarget = false;
        double leftSpeed;
        double rightSpeed;

        error = getSensors().getError(angle);
        if (Math.abs(error) < pCoeff) {
            leftSpeed = 0.0;
            rightSpeed = 0.0;
            onTarget = true;
        } else {
            steer = getSensors().getSteer(error, pCoeff);
            leftSpeed = speed * steer;
            rightSpeed = -leftSpeed;
        }

        setDrivePower(leftSpeed, rightSpeed);

        return onTarget;
    }

    /**
     * Set the direction of the drive motors using modes to determine whether the robot moves
     * forward, backwards, or sideways.
     *
     * @param autoDrive Auto Drive Mode
     */
    public void setAutoDrive(AutoDrive autoDrive) {
        switch (autoDrive) {
            case FORWARD:
                motorDriveLeftFront.setDirection(DcMotorSimple.Direction.FORWARD);
                motorDriveLeftRear.setDirection(DcMotorSimple.Direction.FORWARD);
                motorDriveRightFront.setDirection(DcMotorSimple.Direction.FORWARD);
                motorDriveRightRear.setDirection(DcMotorSimple.Direction.FORWARD);
                break;
            case BACKWARD:
                motorDriveLeftFront.setDirection(DcMotorSimple.Direction.REVERSE);
                motorDriveLeftRear.setDirection(DcMotorSimple.Direction.REVERSE);
                motorDriveRightFront.setDirection(DcMotorSimple.Direction.REVERSE);
                motorDriveRightRear.setDirection(DcMotorSimple.Direction.REVERSE);
                break;
            case SIDEWAYS_LEFT:
                motorDriveLeftFront.setDirection(DcMotorSimple.Direction.FORWARD);
                motorDriveLeftRear.setDirection(DcMotorSimple.Direction.REVERSE);
                motorDriveRightFront.setDirection(DcMotorSimple.Direction.REVERSE);
                motorDriveRightRear.setDirection(DcMotorSimple.Direction.FORWARD);
                break;
            case SIDEWAYS_RIGHT:
                motorDriveLeftFront.setDirection(DcMotorSimple.Direction.REVERSE);
                motorDriveLeftRear.setDirection(DcMotorSimple.Direction.FORWARD);
                motorDriveRightFront.setDirection(DcMotorSimple.Direction.FORWARD);
                motorDriveRightRear.setDirection(DcMotorSimple.Direction.REVERSE);
                break;
        }
    }

    public SensorBot getSensors() {
        return sensors;
    }
}
