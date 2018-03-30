package org.firstinspires.ftc.teamcode.competition;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@Deprecated
@TeleOp(name = "Competition: Drive Control", group = "Competition")
@Disabled
public class DriveControlOld extends OpMode {

    /* This class used to be the program that the team used during competition and driving
     * practice. However, this class no longer serves as the main drive program as new
     * class now replaces this because of the usage of the DriveBot.class. For now, this class
     * shall serve as a reference for any future driving programs that should have in its
     * program.
     */

    /////////////////////////////////////////////////////////////
    //---------------------------------------------------------//
    //---------You can edit anything below this box.-----------//
    //---------------------------------------------------------//
    /////////////////////////////////////////////////////////////

    // Whether to use encoders on the drive motors
    private static final boolean USE_ENCODERS = true;

    // Sped of the flap arm
    private static final double ARM_SPEED = 0.3;

    // Servo resets back into their position
    private static final double MID_SERVO_LEFT = 0.5;
    private static final double MID_SERVO_RIGHT = 0.5;

    // Min and max of the flap position
    private static final double MIN_FLAP_POSITION = -0.45;
    private static final double MAX_FLAP_POSITION = 0.50;

    //
    private static final double SERVO_SPEED = 0.02;

    /////////////////////////////////////////////////////////////
    //---------------------------------------------------------//
    //---------No editing below unless you absolutely----------//
    //--------------know what are you doing.-------------------//
    //---------------------------------------------------------//
    /////////////////////////////////////////////////////////////

    private DcMotor[] driveMotors = new DcMotor[4];

    private DcMotor clapperMotor;
    private Servo   clapperServoLeft;
    private Servo   clapperServoRight;

    private CRServo crServo;

    private double joystickClip  = 1;
    private double servoPosition = 0;

    private double[] joysticksOne = new double[2];

    @Override
    public void init() {
        // Get hardware references for the robot
        driveMotors[0] = hardwareMap.get(DcMotor.class, "motorleftfront");
        driveMotors[1] = hardwareMap.get(DcMotor.class, "motorrightfront");
        driveMotors[2] = hardwareMap.get(DcMotor.class, "motorleftrear");
        driveMotors[3] = hardwareMap.get(DcMotor.class, "motorrightrear");

        clapperMotor      = hardwareMap.get(DcMotor.class, "motorarm");
        clapperServoLeft  = hardwareMap.get(Servo.class,   "servoleftclap");
        clapperServoRight = hardwareMap.get(Servo.class,   "servorightclap");

        crServo = hardwareMap.get(CRServo.class, "servolowerarm");

        driveMotors[1].setDirection(DcMotor.Direction.REVERSE);
        driveMotors[3].setDirection(DcMotor.Direction.REVERSE);

        for (DcMotor driveMotor : driveMotors){
            if (USE_ENCODERS){
                driveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                driveMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            } else {
                driveMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }
        }

        telemetry.addData(">", "Press the START button!");
        telemetry.update();
    }

    @Override
    public void start(){
        clapperServoLeft.setPosition(MID_SERVO_LEFT);
        clapperServoRight.setPosition(MID_SERVO_RIGHT);
    }

    @Override
    public void loop() {
        setDriveSpeedLimit(); // Set the limit on the drive speed
        setDriveSpeed();      // Set the power of the drive motors
        controlClapper();     // Controls the clapper using gamepad buttons

        updateTelemetry();    // Updates information on the driver phone
    }

    /**
     * Controls the drive speed of the robot
     */
    private void setDriveSpeed(){
        joysticksOne[0] = -gamepad1.left_stick_y;
        joysticksOne[1] = -gamepad1.right_stick_y;

        joysticksOne[0] = Range.clip(joysticksOne[0], -joystickClip, joystickClip);
        joysticksOne[1] = Range.clip(joysticksOne[1], -joystickClip, joystickClip);

        // Moves the robot in tank drive based from gamepad 1 joysticks
        driveMotors[0].setPower(joysticksOne[0]);
        driveMotors[1].setPower(joysticksOne[1]);
        driveMotors[2].setPower(joysticksOne[0]);
        driveMotors[3].setPower(joysticksOne[1]);
    }

    /**
     * Controls the limit on drive speed of the robot
     */
    private void setDriveSpeedLimit(){
        if (gamepad1.a){
            joystickClip = 1;
        } else if (gamepad1.b) {
            joystickClip = 0.7;
        } else if (gamepad1.y){
            joystickClip = 0.5;
        } else if (gamepad1.x){
            joystickClip = 0.3;
        }
    }

    /**
     * Controls the clapper using gamepad buttons
     */
    private void controlClapper(){
        // Moves the clapper arm up or down and opens and closes the flaps based from
        // gamepad 2 left joystick and A and Y buttons.

        // Set the power of the clapper arm
        if (gamepad2.y){
            clapperMotor.setPower(ARM_SPEED);
        } else if (gamepad2.a){
            clapperMotor.setPower(-ARM_SPEED);
        } else {
            clapperMotor.setPower(0);
        }

        if (gamepad2.left_stick_y > 0.2){
            servoPosition += SERVO_SPEED;
        } else if (gamepad2.left_stick_y < -0.2){
            servoPosition -= SERVO_SPEED;
        }
        servoPosition = Range.clip(servoPosition, MIN_FLAP_POSITION, MAX_FLAP_POSITION);

        clapperServoLeft.setPosition(MID_SERVO_LEFT + servoPosition);
        clapperServoRight.setPosition(MID_SERVO_RIGHT - servoPosition);
    }

    private void updateTelemetry(){
        // Sends information to the driver phone through telemetry
        telemetry.addLine("Power Setting");
        telemetry.addLine("A: 1.00");
        telemetry.addLine("B: 0.70");
        telemetry.addLine("Y: 0.50");
        telemetry.addLine("X: 0.30");
        telemetry.addData("Mode", driveMotors[0].getMode().toString());

        // Drive motors
        telemetry.addLine("------------------------------");
        telemetry.addData("Drive Clip", joystickClip);
        telemetry.addData("Drive Power", "%.2f %.2f %.2f %.2f",
                driveMotors[0].getPower(), driveMotors[1].getPower(), driveMotors[2].getPower(),
                driveMotors[3].getPower());
        telemetry.addData("Encoder Position", "%d %d %d %d",
                driveMotors[0].getCurrentPosition(), driveMotors[1].getCurrentPosition(),
                driveMotors[2].getCurrentPosition(), driveMotors[3].getCurrentPosition());

        // Paddle Arm
        telemetry.addLine("------------------------------");
        telemetry.addData("Arm Power", "%.2f", clapperMotor.getPower());

        // Servos
        telemetry.addLine("------------------------------");
        telemetry.addData("Servo Offset", "%.2f", servoPosition);
        telemetry.addData("Servo Position", "%.2f %.2f", clapperServoLeft.getPosition(),
                clapperServoRight.getPosition());

        // Clears and updates the telemetry
        telemetry.update();
    }

    @Override
    public void stop(){
        // Set the power of the arm to zero
        clapperMotor.setPower(0);

        // Set the power of all drive motors to stop movement
        for (DcMotor driveMotor : driveMotors){
            driveMotor.setPower(0);
        }
    }
}
