package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public abstract class Bot {

    protected HardwareMap hwMap;   // Robot's hardware map
    protected Telemetry telemetry; // Robot's central telemetry

    // Drive system
    protected DcMotor[] driveMotors; // Four motors

    // Paddles (Flaps)
    protected DcMotor flapperArm;
    protected Servo[] flaps;

    // Jewel system
    protected DcMotor jewelSlider;
    protected CRServo jewelArm;         // Note: one motor and one servo!!!
    protected Servo   jewelElbow;

    protected static final double MID_SERVO = 0.5;

    protected Bot(){
        driveMotors = new DcMotor[4];
        flaps = new Servo[2];
    }

    /**
     * Initializes the robot and sets up essential hardware that the user relies on such
     * as movement, arm control, and flap control
     *
     * @param ahwMap     Hardware Map
     * @param _telemetry Telemetry
     */
    public void init(HardwareMap ahwMap, Telemetry _telemetry){
        hwMap     = ahwMap;
        telemetry = _telemetry;

        // Settings of telemetry's log system
        telemetry.log().setDisplayOrder(Telemetry.Log.DisplayOrder.OLDEST_FIRST);
        telemetry.log().setCapacity(6);

        // Get hardware references from the robot controller for the robot
        // Motors:
        driveMotors[0] = hwMap.get(DcMotor.class, "motorleftfront");
        driveMotors[1] = hwMap.get(DcMotor.class, "motorrightfront");
        driveMotors[2] = hwMap.get(DcMotor.class, "motorleftrear");
        driveMotors[3] = hwMap.get(DcMotor.class, "motorrightrear");

        // Paddle System (Flap System):
        flapperArm = hwMap.get(DcMotor.class, "motorarm");
        flaps[0]   = hwMap.get(Servo.class, "servoleftclap");
        flaps[1]   = hwMap.get(Servo.class, "servorightclap");

        // Reverses direction of the right drive motors
        driveMotors[1].setDirection(DcMotorSimple.Direction.REVERSE);
        driveMotors[3].setDirection(DcMotorSimple.Direction.REVERSE);

        // Forwards direction of the left drive motors
        driveMotors[0].setDirection(DcMotorSimple.Direction.FORWARD);
        driveMotors[2].setDirection(DcMotorSimple.Direction.FORWARD);
    }

    /**
     * Set the run mode of the drive motors
     * @param runMode Drive Motors' RunMode
     */
    public void setRunMode(DcMotor.RunMode runMode){
        for (DcMotor driveMotor : driveMotors){
            driveMotor.setMode(runMode);
        }
    }

    /**
     * Set the drive power of the drive motors
     * to either move forward or backward or turn left or right
     * @param leftPower  Left Drive Power
     * @param rightPower Right Drive Power
     */
    public void setDrivePower(double leftPower, double rightPower){
        driveMotors[0].setPower(leftPower);
        driveMotors[1].setPower(rightPower);
        driveMotors[2].setPower(leftPower);
        driveMotors[3].setPower(rightPower);
    }

    /**
     * Set the power of the arm which holds the flaps
     *
     * @param armPower Flap Arm Power
     */
    public void setArmPower(double armPower){
        flapperArm.setPower(armPower);
    }

    /**
     * Set the position that either opens or closes the flaps
     *
     * @param position Opens or closes the flaps
     */
    public void setFlapPosition(double position){
        flaps[0].setPosition(MID_SERVO + position);
        flaps[1].setPosition(MID_SERVO - position);
    }

    /**
     * Set the power of the jewel slider, which slides the arm out and in.
     * @param sliderPower Power of the jewel slider
     */
    public void setJewelSliderPower(double sliderPower){
        jewelSlider.setPower(sliderPower);
    }

    /**
     * Set the power (position) of the jewel arm
     * @param jewelArmPower Power of the jewel arm
     */
    public void setJewelArmPower(double jewelArmPower){
        jewelArm.setPower(jewelArmPower);
    }

    /**
     * Set the position of the jewel elbow.
     * @param elbowPosition Position of the elbow
     */
    public void setJewelElbowPosition(double elbowPosition){
        jewelElbow.setPosition(elbowPosition);
    }

    /**
     * Adds a new log on telemetry
     */
    public void addLog(String entry){
        telemetry.log().add(entry);
    }

    /**
     * Clear all logs on the telemetry
     */
    public void clearLog(){
        telemetry.log().clear();
    }
}
