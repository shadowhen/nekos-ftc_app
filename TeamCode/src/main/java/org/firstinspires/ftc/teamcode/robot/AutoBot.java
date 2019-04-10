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
 * @version 1.1
 */
public class AutoBot extends DriveBot {

    // Values for drive motors with encoders
    private static final double COUNTS_PER_REV = 1120;
    private static final double WHEEL_DIAMETER_MM = 100;
    private static final double COUNTS_PER_MM = (COUNTS_PER_REV) / (WHEEL_DIAMETER_MM * Math.PI);

    // Values for lift motor with an encoder
    private static final double LIFT_WHEEL_DIAMETER_MM = 10;
    private static final double COUNTS_LIFT_PER_MM = (COUNTS_PER_REV) / (LIFT_WHEEL_DIAMETER_MM * Math.PI);

    private ElapsedTime timer;
    private SensorBot sensors;

    // Used for accessing methods in the linearOpMode
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

        telemetry.addData("status", "setting IMU up...");
        telemetry.update();

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
     * Set a new target position by converting distance into encoder counts and then adding the
     * counts to the current target position and set it as the new target position.
     * @param leftDist  Left distance in millimeters
     * @param rightDist Right distance in millimeters
     */
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
     * The robot moves forward or backward according to the given distance within certain timeout
     * using the speed for its drive motors.
     * @param speed    Speed by percentage
     * @param distance Distance to travel forward/backward
     * @param timeoutS Timeout in seconds
     */
    public void moveByEncoder(double speed, double distance, double timeoutS) {
        moveByEncoder(speed, distance, distance, timeoutS);
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
        double correction;
        double leftSpeed;
        double rightSpeed;
        double max;

        if (linearOpMode.opModeIsActive()) {
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

            // Reset the gyro sensor's heading to zero for correcting direction
            sensors.resetAngle();

            // Reset the timer for the timeout
            timer.reset();

            // The robot drives to the desired target position until the timer goes
            // pass the limit for a timeout or reaches the destination.
            while (isDriveMotorsBusy() && (timer.seconds() < timeoutS) && linearOpMode.opModeIsActive()) {
                // TODO: Add way to correct the direction of the robot if the robot goes slightly off
                /*correction = sensors.getCorrection(.10, 0);
                leftSpeed = speed - correction;
                rightSpeed = speed + correction;
                max = Math.max(Math.abs(leftSpeed), Math.abs(rightSpeed));
                if (max > 1.0) {
                    leftSpeed /= max;
                    rightSpeed /= max;
                }
                setDrivePower(Math.abs(leftSpeed), Math.abs(rightSpeed));*/

                setDrivePower(speed, speed);

                telemetry.addData("timeout", "%.2f", timeoutS - timer.seconds());
                telemetry.addData("current pos", "%07d %07d", motorDriveLeftFront.getCurrentPosition(), motorDriveRightFront.getCurrentPosition());
                telemetry.addData("target pos", "%07d %07d", motorDriveLeftFront.getTargetPosition(), motorDriveRightFront.getCurrentPosition());
                telemetry.update();
            }

            // Stop the drive motors
            setDrivePower(0, 0);

            // Turns off RUN_TO_TARGET mode
            setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);

            // Reset the angle so the robot can use the gyro sensor for other purposes
            sensors.resetAngle();
        }
    }

    /**
     * Moves the robot sideways using encoders
     * @param speed   Sideways speed
     * @param distance Distance in millimeters
     * @param timeoutS Timeout in seconds
     */
    public void moveSidewaysByEncoder(double speed, double distance, double timeoutS) {

        if (linearOpMode.opModeIsActive()) {
            speed = Math.abs(Range.clip(speed, 0, 1));
            setAutoDrive(AutoDrive.SIDEWAYS);
            setDriveTargetPosition(distance, distance);
            setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);

            timer.reset();
            while (isDriveMotorsBusy() && (timer.seconds() < timeoutS) && linearOpMode.opModeIsActive()) {
                motorDriveLeftFront.setPower(speed);
                motorDriveLeftRear.setPower(speed);
                motorDriveRightFront.setPower(speed);
                motorDriveRightRear.setPower(speed);

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

    }

    public void turnByEncoder(double speed, double turnDist, double timeoutS) {
        moveByEncoder(speed, turnDist, -turnDist, timeoutS);
    }

    /**
     * Moves the lift by distance
     * @param speed    Lift Speed
     * @param distance Distance in millimeters
     * @param timeoutS Timeout in seconds
     */
    public void moveLiftByDistance(double speed, double distance, double timeoutS) {
        int newTargetPosition = (int)(distance * COUNTS_LIFT_PER_MM);

        if (linearOpMode.opModeIsActive()) {
            lift.getLiftMotor().setTargetPosition(newTargetPosition + lift.getCurrentPosition());
            lift.getLiftMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);

            lift.setLiftPower(Math.abs(speed));
            timer.reset();
            while (linearOpMode.opModeIsActive() && timer.seconds() < timeoutS && lift.getLiftMotor().isBusy()) {
                telemetry.addData("status", "moving the lift");
                telemetry.addData("speed", "%.2f", lift.getLiftPower());
                telemetry.addData("target", "%07d", lift.getCurrentPosition());
                telemetry.addData("current", "%07d", lift.getCurrentPosition());
                telemetry.update();
            }
            lift.setLiftPower(0);
            lift.getLiftMotor().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    /**
     * Turns the robot by degrees
     * @param speed    Speed
     * @param degrees  Turn in degrees
     * @param timeoutS Timeout in seconds
     */
    public void turnByGyro(double speed, int degrees, double timeoutS) {
        double leftPower, rightPower;

        if (linearOpMode.opModeIsActive() && degrees != 0) {
            // Reset the degrees first before turning
            sensors.resetAngle();

            speed = Math.abs(speed);
            if (degrees > 0) {
                leftPower = speed;
                rightPower = -speed;
            } else {
                leftPower = -speed;
                rightPower = speed;
            }

            // Reset the timer
            timer.reset();

            // Run the motors
            setDrivePower(leftPower, rightPower);

            // The robot turns until it has reached its desired degree
            if (degrees > 0) {
                while (linearOpMode.opModeIsActive() &&
                        ((sensors.getAngle() == 0) || (sensors.getAngle() < degrees)) &&
                        timer.seconds() < timeoutS) {
                    telemetry.addData("status", "turning left");
                    telemetry.addData("desired degrees", degrees);
                    telemetry.addData("time", "%.2f", timeoutS - timer.seconds());
                    telemetry.update();
                }
            } else {
                while (linearOpMode.opModeIsActive() &&
                        ((sensors.getAngle() == 0) || (sensors.getAngle() > degrees))
                        && timer.seconds() < timeoutS) {
                    telemetry.addData("status", "turning right");
                    telemetry.addData("desired degrees", degrees);
                    telemetry.addData("time", "%.2f", timeoutS - timer.seconds());
                    telemetry.update();
                }
            }

            // Stop the robot
            setDrivePower(0, 0);

            // Reset the angle so the robot can use the gyro sensor without any problems such
            // direction correction.
            sensors.resetAngle();
        }
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
