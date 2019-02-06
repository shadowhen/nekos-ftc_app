package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * This class provides as the framework for the robot which can be used for any purposes
 * @author Henry
 * @version 1.0
 */
public class DriveBot implements Bot {

    public static final double DUMPER_SERVO_CLOSE_POSITION = 1.0;

    protected HardwareMap hwMap;
    protected Telemetry telemetry;

    // Drive motors
    protected DcMotor motorDriveLeftFront;
    protected DcMotor motorDriveLeftRear;
    protected DcMotor motorDriveRightFront;
    protected DcMotor motorDriveRightRear;

    // External functions
    protected Lift lift = new Lift();
    protected Sweeper sweeper = new Sweeper();
    protected Dumper dumper = new Dumper();

    /**
     * Initializes the robot
     * @param ahwMap      Hardware Map
     * @param atelemetry  Telemetry
     */
    public void init(HardwareMap ahwMap, Telemetry atelemetry) {
        hwMap = ahwMap;
        telemetry = atelemetry;

        // Get hardware references from the robot controller's configuration for hardware devices
        motorDriveLeftFront  = hwMap.get(DcMotor.class, "motor_drive_lf");
        motorDriveLeftRear   = hwMap.get(DcMotor.class, "motor_drive_lr");
        motorDriveRightFront = hwMap.get(DcMotor.class, "motor_drive_rf");
        motorDriveRightRear  = hwMap.get(DcMotor.class, "motor_drive_rr");

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

        // Reset the position of the dumper
        dumper.setPosition(DUMPER_SERVO_CLOSE_POSITION);
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

    /**
     * Set the drive power of the left and right motors.
     * @param leftPower  Left Power
     * @param rightPower Right Power
     */
    public void setDrivePower(double leftPower, double rightPower) {
        motorDriveLeftFront.setPower(leftPower);
        motorDriveLeftRear.setPower(leftPower);
        motorDriveRightFront.setPower(rightPower);
        motorDriveRightRear.setPower(rightPower);
    }

    /**
     * Set the drive power of the left and right motors which the robot drives sideways.
     * @param leftPower  Left Power
     * @param rightPower Right Power
     */
    public void setDrivePowerSideways(double leftPower, double rightPower) {
        motorDriveLeftFront.setPower(leftPower);
        motorDriveLeftRear.setPower(-leftPower);
        motorDriveRightFront.setPower(-rightPower);
        motorDriveRightRear.setPower(rightPower);
    }

    /**
     * Set the drive power to turn the robot
     * @param leftPower  Left Power
     * @param rightPower Right Power
     */
    public void setDrivePowerTurn(double leftPower, double rightPower) {
        motorDriveLeftFront.setPower(-leftPower);
        motorDriveLeftRear.setPower(-leftPower);
        motorDriveRightFront.setPower(rightPower);
        motorDriveRightRear.setPower(rightPower);
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

    /**
     * Set the power of the lift
     * @param power Lift Power
     */
    public void setLiftPower(double power) {
        lift.setLiftPower(power);
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
     * Returns the hardware map
     * @return Hardware Map
     */
    public HardwareMap getHardwareMap() {
        return hwMap;
    }

    /**
     * Returns the telemetry
     * @return Telemetry
     */
    public Telemetry getTelemetry() {
        return telemetry;
    }

    /**
     * Returns the front left drive motor
     * @return Front left drive motor
     */
    public DcMotor getMotorDriveLeftFront() {
        return motorDriveLeftFront;
    }

    /**
     * Returns the rear left drive motor
     * @return Rear left drive motor
     */
    public DcMotor getMotorDriveLeftRear() {
        return motorDriveLeftRear;
    }

    /**
     * Returns the front right drive motor
     * @return Front right drive motor
     */
    public DcMotor getMotorDriveRightFront() {
        return motorDriveRightFront;
    }

    /**
     * Returns the rear right drive motor
     * @return Rear right drive motor
     */
    public DcMotor getMotorDriveRightRear() {
        return motorDriveRightRear;
    }
}
