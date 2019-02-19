package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.robot.Bot;
import org.firstinspires.ftc.teamcode.robot.DriveBot;

/**
 * This class provides the drive functionality for the driver to drive the robot on the field
 * and during competition.
 * @author Henry
 * @version 1.3
 */
@TeleOp(name = "Drive Op 1.3 - Competition", group = "drive")
public class DriveOp extends OpMode {

    private static final double SWEEPER_LIFT_SPEED = 0.9;

    // Control button for the sideways control on gamepad 1
    private boolean sidewaysControlState = false;
    private boolean sidewaysControlStateButtonDown;

    // Needed for moving lift using encoder counts
    private DriveBot robot;

    @Override
    public void init() {
        // Initializes the robot for driver control
        robot = new DriveBot();
        robot.init(hardwareMap, telemetry);

        // Drive motors will use encoders for setting the speed value
        robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void init_loop() {
        // Tells the user that the robot is ready to start
        telemetry.addData(">", "waiting for START...");
    }

    @Override
    public void start() {
        // Allow the robot's lift and latch to work properly
        robot.getSweeper().setLiftZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    @Override
    public void loop() {
        // These methods represent certain functions on the robot
        drive();
        lift();
        dump();
        slide();
        sweep();

        // Sends the info to the driver station for debugging and important acoustics
        telemetry.addData("Sideways Motion Controls", sidewaysControlState ? "D-Pad" : "Bumpers");

        // DRIVE MOTORS
        telemetry.addLine("-------------------");
        telemetry.addData("Left Motor-Front Power", "%.2f", robot.getMotorDriveLeftFront().getPower());
        telemetry.addData("Left Motor-Rear Power", "%.2f", robot.getMotorDriveLeftRear().getPower());
        telemetry.addData("Right Motor-Front Power", "%.2f", robot.getMotorDriveRightFront().getPower());
        telemetry.addData("Right Motor-Rear Power", "%.2f", robot.getMotorDriveRightRear().getPower());

        // SWEEPER
        telemetry.addLine("-------------------");
        telemetry.addData("Sweeper Power", "%.2f", robot.getSweeper().getSweeperPower());
        telemetry.addData("Sweeper-Lift Power", "%.2f", robot.getSweeper().getLiftPower());
        telemetry.addData("Sweeper-Slider Power", "%.2f", robot.getSweeper().getSliderPower());

        // LIFT
        telemetry.addLine("-------------------");
        telemetry.addData("Lift Power", "%.2f", robot.getLift().getLiftPower());

        // DUMPER
        telemetry.addLine("-------------------");
        //telemetry.addData("Dumper Position", "%.2f",robot.getDumper().getPosition());
        telemetry.addData("Dumper Power", "%.2f", robot.getDumper().getPower());
    }

    /**
     * Drives the robot around
     */
    private void drive() {
        // Get joystick y values from gamepad 1
        float joystickLeftYOne = gamepad1.left_stick_y;
        float joystickRightYOne = gamepad1.right_stick_y;

        // Changes the control layout for turning and sideways motion on bumpers and dpad
        if (!sidewaysControlStateButtonDown && gamepad1.a) {
            sidewaysControlState = !sidewaysControlState;
            sidewaysControlStateButtonDown = true;
        } else if (sidewaysControlStateButtonDown && !gamepad1.a) {
            sidewaysControlStateButtonDown = false;
        }

        // Set the drive power based on the input of gamepad 1
        if (gamepad1.right_bumper) {
            // Move the robot sideways left or turn left depending the sideways state
            if (!sidewaysControlState) {
                robot.getMotorDriveLeftFront().setPower(-Bot.DRIVE_SPEED);
                robot.getMotorDriveLeftRear().setPower(Bot.DRIVE_SPEED);
                robot.getMotorDriveRightFront().setPower(Bot.DRIVE_SPEED);
                robot.getMotorDriveRightRear().setPower(-Bot.DRIVE_SPEED);

            } else {
                robot.setDrivePowerTurn(Bot.TURN_SPEED, Bot.TURN_SPEED);
            }

        } else if (gamepad1.left_bumper) {
            // Move the robot sideways right or turn right using the right bumper
            if (!sidewaysControlState) {
                robot.getMotorDriveLeftFront().setPower(Bot.DRIVE_SPEED);
                robot.getMotorDriveLeftRear().setPower(-Bot.DRIVE_SPEED);
                robot.getMotorDriveRightFront().setPower(-Bot.DRIVE_SPEED);
                robot.getMotorDriveRightRear().setPower(Bot.DRIVE_SPEED);

            } else {
                robot.setDrivePowerTurn(-Bot.TURN_SPEED, -Bot.TURN_SPEED);
            }

        } else if (gamepad1.dpad_up) {
            // Moves the robot forwards
            robot.setDrivePower(-Bot.DRIVE_SPEED, -Bot.DRIVE_SPEED);

        } else if (gamepad1.dpad_down) {
            // Moves the robot backwards
            robot.setDrivePower(Bot.DRIVE_SPEED, Bot.DRIVE_SPEED);

        } else if (gamepad1.dpad_right) {
            // Moves the robots sideways left or turn left using left dpad
            if (sidewaysControlState) {
                robot.getMotorDriveLeftFront().setPower(-Bot.DRIVE_SPEED);
                robot.getMotorDriveLeftRear().setPower(Bot.DRIVE_SPEED);
                robot.getMotorDriveRightFront().setPower(Bot.DRIVE_SPEED);
                robot.getMotorDriveRightRear().setPower(-Bot.DRIVE_SPEED);
            } else {
                robot.setDrivePowerTurn(-Bot.TURN_SPEED, -Bot.TURN_SPEED);
            }

        } else if (gamepad1.dpad_left) {
            // Moves the robots sideways right or turn right using right dpad
            if (sidewaysControlState) {
                robot.getMotorDriveLeftFront().setPower(Bot.DRIVE_SPEED);
                robot.getMotorDriveLeftRear().setPower(-Bot.DRIVE_SPEED);
                robot.getMotorDriveRightFront().setPower(-Bot.DRIVE_SPEED);
                robot.getMotorDriveRightRear().setPower(Bot.DRIVE_SPEED);
            } else {
                robot.setDrivePowerTurn(Bot.TURN_SPEED, Bot.TURN_SPEED);
            }

        } else {
            // Controls the movement of the robot using joysticks on gamepad 1
            // or setting the drive power to zero if no input exists
            robot.setDrivePower(joystickLeftYOne, joystickRightYOne);

        }
    }

    /**
     * Controls the lift
     */
    private void lift() {
        double liftPower = 0.0;

        // Raises or lower the lift using the lander motor
        if (gamepad2.y) {
            liftPower -= Bot.VERTICAL_LIFT_SPEED;
        }
        if (gamepad2.a) {
            liftPower += Bot.VERTICAL_LIFT_SPEED;
        }
        robot.getLift().setLiftPower(liftPower);
    }

    /**
     * Controls the dumper which dumps the minerals
     */
    private void dump() {
        double dumperPower = 0.0;

        // Opens up or down of the dumper
        if (gamepad2.right_trigger != 0) {
            dumperPower += Bot.DUMPER_MOTOR_SPEED;
        }
        if (gamepad2.left_trigger != 0) {
            dumperPower -= Bot.DUMPER_MOTOR_SPEED;
        }
        robot.getDumper().setPower(dumperPower);
    }

    /**
     * Controls the sweeper mechanism on the robot
     */
    private void sweep() {
        double liftPower = 0.0;

        float leftTrigger = gamepad1.left_trigger;
        float rightTrigger = gamepad1.right_trigger;
        float totalTrigger = rightTrigger - leftTrigger;

        // Deploys or retracts the sweeper using the lift motor
        if (gamepad2.dpad_up) {
            liftPower += SWEEPER_LIFT_SPEED;
        }
        if (gamepad2.dpad_down) {
            liftPower -= SWEEPER_LIFT_SPEED;
        }
        if (gamepad2.left_stick_y != 0.0f) {
            liftPower = Range.clip(gamepad2.left_stick_y, -Bot.SWEEPER_LIFT_SPEED, Bot.SWEEPER_LIFT_SPEED);
        }
        robot.getSweeper().setLiftPower(liftPower);

        // Sweeps the minerals from the ground
        robot.getSweeper().setSweeperPower(Range.clip(totalTrigger, -Bot.SWEEPER_SPEED, Bot.SWEEPER_SPEED));
    }

    /**
     * Controls the drawer slider which slides in and out the sweeper.
     */
    private void slide() {
        double sliderPower = 0.0;
        if (gamepad2.x) {
            sliderPower -= Bot.SLIDER_SPEED;
        }
        if (gamepad2.b) {
            sliderPower += Bot.SLIDER_SPEED;
        }
        robot.getSweeper().setSliderPower(sliderPower);
    }
}
