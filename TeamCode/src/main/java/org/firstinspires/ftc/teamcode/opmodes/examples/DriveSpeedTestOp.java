package org.firstinspires.ftc.teamcode.opmodes.examples;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.robot.DriveBot;

/**
 * This class provides the drivers to experiment speed levels for their tank and sideways movement
 * @author Henry
 * @version 1.0
 */
@TeleOp(name = "Drive Speed Test", group = "test")
public class DriveSpeedTestOp extends OpMode {

    private static final double DRIVE_SPEED = 0.5;
    private static final double INCREMENT_SPEED = 0.01;

    private DriveBot robot;

    private double driveSpeed = 1.00;
    private double sidewaysSpeed = 1.00;

    // Sideways movement
    private boolean sidewaysMovement;
    private boolean sidewaysMovementButtonDown;

    // Implements a button down way for the gamepads
    private boolean aButtonDown;
    private boolean bButtonDown;
    private boolean xButtonDown;
    private boolean yButtonDown;

    @Override
    public void init() {
        robot = new DriveBot();
        robot.init(hardwareMap, telemetry);

        // Tells the user that the robot is ready to start
        telemetry.addData(">", "ROBOT READY!");
        telemetry.update();
    }

    @Override
    public void init_loop() {
        // Tells the user that the robot is ready to start
        telemetry.addData(">", "waiting for START...");
        telemetry.addData("position", robot.getDumper().getServoDumper().getPosition());
        telemetry.update();
    }

    @Override
    public void loop() {
        // Get joystick y values from gamepad one
        float joystickLeftYOne = gamepad1.left_stick_y;
        float joystickRightYOne = gamepad1.right_stick_y;

        // Get joystick x values from gamepad one
        float joystickLeftXOne = gamepad1.left_stick_x;
        float joystickRightXOne = gamepad1.right_stick_x;

        // Clips the speed from the joystick so the robot can drive at a desired speed
        double leftSpeedNormal = Range.clip(-driveSpeed, driveSpeed, joystickLeftYOne);
        double rightSpeedNormal = Range.clip(-driveSpeed, driveSpeed, joystickRightYOne);
        double leftSpeedSideways = Range.clip(-sidewaysSpeed, sidewaysSpeed, joystickLeftXOne);
        double rightSpeedSideways = Range.clip(-sidewaysSpeed, sidewaysSpeed, joystickRightXOne);

        // When the user presses the button, the robot switches from normal to sideways movement
        if (gamepad1.a && !sidewaysMovementButtonDown) {
            sidewaysMovement = !sidewaysMovement;
            sidewaysMovementButtonDown = true;
        } else if (!gamepad1.a && sidewaysMovementButtonDown) {
            sidewaysMovementButtonDown = false;
        }

        // Drives the robot around
        if (sidewaysMovement) {
            if (gamepad1.dpad_down) {
                robot.setDrivePower(driveSpeed, driveSpeed);
            } else if (gamepad1.dpad_up) {
                robot.setDrivePower(-driveSpeed, -driveSpeed);
            } else if (gamepad1.dpad_left) {
                robot.setDrivePowerSideways(-sidewaysSpeed, -sidewaysSpeed);
            } else if (gamepad1.dpad_right) {
                robot.setDrivePowerSideways(sidewaysSpeed, sidewaysSpeed);
            } else {
                robot.setDrivePowerSideways(leftSpeedSideways, rightSpeedSideways);
            }
        } else {
            if (gamepad1.dpad_down) {
                robot.setDrivePower(driveSpeed, driveSpeed);
            } else if (gamepad1.dpad_up) {
                robot.setDrivePower(-driveSpeed, -driveSpeed);
            } else if (gamepad1.dpad_left) {
                robot.setDrivePower(driveSpeed, -driveSpeed);
            } else if (gamepad1.dpad_right) {
                robot.setDrivePower(-driveSpeed, driveSpeed);
            } else {
                robot.setDrivePower(leftSpeedNormal, rightSpeedNormal);
            }
        }

        // Increases or decreases the speed by pressing the buttons
        if (gamepad2.y && !yButtonDown) {
            yButtonDown = true;
            driveSpeed += INCREMENT_SPEED;
        } else if (!gamepad2.y && yButtonDown) {
            yButtonDown = false;
        }
        if (gamepad2.x && !xButtonDown) {
            xButtonDown = true;
            driveSpeed -= INCREMENT_SPEED;
        } else if (!gamepad2.x && xButtonDown) {
            xButtonDown = false;
        }
        if (gamepad2.b && !bButtonDown) {
            bButtonDown = true;
            sidewaysSpeed += INCREMENT_SPEED;
        } else if (!gamepad2.b && bButtonDown) {
            bButtonDown = false;
        }
        if (gamepad2.a && !aButtonDown) {
            aButtonDown = true;
            sidewaysSpeed -= INCREMENT_SPEED;
        } else if (!gamepad2.a && aButtonDown) {
            aButtonDown = false;
        }

        // Clips the speed between 0 to 1
        driveSpeed = Range.clip(driveSpeed, 0, 1);
        sidewaysSpeed = Range.clip(sidewaysSpeed, 0, 1);

        // Send telemery data to the driver station
        telemetry.addData("Instructions", "Increases or decreases the speed by gamepad1 buttons");
        telemetry.addData("Y: drive up | X: drive down", "B: sideways up | A: sideways down");
        telemetry.addData("drive speed", "%.2f", driveSpeed);
        telemetry.addData("sideways speed", sidewaysSpeed);
        telemetry.addData("Movement Mode", sidewaysMovement ? "Sideways" : "Normal");
        telemetry.update();
    }
}
