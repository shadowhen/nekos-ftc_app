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
        setAutoDrive(AutoDrive.FORWARD);
        setDriveTargetPosition(leftDist, rightDist);

        // Turns on RUN_TO_TARGET mode on the drive motors
        setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);

        speed = Range.clip(Math.abs(speed), 0.0, 1.0);
        timer.reset();

        // The robot drives to the desired target position
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
     * Moves the lift up or down using speed and time
     * @param speed Lfit Speed
     * @param time  Time that the lift should move
     */
    public void moveLiftByTime(double speed, double time) {
        lift.setLiftPower(speed);
        timer.reset();
        while (linearOpMode.opModeIsActive() && timer.seconds() < time) {
            telemetry.addData("status", "moving the lift");
            telemetry.addData("speed", "%.2f", lift.getLiftPower());
            telemetry.addData("current", "%07d", lift.getLiftMotor().getCurrentPosition());
            telemetry.update();
        }
        lift.setLiftPower(0);
    }

    /**
     * Moves the lift by encoder counts
     * @param speed    Lift Speed
     * @param counts   Lift Encoder Counts
     * @param timeoutS Timeout in seconds
     */
    public void moveLiftByCounts(double speed, int counts, double timeoutS) {
        lift.getLiftMotor().setTargetPosition(counts + lift.getLiftMotor().getTargetPosition());
        lift.getLiftMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);

        lift.setLiftPower(Math.abs(speed));
        timer.reset();
        while (linearOpMode.opModeIsActive() && timer.seconds() < timeoutS && lift.getLiftMotor().isBusy()) {
            telemetry.addData("status", "moving the lift");
            telemetry.addData("speed", "%.2f", lift.getLiftPower());
            telemetry.addData("target", "%07d", counts);
            telemetry.addData("current", "%07d", lift.getLiftMotor().getCurrentPosition());
            telemetry.update();
        }
        lift.setLiftPower(0);
        lift.getLiftMotor().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /**
     * Moves the lift by distance
     * @param speed    Lift Speed
     * @param distance Distance that the lift motor should move
     * @param timeoutS Timeout in seconds
     */
    public void moveLiftByDistance(double speed, double distance, double timeoutS) {
        lift.getLiftMotor().setTargetPosition((int)(distance * COUNTS_LIFT_PER_MM) + lift.getLiftMotor().getCurrentPosition());
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
     * Very similar to moveByEncoder method. This method utilizes the gyro sensor to keep the robot
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

    public void moveLift(double speed, double distance, double timeoutS) {
        int newTarget = Lift.convertDistanceToTarget(distance);
        lift.getLanderMotor().setTargetPosition(lift.getLanderMotor().getCurrentPosition() + newTarget);
        lift.getLanderMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);

        timer.reset();
        while (linearOpMode.opModeIsActive() && timer.seconds() < timeoutS) {
            lift.setLanderPower(Math.abs(speed));

            telemetry.addData("current", lift.getLanderMotor().getCurrentPosition());
            telemetry.addData("target", lift.getLanderMotor().getTargetPosition());
            telemetry.addData("timeout", "%.2f", timeoutS - timer.seconds());
            telemetry.update();
        }

        lift.setLiftPower(0.0);
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
     * Returns boolean if the robot is on the current heading.
     * @param speed  Speed
     * @param angle  Angle
     * @param pCoeff Proportional Coefficient
     * @return boolean
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
