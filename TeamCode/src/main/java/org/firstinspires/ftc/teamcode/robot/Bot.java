package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public abstract class Bot implements Robot {

    protected HardwareMap hwMap;   // Robot's hardware map
    protected Telemetry telemetry; // Robot's central telemetry

    // Drive system
    private DcMotor[] driveMotors; // Four motors

    // Paddle System
    private DcMotor paddleArmMotor;
    private Servo[] paddleServos;

    // Jewel system
    private DcMotor jewelSliderMotor;
    private CRServo jewelArmCrServo;         // Note: one motor and one servo!!!

    private static final double MID_SERVO_POSITION = 0.5;

    Bot(){
        driveMotors  = new DcMotor[4];
        paddleServos = new Servo[2];
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
        // Drive Motors:
        driveMotors[0] = hwMap.get(DcMotor.class, "motorleftfront");
        driveMotors[1] = hwMap.get(DcMotor.class, "motorrightfront");
        driveMotors[2] = hwMap.get(DcMotor.class, "motorleftrear");
        driveMotors[3] = hwMap.get(DcMotor.class, "motorrightrear");

        // Paddle System:
        paddleArmMotor  = hwMap.get(DcMotor.class, "motorarm");
        paddleServos[0] = hwMap.get(Servo.class, "servoleftclap");
        paddleServos[1] = hwMap.get(Servo.class, "servorightclap");

        // Jewel arm system:
        jewelSliderMotor = hwMap.get(DcMotor.class, "motorlowerarm");
        jewelArmCrServo  = hwMap.get(CRServo.class,"servolowerarm");

        // Reverses direction of the right drive motors
        driveMotors[1].setDirection(DcMotorSimple.Direction.REVERSE);
        driveMotors[3].setDirection(DcMotorSimple.Direction.REVERSE);

        // Forwards direction of the left drive motors
        driveMotors[0].setDirection(DcMotorSimple.Direction.FORWARD);
        driveMotors[2].setDirection(DcMotorSimple.Direction.FORWARD);

        jewelArmCrServo.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    /**
     * Closes all hardware devices on the robot
     */
    public void close() {
        for (int i = 0; i < getDriveMotors().length; i++){
            getDriveMotors()[i].close();
        }
        for (int i = 0; i < getPaddleServos().length; i++){
            getPaddleServos()[i].close();
        }

        getPaddleArmMotor().close();
        getJewelArmCrServo().close();
        getJewelSliderMotor().close();
    }

    ////////////////////////////////////////////////////////////////////////

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
     * Set the position that either opens or closes the paddleServos
     *
     * @param position Opens or closes the paddleServos
     */
    public void setPaddlePosition(double position){
        paddleServos[0].setPosition(MID_SERVO_POSITION + position);
        paddleServos[1].setPosition(MID_SERVO_POSITION - position);
    }

    ////////////////////////////////////////////////////////////////////////

    public DcMotor[] getDriveMotors() {
        return driveMotors;
    }

    public DcMotor getPaddleArmMotor() {
        return paddleArmMotor;
    }

    public Servo[] getPaddleServos() {
        return paddleServos;
    }

    public DcMotor getJewelSliderMotor() {
        return jewelSliderMotor;
    }

    public CRServo getJewelArmCrServo() {
        return jewelArmCrServo;
    }
}
