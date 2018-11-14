package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.robot.DriveBot;
import org.firstinspires.ftc.teamcode.robot.Lift;

/**
 * This class provides the drive functionality for the driver to drive the robot on the field
 * and during competition.
 * @author Henry
 */
@TeleOp(name = "Drive Op 1.1", group = "drive")
public class DriveOp extends OpMode {

    private static final double DRIVE_SPEED = 0.5;
    private static final double LIFT_POWER = 0.25;
    private static final double LIFT_SPEED = 0.02;

    private double liftPosition = 0.5;

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
        drive();
        lift();

        telemetry.addData("Movement Mode", sidewaysMovement ? "Sideways" : "Normal");
        telemetry.update();
    }

    private void drive() {
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
    }

    private void lift() {
        // Raises or lowers the lift
        if (gamepad2.dpad_up && !gamepad2.dpad_down) {
            robot.getLift().setLiftPower(LIFT_POWER);
        } else if (!gamepad2.dpad_up && gamepad2.dpad_down) {
            robot.getLift().setLiftPower(-LIFT_POWER);
        } else {
            robot.getLift().setLiftPower(0);
        }

        // Releases or closes the lift
        if (gamepad2.b) {
            liftPosition += LIFT_SPEED;
        } else if (gamepad2.x) {
            liftPosition += -LIFT_SPEED;
        }
        liftPosition = Range.clip(liftPosition, Lift.MIN_LIFT_SERVO_POSITION, Lift.MAX_LIFT_SERVO_POSITION);
        robot.getLift().setLiftPosition(liftPosition);
    }
}
