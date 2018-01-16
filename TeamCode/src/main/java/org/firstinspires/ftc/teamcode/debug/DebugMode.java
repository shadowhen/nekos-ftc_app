package org.firstinspires.ftc.teamcode.debug;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.DriveBot;

@TeleOp(name = "Debug Mode (DO NOT RUN)")
@Disabled
public class DebugMode extends OpMode {

    private DriveBot robot = new DriveBot();
    private DcMotor  lowerArmMotor;
    private Servo    lowerArmServo;

    private double joystickLimit  = 1;
    private double[] joysticksOne = new double[2];

    private static final double MID_LOWER_SERVO = 0.5;

    @Override
    public void init() {
        lowerArmMotor = hardwareMap.get(DcMotor.class, "lowerarmmotor");
        lowerArmServo = hardwareMap.get(Servo.class, "lowerarmservo");

        lowerArmMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lowerArmMotor.setPower(0);
        lowerArmServo.setPosition(MID_LOWER_SERVO);

        robot.init(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        double flapOffset;
        double lowerOffset;

        // Set the limit of how much gampad1 joystick values is used to move the robot
        if (gamepad1.a){
            joystickLimit = 1;
        } else if (gamepad1.b) {
            joystickLimit = 0.7;
        } else if (gamepad1.y){
            joystickLimit = 0.5;
        } else if (gamepad1.x){
            joystickLimit = 0.3;
        }

        // Limits the joysticks on gamepad 1, allowing a fluid control of the robot
        joysticksOne[0] = Range.clip(joysticksOne[0], -joystickLimit, joystickLimit);
        joysticksOne[1] = Range.clip(joysticksOne[1], -joystickLimit, joystickLimit);

        // Set the drive power of the robot
        robot.setDrivePower(-joysticksOne[0], -joysticksOne[1]);

        // Y moves the flap arm up
        // A moves the flap arm down
        if (gamepad2.y){
            robot.setArmPower(0.5);
        } else if (gamepad2.a){
            robot.setArmPower(-0.5);
        } else {
            robot.setArmPower(0);
        }

        // X moves the lower arm left
        // B moves the lower arm right
        if (gamepad2.x){
            lowerArmMotor.setPower(0.5);
        } else if (gamepad2.b){
            lowerArmMotor.setPower(-0.5);
        } else {
            lowerArmMotor.setPower(0);
        }

        // Flaps that hold the glyph
        flapOffset = gamepad2.left_stick_y;
        flapOffset = Range.clip(flapOffset, -0.5, 0.5);
        robot.setFlapPosition(flapOffset);

        // Lower servo hits the jewel off the base
        lowerOffset = gamepad2.right_stick_y;
        lowerOffset = Range.clip(lowerOffset, -0.5, 0.5);
        lowerArmServo.setPosition(MID_LOWER_SERVO + lowerOffset);

        telemetry.addData("Gamepad 1 Joystick", String.format("%.2f: %.2f",
                -gamepad1.left_stick_y, -gamepad1.right_stick_y));
        telemetry.addData("Clap Offset", String.format("%.2f", flapOffset));
        telemetry.addData("Lower Offset", String.format("%.2f", lowerOffset));
        telemetry.update();
    }
}
