package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.opmodes.DriveOp;

/**
 * This class extends the DriveBot.class to implement autonomous features into the robot, so
 * the robot can drive and do tasks without the intervention of the driver.
 *
 * @author Henry
 * @version 1.0
 */
public class AutoBot extends DriveBot {

    // Values for drive motors with encoders
    private static final double COUNTS_PER_REV = 1120;
    private static final double WHEEL_DIAMETER_MM = 100;
    private static final double COUNTS_PER_MM = (COUNTS_PER_REV) / (WHEEL_DIAMETER_MM * Math.PI);

    // Values for lift motor with an encoder
    private static final double LIFT_WHEEL_DIAMETER_MM = 10;
    private static final double COUNTS_LIFT_PER_MM = (COUNTS_PER_REV) / (LIFT_WHEEL_DIAMETER_MM * Math.PI);

    private ElapsedTime timer = new ElapsedTime();
    private SensorBot sensors = new SensorBot();

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

    public void setDriveTargetPosition(double leftDist, double rightDist) {
        // Convert the distance values into target values
        int newLeftTarget = convertDistanceToPosition(leftDist);
        int newRightTarget = convertDistanceToPosition(rightDist);

        // Drive motors receive a new target position
        motorDriveLeftFront.setTargetPosition(motorDriveLeftFront.getCurrentPosition() + newLeftTarget);
        motorDriveLeftRear.setTargetPosition(motorDriveLeftRear.getCurrentPosition() + newLeftTarget);
        motorDriveRightFront.setTargetPosition(motorDriveRightFront.getCurrentPosition() + newRightTarget);
        motorDriveRightRear.setTargetPosition(motorDriveRightRear.getCurrentPosition() + newRightTarget);
    }

