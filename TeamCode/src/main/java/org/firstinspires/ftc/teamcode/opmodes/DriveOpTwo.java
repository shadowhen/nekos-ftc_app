package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.DriveBot;

@TeleOp(name = "Drive Op 1.1", group = "drive")
public class DriveOpTwo extends OpMode {

    private static final double DRIVE_SPEED = 0.5;

    private DriveBot robot;

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
                robot.setDrivePower(DRIVE_SPEED, DRIVE_SPEED);
            } else if (gamepad1.dpad_up) {
                robot.setDrivePower(-DRIVE_SPEED, -DRIVE_SPEED);
            } else if (gamepad1.dpad_left) {
                robot.setDrivePowerSideways(-DRIVE_SPEED, -DRIVE_SPEED);
            } else if (gamepad1.dpad_right) {
                robot.setDrivePowerSideways(DRIVE_SPEED, DRIVE_SPEED);
            } else {
                robot.setDrivePowerSideways(joystickLeftXOne, joystickLeftXOne);
            }
        } else {
            if (gamepad1.dpad_down) {
                robot.setDrivePower(DRIVE_SPEED, DRIVE_SPEED);
            } else if (gamepad1.dpad_up) {
                robot.setDrivePower(-DRIVE_SPEED, -DRIVE_SPEED);
            } else if (gamepad1.dpad_left) {
                robot.setDrivePower(DRIVE_SPEED, -DRIVE_SPEED);
            } else if (gamepad1.dpad_right) {
                robot.setDrivePower(-DRIVE_SPEED, DRIVE_SPEED);
            } else {
                robot.setDrivePower(joystickLeftYOne, joystickRightYOne);
            }
        }

        // Adds telemery data from the robot class
        robot.addData();
        telemetry.addData("Movement Mode", sidewaysMovement ? "Sideways" : "Normal");
        telemetry.update();
    }
}
