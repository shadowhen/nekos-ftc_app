package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * This class provides as the framework for the robot which can be used for any purposes
 * @author Henry
 * @version 1.1
 */
public class DriveBot implements Bot {

    protected HardwareMap hwMap;
    protected Telemetry telemetry;

    // Drive motors
    protected DcMotor motorDriveLeftFront;
    protected DcMotor motorDriveLeftRear;
    protected DcMotor motorDriveRightFront;
    protected DcMotor motorDriveRightRear;

    // External functions
    protected Lift lift;
    protected Sweeper sweeper;
    protected Dumper dumper;

    /**
     * Initializes the robot
     * @param ahwMap      Hardware Map
     * @param atelemetry  Telemetry
     */
    public void init(HardwareMap ahwMap, Telemetry atelemetry) {
        hwMap = ahwMap;
        telemetry = atelemetry;

        lift = new Lift();
        sweeper = new Sweeper();
        dumper = new Dumper();

        // Get hardware references from the robot controller's configuration for hardware devices
        motorDriveLeftFront  = hwMap.get(DcMotor.class, HardwareName.MOTOR_DRIVE_LEFT_FRONT);
        motorDriveLeftRear   = hwMap.get(DcMotor.class, HardwareName.MOTOR_DRIVE_LEFT_REAR);
        motorDriveRightFront = hwMap.get(DcMotor.class, HardwareName.MOTOR_DRIVE_RIGHT_FRONT);
        motorDriveRightRear  = hwMap.get(DcMotor.class, HardwareName.MOTOR_DRIVE_RIGHT_REAR);

        // Reverses the right drive motors' direction
        motorDriveRightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        motorDriveRightRear.setDirection(DcMotorSimple.Direction.REVERSE);

        // Because the drive motors affect the motion of the meachum wheels, the
        // zero power behavior would be in BRAKE.
        motorDriveLeftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorDriveLeftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorDriveRightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorDriveRightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Initializes other hardware on the robot
        lift.init(hwMap);
        sweeper.init(hwMap);
        dumper.init(hwMap);
    }

    /**
     * Set the run mode of the drive motors
     * @param runMode Run Mode
     */
    public void setDriveMode(DcMotor.RunMode runMode) {
        motorDriveLeftFront.setMode(runMode);
        motorDriveLeftRear.setMode(runMode);
        motorDriveRightFront.setMode(runMode);
        motorDriveRightRear.setMode(runMode);
    }

    public void setDrivePower(double leftFrontPower, double leftRearPower, double rightFrontPower, double rightRearPower) {
        motorDriveLeftFront.setPower(leftFrontPower);
        motorDriveLeftRear.setPower(leftRearPower);
        motorDriveRightFront.setPower(rightFrontPower);
        motorDriveRightRear.setPower(rightRearPower);
    }

    /**
     * Set the drive power of the left and right motors.
     * @param leftPower  Left Power
     * @param rightPower Right Power
     */
    public void setDrivePower(double leftPower, double rightPower) {
        setDrivePower(leftPower, leftPower, rightPower, rightPower);
    }

    /**
     * Set the drive power using an array
     * @param drivePower Drive Power Array
     */
    public void setDrivePower(double[] drivePower) {
        if (drivePower.length >= 4) {
            setDrivePower(drivePower[0], drivePower[1], drivePower[2], drivePower[3]);
        }
    }

    public void setDrivePowerSideways(double leftFrontPower, double leftRearPower, double rightFrontPower, double rightRearPower) {
        setDrivePower(leftFrontPower, -leftRearPower, -rightFrontPower, rightRearPower);
    }

    /**
     * Set the drive power of the left and right motors which the robot drives sideways.
     * @param leftPower  Left Power
     * @param rightPower Right Power
     */
    public void setDrivePowerSideways(double leftPower, double rightPower) {
        setDrivePowerSideways(leftPower, leftPower, rightPower, rightPower);
    }

    /**
     * Set the drive power to sideways move the robot
     * @param power Drive Power
     */
    public void setDrivePowerSideways(double power) {
        setDrivePowerSideways(power, power);
    }

    public void setDrivePowerSideways(double[] power) {
        if (power.length >= 4) {
            setDrivePowerSideways(power[0], power[1], power[2], power[3]);
        } else if (power.length >= 2) {
            setDrivePowerSideways(power[0], power[1]);
        } else if (power.length >= 1) {
            setDrivePowerSideways(power[0]);
        } else {
            setDrivePowerSideways(0);
        }

    }

    /**
     * Set the drive power to turn the robot
     * @param leftPower  Left Power
     * @param rightPower Right Power
     */
    public void setDrivePowerTurn(double leftPower, double rightPower) {
        setDrivePower(-leftPower, -leftPower, rightPower, rightPower);
    }

    /**
     * Set the drive power to turn the robot
     * @param power Drive Power
     */
    public void setDrivePowerTurn(double power) {
        setDrivePowerTurn(power, power);
    }

    /**
     * Set the drive motors' zero power behavior
     * @param behavior Zero Power Behavior
     */
    public void setDriveZeroPowerBehavior(DcMotor.ZeroPowerBehavior behavior) {
        motorDriveLeftFront.setZeroPowerBehavior(behavior);
        motorDriveLeftRear.setZeroPowerBehavior(behavior);
        motorDriveRightFront.setZeroPowerBehavior(behavior);
        motorDriveRightRear.setZeroPowerBehavior(behavior);
    }

    public double[] getDrivePower() {
        return new double[]{ motorDriveLeftFront.getPower(), motorDriveLeftRear.getPower(),
                motorDriveRightFront.getPower(), motorDriveRightRear.getPower()};
    }

    /**
     * Returns if all of the drive motors are busy
     * @return All drive motors busy
     */
    public boolean isDriveMotorsBusy() {
        return (motorDriveLeftFront.isBusy() && motorDriveLeftRear.isBusy()
                && motorDriveRightFront.isBusy() && motorDriveRightRear.isBusy());
    }

    /**
     * Returns the lift
     * @return Lift
     */
    public Lift getLift() {
        return lift;
    }

    /**
     * Returns the sweeper
     * @return Sweeper
     */
    public Sweeper getSweeper() {
        return sweeper;
    }

    /**
     * Returns the dumper
     * @return Dumper
     */
    public Dumper getDumper() {
        return dumper;
    }

    /**
     * Returns the telemetry
     * @return Telemetry
     */
    public Telemetry getTelemetry() {
        return telemetry;
    }
}