    /**
     * The robot travels at a specified distance using its drive motors' encoders.
     *
     * @param speed     Drive speed
     * @param leftDist  Left Distance
     * @param rightDist Right Distance
     * @param timeoutS  Timeout in seconds
     */
    public void moveByEncoder(double speed, double leftDist, double rightDist, double timeoutS) {
        // Set the direction of the drive motors so the robot can drive forward and backward
        // correctly
        setAutoDrive(AutoDrive.FORWARD);

        // Set the target position of the drive motors
        setDriveTargetPosition(leftDist, rightDist);

        // Turns on RUN_TO_TARGET mode on the drive motors
        setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Limits the speed between 0.0 and 1.0 since negative speed does not matter in
        // target position
        speed = Range.clip(Math.abs(speed), 0.0, 1.0);

        // Reset the timer for the timeout
        timer.reset();

        // The robot drives to the desired target position until the timer goes
        // pass the limit for a timeout or reaches the destination.
        while (isDriveMotorsBusy() && (timer.seconds() < timeoutS) && linearOpMode.opModeIsActive()) {
            setDrivePower(Math.abs(speed), Math.abs(speed));

            telemetry.addData("timeout", "%.2f", timeoutS - timer.seconds());
            telemetry.addData("current pos", "%07d %07d", motorDriveLeftFront.getCurrentPosition(), motorDriveRightFront.getCurrentPosition());
            telemetry.addData("target pos", "%07d %07d", motorDriveLeftFront.getTargetPosition(), motorDriveRightFront.getCurrentPosition());
            telemetry.update();
        }

        // Stop the drive motors
        setDrivePower(0, 0);

        // Turns off RUN_TO_TARGET mode
        setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /**
     * Moves the robot sideways using encoders
     * @param percent  Percent of sideways speed
     * @param distance Distance in millimeters
     * @param timeoutS Timeout in seconds
     */
    public void moveSidewaysByEncoder(double percent, double distance, double timeoutS) {
        percent = Range.clip(percent, 0, 1);
        setAutoDrive(AutoDrive.SIDEWAYS);
        setDriveTargetPosition(distance, distance);
        setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);

        timer.reset();
        while (isDriveMotorsBusy() && (timer.seconds() < timeoutS) && linearOpMode.opModeIsActive()) {
            motorDriveLeftFront.setPower(Math.abs(DriveOp.SIDEWAYS_LF_SPEED * percent));
            motorDriveLeftRear.setPower(Math.abs(DriveOp.SIDEWAYS_LR_SPEED * percent));
            motorDriveRightFront.setPower(Math.abs(DriveOp.SIDEWAYS_RF_SPEED * percent));
            motorDriveRightRear.setPower(Math.abs(DriveOp.SIDEWAYS_RR_SPEED * percent));

            telemetry.addData("timeout", "%.2f", timeoutS - timer.seconds());
            telemetry.addData("current pos", "%07d %07d", motorDriveLeftFront.getCurrentPosition(), motorDriveRightFront.getCurrentPosition());
            telemetry.addData("target pos", "%07d %07d", motorDriveLeftFront.getTargetPosition(), motorDriveRightFront.getCurrentPosition());
            telemetry.update();
        }

        // Stop the drive motors
        setDrivePower(0, 0);

        // Turns off RUN_TO_TARGET mode
        setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /**
     * Moves the lift by distance
     * @param speed    Lift Speed
     * @param distance Distance that the lift motor should move
     * @param timeoutS Timeout in seconds
     */
    public void moveLiftByDistance(double speed, double distance, double timeoutS) {
        int newTargetPosition = (int)(distance * COUNTS_LIFT_PER_MM);

        lift.getLiftMotor().setTargetPosition(newTargetPosition + lift.getLiftMotor().getCurrentPosition());
        lift.getLiftMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);

        lift.setLiftPower(Math.abs(speed));
        timer.reset();
        while (linearOpMode.opModeIsActive() && timer.seconds() < timeoutS && lift.getLiftMotor().isBusy()) {
            telemetry.addData("status", "moving the lift");
            telemetry.addData("speed", "%.2f", lift.getLiftPower());
            telemetry.addData("target", "%07d", lift.getLiftMotor().getTargetPosition());
            telemetry.addData("current", "%07d", lift.getLiftMotor().getCurrentPosition());
            telemetry.update();
        }
        lift.setLiftPower(0);
        lift.getLiftMotor().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /**
     * Turns the robot by angle
     * @param speed    Speed
     * @param angle    Turn angle
     * @param offset   Offset angle
     * @param timeoutS Timeout seconds
     */
    public void turnByGyro(double speed, int angle, int offset, double timeoutS) {
        int targetAngle = (int)sensors.getAngle() + angle;
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
            currentHeading = (int)sensors.getAngle();
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
     * Set the direction of the drive motors using modes to determine whether the robot moves
     * forward, backwards, or sideways.
     *
     * @param autoDrive Auto Drive Mode
     */
    public void setAutoDrive(AutoDrive autoDrive) {
        switch (autoDrive) {
            case FORWARD:
                motorDriveLeftFront.setDirection(DcMotorSimple.Direction.REVERSE);
                motorDriveLeftRear.setDirection(DcMotorSimple.Direction.REVERSE);
                motorDriveRightFront.setDirection(DcMotorSimple.Direction.FORWARD);
                motorDriveRightRear.setDirection(DcMotorSimple.Direction.FORWARD);
                break;
            case BACKWARD:
                motorDriveLeftFront.setDirection(DcMotorSimple.Direction.FORWARD);
                motorDriveLeftRear.setDirection(DcMotorSimple.Direction.FORWARD);
                motorDriveRightFront.setDirection(DcMotorSimple.Direction.REVERSE);
                motorDriveRightRear.setDirection(DcMotorSimple.Direction.REVERSE);
                break;
            case SIDEWAYS:
                motorDriveLeftFront.setDirection(DcMotorSimple.Direction.REVERSE);
                motorDriveLeftRear.setDirection(DcMotorSimple.Direction.FORWARD);
                motorDriveRightFront.setDirection(DcMotorSimple.Direction.REVERSE);
                motorDriveRightRear.setDirection(DcMotorSimple.Direction.FORWARD);
                break;
        }
    }

    /**
     * Returns the sensors
     * @return Sensors
     */
    public SensorBot getSensors() {
        return sensors;
    }
}
