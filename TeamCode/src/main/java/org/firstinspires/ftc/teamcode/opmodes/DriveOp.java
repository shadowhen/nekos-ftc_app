package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
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

    private DriveBot robot;

    // Control button for the sideways control on gamepad 1
    private boolean sidewaysControlState = false;
    private boolean sidewaysControlStateButtonDown;

    private boolean sweepingButtonDown = false;
    private boolean sweepingForever = false;

    private boolean sweepingDirectionChangeButtonDown = false;
    private boolean sweepingForward = true;
    
    private double[] driveSpeed = new double[4];

    @Override
    public void init() {
        // Initializes the robot for driver control
        robot = new DriveBot();
        robot.init(hardwareMap, telemetry);

        // Rather set the drive motors with direct power, the drive motors would set according
        // to a speed value
        robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void init_loop() {
        // Tells the user that the robot is ready to start
        telemetry.addData(">", "waiting for START...");
    }

    @Override
    public void loop() {
        drive();
        lift();
        dump();
        slide();
        sweep();

        // Sends the info to the driver station for debugging and important information
        telemetry.addData("Sideways Motion Controls", sidewaysControlState ? "D-Pad" : "Bumpers");

        // DRIVE MOTORS
        telemetry.addLine("-------------------");
        telemetry.addData("Drive Power (LF, LR, RF, RR)", robot.getDrivePower());

        // SWEEPER
        telemetry.addLine("-------------------");
        telemetry.addData("Sweeper Power", "%.2f", robot.getSweeper().getSweeperPower());
        telemetry.addData("Sweeper-Lift Power", "%.2f", robot.getSweeper().getLiftPower());
        telemetry.addData("Sweeper-Slider Power", "%.2f", robot.getSweeper().getSliderPower());

        telemetry.addData("Sweeping Direction", sweepingForward ? "Forward" : "Backward");
        telemetry.addData("Sweeping Forever", sweepingForever);

        // LIFT
        telemetry.addLine("-------------------");
        telemetry.addData("Lift Power", "%.2f", robot.getLift().getLiftPower());

        // DUMPER
        telemetry.addLine("-------------------");
        //telemetry.addData("Dumper Position", "%.2f",robot.getDumper().getPosition());
        telemetry.addData("Arm Power", "%.2f", robot.getDumper().getPower());
    }

    /**
     * Drives the robot around
     */
    private void drive() {
        // Get joystick y values from gamepad 1
        float joystickLeftYOne = gamepad1.left_stick_y;
        float joystickRightYOne = gamepad1.right_stick_y;

        double leftFrontPower = Range.clip(joystickLeftYOne, -Bot.LEFT_FRONT_POWER, Bot.LEFT_FRONT_POWER);
        double leftRearPower = Range.clip(joystickLeftYOne, -Bot.LEFT_REAR_POWER, Bot.LEFT_REAR_POWER);
        double rightFrontPower = Range.clip(joystickRightYOne, -Bot.RIGHT_FRONT_POWER, Bot.RIGHT_FRONT_POWER);
        double rightRearPower = Range.clip(joystickRightYOne, -Bot.RIGHT_REAR_POWER, Bot.RIGHT_REAR_POWER);

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
                leftFrontPower = -Bot.LEFT_FRONT_POWER;
                leftRearPower = Bot.LEFT_REAR_POWER;
                rightFrontPower = Bot.RIGHT_FRONT_POWER;
                rightRearPower = -Bot.RIGHT_REAR_POWER;

            } else {
                leftFrontPower = -Bot.LEFT_FRONT_POWER;
                leftRearPower = -Bot.LEFT_REAR_POWER;
                rightFrontPower = Bot.RIGHT_FRONT_POWER;
                rightRearPower = Bot.RIGHT_REAR_POWER;
            }

        } else if (gamepad1.left_bumper) {
            // Move the robot sideways right or turn right using the right bumper
            if (!sidewaysControlState) {
                leftFrontPower = Bot.LEFT_FRONT_POWER;
                leftRearPower = -Bot.LEFT_REAR_POWER;
                rightFrontPower = -Bot.RIGHT_FRONT_POWER;
                rightRearPower = Bot.RIGHT_REAR_POWER;

            } else {
                leftFrontPower = Bot.LEFT_FRONT_POWER;
                leftRearPower = Bot.LEFT_REAR_POWER;
                rightFrontPower = -Bot.RIGHT_FRONT_POWER;
                rightRearPower = -Bot.RIGHT_REAR_POWER;
            }

        } else if (gamepad1.dpad_up) {
            // Moves the robot forwards
            leftFrontPower = -Bot.LEFT_FRONT_POWER;
            leftRearPower = -Bot.LEFT_REAR_POWER;
            rightFrontPower = -Bot.RIGHT_FRONT_POWER;
            rightRearPower = -Bot.RIGHT_REAR_POWER;

        } else if (gamepad1.dpad_down) {
            // Moves the robot backwards
            leftFrontPower = Bot.LEFT_FRONT_POWER;
            leftRearPower = Bot.LEFT_REAR_POWER;
            rightFrontPower = Bot.RIGHT_FRONT_POWER;
            rightRearPower = Bot.RIGHT_REAR_POWER;

        } else if (gamepad1.dpad_right) {
            // Moves the robots sideways left or turn left using left dpad
            if (sidewaysControlState) {
                leftFrontPower = Bot.LEFT_FRONT_POWER;
                leftRearPower = -Bot.LEFT_REAR_POWER;
                rightFrontPower = -Bot.RIGHT_FRONT_POWER;
                rightRearPower = Bot.RIGHT_REAR_POWER;
            } else {
                leftFrontPower = Bot.LEFT_FRONT_POWER;
                leftRearPower = Bot.LEFT_REAR_POWER;
                rightFrontPower = -Bot.RIGHT_FRONT_POWER;
                rightRearPower = -Bot.RIGHT_REAR_POWER;
            }

        } else if (gamepad1.dpad_left) {
            // Moves the robots sideways right or turn right using right dpad
            if (sidewaysControlState) {
                leftFrontPower = Bot.LEFT_FRONT_POWER;
                leftRearPower = -Bot.LEFT_REAR_POWER;
                rightFrontPower = -Bot.RIGHT_FRONT_POWER;
                rightRearPower = Bot.RIGHT_REAR_POWER;
            } else {
                leftFrontPower = -Bot.LEFT_FRONT_POWER;
                leftRearPower = -Bot.LEFT_REAR_POWER;
                rightFrontPower = Bot.RIGHT_FRONT_POWER;
                rightRearPower = Bot.RIGHT_REAR_POWER;
            }

        }

        robot.setDrivePower(leftFrontPower, leftRearPower, rightFrontPower, rightRearPower);
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
        float totalTrigger = gamepad1.right_trigger - gamepad1.left_trigger;

        // Lifts the sweeper up or down
        robot.getSweeper().setLiftPower(Range.clip(gamepad2.left_stick_y, -Bot.SWEEPER_LIFT_SPEED, Bot.SWEEPER_LIFT_SPEED));

        // Activate or deactivate sweeping forever on the sweeper forever
        if (!sweepingButtonDown && gamepad1.b) {
            sweepingButtonDown = true;
            sweepingForever = !sweepingForever;
        } else if (sweepingButtonDown && !gamepad1.b) {
            sweepingButtonDown = false;
        }

        // Switches the direction of the sweeper
        if (!sweepingDirectionChangeButtonDown && gamepad1.y) {
            sweepingDirectionChangeButtonDown = true;
            sweepingForward = !sweepingForward;
        } else if (sweepingDirectionChangeButtonDown && !gamepad1.y) {
            sweepingDirectionChangeButtonDown = false;
        }

        // Sweeps the minerals from the ground
        if (sweepingForever) {
            robot.getSweeper().setSweeperPower(sweepingForward ? Bot.SWEEPER_SPEED : -Bot.SWEEPER_SPEED);
        } else {
            robot.getSweeper().setSweeperPower(Range.clip(totalTrigger, -Bot.SWEEPER_SPEED, Bot.SWEEPER_SPEED));
        }
    }

    /**
     * Controls the drawer slider which slides in and out the sweeper.
     */
    private void slide() {
        double sliderPower = 0.0;

        // Slides the horizontal slider in or out
        if (gamepad2.dpad_up) {
            sliderPower -= Bot.SLIDER_SPEED;
        }
        if (gamepad2.dpad_down) {
            sliderPower += Bot.SLIDER_SPEED;
        }
        robot.getSweeper().setSliderPower(sliderPower);
    }
}
