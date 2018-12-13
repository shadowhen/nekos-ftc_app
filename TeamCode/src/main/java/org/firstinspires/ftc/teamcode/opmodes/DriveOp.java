package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.robot.DriveBot;
import org.firstinspires.ftc.teamcode.robot.Dumper;

/**
 * This class provides the drive functionality for the driver to drive the robot on the field
 * and during competition.
 * @author Henry
 * @version 1.2
 */
@TeleOp(name = "Drive Op 1.2", group = "drive")
public class DriveOp extends OpMode {

    private static final double DRIVE_SPEED = 0.5;
    private static final double LIFT_POWER = 0.25;
    private static final double SWEEPER_POWER = 1.0;
    private static final double SWEEPER_LIFT_POWER = 0.8;
    private static final double SLIDER_POWER = 0.25;

    private static final double DUMPER_SPEED = 0.002;
    private static final double PUSHER_SPEED = 0.001;

    private double dumperPosition = 0.5;
    private boolean lockPusherStateButtonDown;
    private boolean lockPusherState = true;

    private boolean sidewaysState = false;
    private boolean sidewaysStateButtonDown;

    private DriveBot robot;

    @Override
    public void init() {
        robot = new DriveBot();
        robot.init(hardwareMap, telemetry);

        // Gets the current position of the server dumper, so the dumper position can match with
        // the servo dumper's position and adjust it when the driver adjusts the position from
        // the gamepads.
        dumperPosition = robot.getDumper().getServoDumper().getPosition();

        // Tells the user that the robot is ready to start
        telemetry.addData(">", "ROBOT READY!!!!");
        telemetry.addData("position", robot.getDumper().getServoDumper().getPosition());
        telemetry.update();
    }

    @Override
    public void loop() {
        drive();
        lift();
        dump();
        sweep();

        telemetry.addData("Movement Mode", sidewaysState ? "Bumpers" : "Dpad");
        telemetry.addData("PUSHER LOCKED", lockPusherState ? "ON" : "OFF");
        telemetry.addLine("-------------------");
        telemetry.addData("Left Motor Front Power", "%.2f", robot.getMotorDriveLeftFront().getPower());
        telemetry.addData("Left Motor Rear Power", "%.2f", robot.getMotorDriveLeftRear().getPower());
        telemetry.addData("Right Motor Front Power", "%.2f", robot.getMotorDriveRightFront().getPower());
        telemetry.addData("Right Motor Rear Power", "%.2f", robot.getMotorDriveRightRear().getPower());
        telemetry.addLine("-------------------");
        telemetry.addData("Sweeper Motor Power", "%.2f", robot.getSweeper().getSweeperMotor().getPower());
        telemetry.addData("Sweeper Lift Motor Power", "%.2f", robot.getSweeper().getLiftMotor().getPower());
        telemetry.addData("Sweeper Slider Motor Power", "%.2f", robot.getSweeper().getSliderMotor().getPower());
        telemetry.addLine("-------------------");
        telemetry.addData("Lift Motor Power", "%.2f", robot.getLift().getLiftMotor().getPower());
        telemetry.addLine("-------------------");
        telemetry.addData("Pusher Position", "%.2f", robot.getPusher().getServoPusher().getPosition());
        telemetry.addData("Dumper Position", "%.2f",robot.getDumper().getServoDumper().getPosition());
        telemetry.update();
    }

