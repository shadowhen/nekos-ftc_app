package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.robot.DriveBot;

@TeleOp(name = "Drive Test")
public class DriveTest extends OpMode {

    private DriveBot robot = new DriveBot();

    private double[] joystick = new double[4];

    private double paddlePosition = 0;

    private static final double PADDLE_SPEED = 0.02;

    @Override
    public void init() {
        robot.init(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        // Get joystick values from both gamepads
        joystick[0] = -gamepad1.left_stick_y;
        joystick[1] = -gamepad1.right_stick_y;

        // Set the speed limit on the drive motors
        if (gamepad1.a){
            robot.setDriveSpeedLimit(1.00);
        } else if (gamepad1.b) {
            robot.setDriveSpeedLimit(0.75);
        } else if (gamepad1.y){
            robot.setDriveSpeedLimit(0.50);
        } else if (gamepad1.x){
            robot.setDriveSpeedLimit(0.25);
        }

        // Drives the robot around
        if (gamepad1.dpad_up) {
            joystick[0] = robot.getDriveSpeedLimit();
            joystick[1] = robot.getDriveSpeedLimit();
        } else if (gamepad1.dpad_down) {
            joystick[0] = -robot.getDriveSpeedLimit();
            joystick[1] = -robot.getDriveSpeedLimit();
        } else if (gamepad1.dpad_left) {
            joystick[0] = -robot.getDriveSpeedLimit();
            joystick[1] = robot.getDriveSpeedLimit();
        } else if (gamepad1.dpad_right) {
            joystick[0] = robot.getDriveSpeedLimit();
            joystick[1] = -robot.getDriveSpeedLimit();
        }
        robot.setDrivePower(joystick[0], joystick[1]);

        // Moves the paddle arm up or down
        if (gamepad2.a) {
            robot.getPaddleArmMotor().setPower(-0.3);
        } else if (gamepad2.y) {
            robot.getPaddleArmMotor().setPower(0.3);
        } else {
            robot.getPaddleArmMotor().setPower(0);
        }

        // Opens or closes paddles
        if (gamepad2.left_stick_y > 0.2) {
            paddlePosition += PADDLE_SPEED;
        } else if (gamepad2.left_stick_y < -0.2) {
            paddlePosition -= PADDLE_SPEED;
        } else if (gamepad2.dpad_up) {
            paddlePosition = 0.5;
        } else if (gamepad2.dpad_down) {
            paddlePosition = -0.5;
        } else if (gamepad2.dpad_left) {
            paddlePosition = 0.0;
        }
        paddlePosition = Range.clip(paddlePosition, -0.5, 0.5);
        robot.setPaddlePosition(paddlePosition);

        // Pulls or push the jewel arm in or out
        robot.getJewelSliderMotor().setPower(-gamepad2.right_stick_y);

        // Moves the jewel arm around
        if (gamepad2.x) {
            robot.getJewelArmCrServo().setPower(0.8);
        } else if (gamepad2.b) {
            robot.getJewelArmCrServo().setPower(-0.8);
        } else {
            robot.getJewelArmCrServo().setPower(0.0);
        }

        telemetry.addData("Speed Limit", "%.2f", robot.getDriveSpeedLimit());
        telemetry.addLine("Controls: ").addData("A","1.00")
                .addData("B", "0.75").addData("Y", "0.50")
                .addData("X", "0.25");
        telemetry.addLine("Speed: ")
                .addData("Left", "%.2f %.2f",
                        robot.getDriveMotors()[0].getPower(),
                        robot.getDriveMotors()[2].getPower())
                .addData("Right", "%.2f %.2f",
                        robot.getDriveMotors()[1].getPower(),
                        robot.getDriveMotors()[3].getPower());
        telemetry.addData("CR Servo Power", robot.getJewelArmCrServo().getPower());
        telemetry.addData("Paddle Position", paddlePosition);
        telemetry.update();
    }

    @Override
    public void stop() {
        robot.close();
    }
}
