package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.DriveBot;

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

    private static final double DUMPER_SPEED = 0.002;
    private static final double PUSHER_SPEED = 0.001;

    private double dumperPosition = 0.5;
    private boolean lockPusherStateButtonDown;
    private boolean lockPusherState = true;

    private DriveBot robot;

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

        //telemetry.addData("Movement Mode", sidewaysMovement ? "Sideways" : "Normal");
        telemetry.addData("PUSHER LOCKED", lockPusherState ? "ON" : "OFF");
        telemetry.addData("Left Motor Front Power", robot.getMotorDriveLeftFront().getPower());
        telemetry.addData("Left Motor Rear Power", robot.getMotorDriveLeftRear().getPower());
        telemetry.addData("Right Motor Front Power", robot.getMotorDriveRightFront().getPower());
        telemetry.addData("Right Motor Rear Power", robot.getMotorDriveRightRear().getPower());
        telemetry.addData("Sweeper Motor Power", robot.getSweeper().getSweeperMotor().getPower());
        telemetry.addData("Sweeper Lift Motor Power", robot.getSweeper().getLiftMotor().getPower());
        telemetry.addData("Sweeper Slider Motor Power", robot.getSweeper().getSliderMotor().getPower());
        telemetry.addData("Lift Motor Power", robot.getLift().getLiftMotor().getPower());
        telemetry.addData("Pusher Position", robot.getPusher().getServoPusher().getPosition());
        telemetry.addData("Dumper Position", robot.getDumper().getServoDumper().getPosition());
        telemetry.update();
    }

    private void drive() {
        // Get joystick y values from gamepad one
        float joystickLeftYOne = gamepad1.left_stick_y;
        float joystickRightYOne = gamepad1.right_stick_y;

        // Get joystick x values from gamepad one
        float joystickLeftXOne = gamepad1.left_stick_x;
        float joystickRightXOne = gamepad1.right_stick_x;

        float leftTrigger = gamepad1.left_trigger;
        float rightTrigger = gamepad1.right_trigger;
        float totalTrigger = Math.abs(leftTrigger - rightTrigger);

        if ((leftTrigger >= 0.0f) && (leftTrigger > rightTrigger)) {
            robot.setDrivePowerSideways(-totalTrigger, totalTrigger);
        } else if ((rightTrigger >= 0.0f) && (rightTrigger > leftTrigger)) {
            robot.setDrivePowerSideways(totalTrigger, -totalTrigger);
        } else if (gamepad1.dpad_down) {
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

    private void lift() {
        // Raises or lowers the lift
        if (gamepad2.left_bumper) {
            robot.getLift().setLiftPower(-LIFT_POWER);
        } else if (gamepad2.right_bumper) {
            robot.getLift().setLiftPower(LIFT_POWER);
        } else {
            robot.getLift().setLiftPower(0.0);
        }
    }

    private void dump() {
        if (gamepad2.left_trigger > 0.0) {
            dumperPosition = dumperPosition + -(DUMPER_SPEED);
        } else if (gamepad2.right_trigger > 0.0) {
            dumperPosition = dumperPosition + (DUMPER_SPEED);
        }

        robot.getDumper().setPosition(dumperPosition);
    }

    private void sweep() {
        double joystickSweep = gamepad2.right_stick_y;

        // Moves the slide horizontally
        if (gamepad2.dpad_up) {
            robot.getSweeper().setSliderPower(SLIDER_POWER);
        } else if (gamepad2.dpad_down) {
            robot.getSweeper().setSliderPower(-SLIDER_POWER);
        } else {
            robot.getSweeper().setSliderPower(0.0);
        }

        // Lift the sweeper
        if (gamepad2.y) {
            robot.getSweeper().setLiftPower(LIFT_POWER);
        } else if (gamepad2.a) {
            robot.getSweeper().setLiftPower(-LIFT_POWER);
        } else {
            robot.getSweeper().setLiftPower(0.0);
        }

        // Sweeps minerals from the floor
        robot.getSweeper().setSweeperPower(joystickSweep);
    }

    private void push() {
        if (!lockPusherStateButtonDown && gamepad2.b) {
            lockPusherStateButtonDown = true;
            lockPusherState = !lockPusherState;
        } else if (lockPusherStateButtonDown && !gamepad2.b) {
            lockPusherStateButtonDown = false;
        }

        if (!lockPusherState) {
            if (gamepad2.left_stick_y < 0.0f) {
                robot.getPusher().getServoPusher().setPosition(robot.getPusher().getServoPusher().getPosition() - PUSHER_SPEED);
            } else if (gamepad2.left_stick_y > 0.0f) {
                robot.getPusher().getServoPusher().setPosition(robot.getPusher().getServoPusher().getPosition() + PUSHER_SPEED);
            }
        }
    }
}
