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
    private static final double SLIDER_POWER = 0.25;

    private static final double DUMPER_SPEED = 0.02;

    private double dumperPosition = 0.5;

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
        dump();
        sweep();

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
        robot.getLift().setLiftPower(gamepad2.left_bumper && !gamepad2.right_bumper ? LIFT_POWER : 0.0);
        robot.getLift().setLiftPower(!gamepad2.left_bumper && gamepad2.right_bumper ? -LIFT_POWER : 0.0);
        robot.getLift().setLiftPower((!gamepad2.left_bumper && !gamepad2.right_bumper) ||
                (gamepad2.left_bumper && gamepad2.right_bumper) ? 1.0 : 0.0);
    }

    private void dump() {
        double leftTrigger = gamepad2.left_trigger;
        double rightTrigger = gamepad2.right_trigger;

        if (leftTrigger > 0 && rightTrigger <= 0) {
            dumperPosition = dumperPosition + -(leftTrigger * DUMPER_SPEED);
        } else if (leftTrigger <= 0 && rightTrigger > 0) {
            dumperPosition = dumperPosition + (rightTrigger * DUMPER_SPEED);
        }

        robot.getDumper().setPosition(dumperPosition);
    }

    private void sweep() {
        double joystickSweep = gamepad2.right_stick_y;

        // Moves the slide horizontally
        robot.getSweeper().setSliderPower(gamepad2.dpad_up && !gamepad2.dpad_down ? SLIDER_POWER : 0.0);
        robot.getSweeper().setSliderPower(!gamepad2.dpad_up && gamepad2.dpad_down ? -SLIDER_POWER : 0.0);
        robot.getSweeper().setSliderPower((gamepad2.dpad_up && gamepad2.dpad_down) ? 0.0 : 0.0);

        // Lift the sweeper
        robot.getSweeper().setLiftPower(gamepad2.y && !gamepad2.a ? LIFT_POWER : 0.0);
        robot.getSweeper().setLiftPower(!gamepad2.y && gamepad2.a ? -LIFT_POWER : 0.0);
        robot.getSweeper().setLiftPower(gamepad2.y && gamepad2.a ? 0.0 : 0.0);

        // Sweeps minerals from the floor
        robot.getSweeper().setSweeperPower(joystickSweep);
    }
}
