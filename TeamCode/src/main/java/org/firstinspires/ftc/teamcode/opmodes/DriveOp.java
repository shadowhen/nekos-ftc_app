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

    // Sweeper Time
    private static final double DEPLOY_TIME = 0.5;
    private static final double RETRACT_TIME = 0.6;

    // Control button for the sideways control on gamepad 1
    private boolean sidewaysControlState = false;
    private boolean sidewaysControlStateButtonDown;

    // Needed for moving lift using encoder counts
    private DriveBot robot;

    private ElapsedTime sweeperDeployTimer = new ElapsedTime();
    private double sweeperTime = 0.0;
    private boolean sweeperBusy = false;

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
    public void start() {
        // Allow the robot's lift and latch to work properly
        robot.getSweeper().setLiftZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    @Override
    public void loop() {
        drive();
        lift();
        dump();
        slide();
        sweep();

        // Sends the info to the driver station for debugging and important acoustics
        telemetry.addData("Sideways Motion Controls", sidewaysControlState ? "D-Pad" : "Bumpers");
        telemetry.addData("sweeper deploy/retract time", "%.2f", sweeperTime);

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
        float leftTrigger = gamepad1.left_trigger;
        float rightTrigger = gamepad1.right_trigger;
        float totalTrigger = rightTrigger - leftTrigger;

        // Deploys or retracts the sweeper using the lift motor
        //
        // If the driver presses one of the buttons, the sweeper would deploy or retracts
        // with little effort on the driver's side. The deployment or retractment takes
        // time, so the sweeper lift motor would be "busy" for the time being.
        //
        // After the time is up, the sweeper lift motor would set to power of zero which
        // stops the motor
        if (sweeperBusy && (sweeperTime > sweeperDeployTimer.seconds())) {
            robot.getSweeper().setLiftPower(0.0);
            sweeperBusy = false;
            sweeperTime = 0.0;
        } else {
            if (gamepad2.right_bumper && (gamepad2.a || gamepad2.b)) {
                sweeperDeployTimer.reset();
                if (gamepad2.a && !gamepad2.b) {
                    sweeperTime = DEPLOY_TIME;
                    robot.getSweeper().setLiftPower(Bot.SWEEPER_LIFT_SPEED);
                    sweeperBusy = true;
                } else if (!gamepad2.a && gamepad2.b) {
                    sweeperTime = RETRACT_TIME;
                    robot.getSweeper().setLiftPower(-Bot.SWEEPER_LIFT_SPEED);
                    sweeperBusy = true;
                }
            } else {
                robot.getSweeper().setLiftPower(Range.clip(gamepad2.left_stick_y, -Bot.SWEEPER_LIFT_SPEED, Bot.SWEEPER_LIFT_SPEED));
            }
        }

        // Sweeps the minerals from the ground
        robot.getSweeper().setSweeperPower(Range.clip(totalTrigger, -Bot.SWEEPER_SPEED, Bot.SWEEPER_SPEED));
    }

    /**
     * Controls the drawer slider which slides in and out the sweeper.
     */
    private void slide() {
        double sliderPower = 0.0;
        if (gamepad2.dpad_up) {
            sliderPower -= Bot.SLIDER_SPEED;
        }
        if (gamepad2.dpad_down) {
            sliderPower += Bot.SLIDER_SPEED;
        }
        robot.getSweeper().setSliderPower(sliderPower);
    }
}
