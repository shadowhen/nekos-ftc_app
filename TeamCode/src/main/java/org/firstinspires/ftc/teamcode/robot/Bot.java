package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.hardware.motors.TetrixMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.configuration.MotorConfigurationType;

import org.firstinspires.ftc.robotcore.external.Telemetry;

abstract class Bot implements Robot {

    protected HardwareMap hwMap;    // Robot's hardware map
    protected Telemetry telemetry;  // Robot's central telemetry

    private DcMotor[] motorDrive;   // Four drive motors

    private DcMotor motorPaddleArm; // Motor Arm that lifts and lowers the paddles
    private Servo[] servoPaddle;    // Paddles that hold the glyphs

    private DcMotor motorSlider;    // Jewel Arm Slider
    private CRServo servoJewelArm;  // Jewel CRServo Arm

    private static final double MID_SERVO_POSITION = 0.5;

    protected Bot() {
        motorDrive  = new DcMotor[4];
        servoPaddle = new Servo[2];
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
        telemetry.log().setCapacity(5);

        // Get hardware references from the robot controller for the robot
        // Drive Motors:
        motorDrive[0] = hwMap.get(DcMotor.class, "motorleftfront");
        motorDrive[1] = hwMap.get(DcMotor.class, "motorrightfront");
        motorDrive[2] = hwMap.get(DcMotor.class, "motorleftrear");
        motorDrive[3] = hwMap.get(DcMotor.class, "motorrightrear");

        // Paddle System:
        motorPaddleArm = hwMap.get(DcMotor.class,"motorarm");
        servoPaddle[0] = hwMap.get(Servo.class,"servoleftclap");
        servoPaddle[1] = hwMap.get(Servo.class,"servorightclap");

        // Jewel arm system:
        motorSlider = hwMap.get(DcMotor.class,"motorlowerarm");
        servoJewelArm = hwMap.get(CRServo.class,"servolowerarm");

        // Reverses direction of the right drive motors
        motorDrive[1].setDirection(DcMotorSimple.Direction.REVERSE);
        motorDrive[3].setDirection(DcMotorSimple.Direction.REVERSE);

        // Forwards direction of the left drive motors
        motorDrive[0].setDirection(DcMotorSimple.Direction.FORWARD);
        motorDrive[2].setDirection(DcMotorSimple.Direction.FORWARD);

        motorSlider.setDirection(DcMotorSimple.Direction.FORWARD);
        motorPaddleArm.setDirection(DcMotorSimple.Direction.REVERSE);
        servoJewelArm.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    /**
     * Closes all hardware devices on the robot
     */
    public void close() {
        for (int i = 0; i < getMotorDrive().length; i++){
            motorDrive[i].close();
        }
        for (int i = 0; i < getServoPaddle().length; i++){
            servoPaddle[i].close();
        }

        motorPaddleArm.close();
        servoJewelArm.close();
        motorSlider.close();
    }

    /**
     * Set the power of the drive motors
     * @param leftPower  Left Drive Power
     * @param rightPower Right Drive Power
     */
    public void setDrivePower(double leftPower, double rightPower){
        motorDrive[0].setPower(leftPower);
        motorDrive[1].setPower(rightPower);
        motorDrive[2].setPower(leftPower);
        motorDrive[3].setPower(rightPower);
    }

    /**
     * Set the position that either opens or closes the servoPaddle (only max 0.5)
     * @param position Opens or closes the servoPaddle
     */
    public void setPaddlePosition(double position){
        servoPaddle[0].setPosition(MID_SERVO_POSITION + position);
        servoPaddle[1].setPosition(MID_SERVO_POSITION - position);
    }

    public DcMotor[] getMotorDrive() {
        return motorDrive;
    }

    public DcMotor getMotorPaddleArm() {
        return motorPaddleArm;
    }

    public Servo[] getServoPaddle() {
        return servoPaddle;
    }

    public DcMotor getMotorSlider() {
        return motorSlider;
    }

    public CRServo getServoJewelArm() {
        return servoJewelArm;
    }
}