    private void drive() {
        // Get joystick y values from gamepad one
        float joystickLeftYOne = gamepad1.left_stick_y;
        float joystickRightYOne = gamepad1.right_stick_y;

        // Get joystick x values from gamepad one
        float joystickLeftXOne = gamepad1.left_stick_x;
        float joystickRightXOne = gamepad1.right_stick_x;

        if (!sidewaysStateButtonDown && gamepad1.back) {
            sidewaysState = !sidewaysState;
            sidewaysStateButtonDown = true;
        } else if (sidewaysStateButtonDown && !gamepad1.back) {
            sidewaysStateButtonDown = false;
        }

        // Robot moves sideways
        if (gamepad1.left_bumper) {
            if (!sidewaysState) {
                robot.setDrivePowerSideways(DRIVE_SPEED, -DRIVE_SPEED);
            } else {
                robot.setDrivePower(DRIVE_SPEED, -DRIVE_SPEED);
            }
        } else if (gamepad1.right_bumper) {
            if (!sidewaysState) {
                robot.setDrivePowerSideways(-DRIVE_SPEED, DRIVE_SPEED);
            } else {
                robot.setDrivePower(-DRIVE_SPEED, DRIVE_SPEED);
            }
        }

        // Set the drive power based on the input
        if (gamepad1.dpad_down) {
            robot.setDrivePower(DRIVE_SPEED, DRIVE_SPEED);
        } else if (gamepad1.dpad_up) {
            robot.setDrivePower(-DRIVE_SPEED, -DRIVE_SPEED);
        } else if (gamepad1.dpad_left) {
            if (sidewaysState) {
                robot.setDrivePowerSideways(DRIVE_SPEED, -DRIVE_SPEED);
            } else {
                robot.setDrivePower(DRIVE_SPEED, -DRIVE_SPEED);
            }
        } else if (gamepad1.dpad_right) {
            if (sidewaysState) {
                robot.setDrivePowerSideways(-DRIVE_SPEED, DRIVE_SPEED);
            } else {
                robot.setDrivePower(-DRIVE_SPEED, DRIVE_SPEED);
            }
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
        // Get both trigger values from the gamepad and the difference between the trigger values
        float leftTrigger = gamepad1.left_trigger;
        float rightTrigger = gamepad1.right_trigger;
        float totalTrigger = leftTrigger - rightTrigger;

        if (totalTrigger > 0.1f) {
            dumperPosition = dumperPosition + DUMPER_SPEED;
        } else if (totalTrigger < -0.1f) {
            dumperPosition = dumperPosition - DUMPER_SPEED;
        }
        dumperPosition = Range.clip(dumperPosition, Dumper.MIN_POSITION, Dumper.MAX_POSITION);

        robot.getDumper().setPosition(dumperPosition);
    }

    private void sweep() {
        //float leftTrigger = gamepad1.left_trigger;
        //float rightTrigger = gamepad1.right_trigger;
        //float totalTrigger = leftTrigger - rightTrigger;

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
            robot.getSweeper().setLiftPower(SWEEPER_LIFT_POWER);
        } else if (gamepad2.a) {
            robot.getSweeper().setLiftPower(-SWEEPER_LIFT_POWER);
        } else {
            robot.getSweeper().setLiftPower(0.0);
        }

        // Sweeps minerals from the floor
        //robot.getSweeper().setSweeperPower(totalTrigger);
        if (gamepad2.x) {
            robot.getSweeper().setSweeperPower(-SWEEPER_POWER);
        } else if (gamepad2.b) {
            robot.getSweeper().setSweeperPower(SWEEPER_POWER);
        } else {
            robot.getSweeper().setSweeperPower(0.0);
        }
    }

    private void push() {
        // Locks or unlocks the pusher which can be moved by the drivers
        if (!lockPusherStateButtonDown && gamepad2.b) {
            lockPusherStateButtonDown = true;
            lockPusherState = !lockPusherState;
        } else if (lockPusherStateButtonDown && !gamepad2.b) {
            lockPusherStateButtonDown = false;
        }

        // If the driver has unlocked pusher, the drive can either pull in or out of the pusher.
        if (!lockPusherState) {
            if (gamepad2.left_stick_y < 0.0f) {
                robot.getPusher().getServoPusher().setPosition(robot.getPusher().getServoPusher().getPosition() - PUSHER_SPEED);
            } else if (gamepad2.left_stick_y > 0.0f) {
                robot.getPusher().getServoPusher().setPosition(robot.getPusher().getServoPusher().getPosition() + PUSHER_SPEED);
            }
        }
    }
}
