package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.robot.DriveBot;

/**
 * This class provides the drivers to experiment speed levels for their tank and sideways movement
 * @author Henry
 */
@TeleOp(name = "Drive Speed Test", group = "drive")
public class DriveSpeedTestOp extends OpMode {

    private static final double DRIVE_SPEED = 0.5;

    private DriveBot robot;

    private double driveSpeed = 1.00;
    private double sidewaysSpeed = 1.00;

    // Sideways movement
    private boolean sidewaysMovement;
    private boolean sidewaysMovementButtonDown;

    @Override
    public void init() {
        robot = new DriveBot();
        robot.init(hardwareMap, telemetry);

        // Tells the user that the robot is ready to start
        telemetry.addData(">", "ROBOT READY!!!!");
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
                robot.setDrivePowerSideways(joystickLeftXOne, joystickLeftXOne);
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
                robot.setDrivePower(joystickLeftYOne, joystickRightYOne);
            }
        }

        // Increases or decreases the speed by buttons
        if (gamepad1.y) {
            driveSpeed += 0.05;
        }
        if (gamepad1.x) {
            driveSpeed -= 0.05;
        }
        if (gamepad1.b) {
            sidewaysSpeed += 0.05;
        }
        if (gamepad1.a) {
            sidewaysSpeed -= 0.05;
        }

        // Clips the speed between 0 to 1
        driveSpeed = Range.clip(driveSpeed, 0, 1);
        sidewaysSpeed = Range.clip(sidewaysSpeed, 0, 1);

        telemetry.addData("Instructions", "Increases or decreases the speed by gamepad1 buttons");
        telemetry.addData("Y: drive up | X: drive down", "B: sideways up | A: sideways down");
        telemetry.addData("drive speed", driveSpeed);
        telemetry.addData("sideways speed", sidewaysSpeed);
        telemetry.addData("Movement Mode", sidewaysMovement ? "Sideways" : "Normal");
        telemetry.update();
    }
}
