package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.robot.DriveBot;
import org.firstinspires.ftc.teamcode.robot.Dumper;

/**
 * This class provides the drive functionality for the driver to drive the robot on the field
 * and during competition.
 * @author Henry
 * @version 1.2
 */
@TeleOp(name = "Drive Op 1.2 - Competition", group = "drive")
public class DriveOp extends OpMode {

    private static final double DRIVE_SPEED = 0.5;
    private static final double DRIVE_SIDEWAYS_SPEED = 0.8;
    private static final double TURN_SPEED = 0.8;

    // Speed for sideways motion
    // Note: This setting is used if the robot is unbalanced, but the builders might want to
    // look at the robot anf find the center of mass.
    public static final double SIDEWAYS_LF_SPEED = 0.8;
    public static final double SIDEWAYS_LR_SPEED = 0.8;
    public static final double SIDEWAYS_RF_SPEED = 0.8;
    public static final double SIDEWAYS_RR_SPEED = 0.8;

    private static final double LIFT_POWER = 0.5;
    private static final double SWEEPER_POWER = 0.5;
    private static final double SWEEPER_LIFT_POWER = 0.45;

    // Speed of the dumper's servo
    private static final double DUMPER_SPEED = 0.002;

    // Position of the dumper's eervo
    private double dumperPosition = 0.5;

    private boolean sidewaysState = false;
    private boolean sidewaysStateButtonDown;

    private DriveBot robot;

    @Override
    public void init() {
        robot = new DriveBot();
        robot.init(hardwareMap, telemetry);

        // Drive motors will use encoders for setting the speed value
        robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.getLift().setLiftPower(0.0);

        // Gets the current position of the server dumper, so the dumper position can match with
        // the servo dumper's position and adjust it when the driver adjusts the position from
        // the gamepads.
        dumperPosition = robot.getDumper().getServoDumper().getPosition();
    }

    @Override
    public void init_loop() {
        // Tells the user that the robot is ready to start
        telemetry.addData(">", "waiting for START...");
        telemetry.addData("sweeper lift power", robot.getLift().getLiftMotor().getPower());
        telemetry.addData("position", robot.getDumper().getServoDumper().getPosition());
        telemetry.update();
    }

    @Override
    public void start() {
        robot.getSweeper().getLiftMotor().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        robot.getLift().getLanderMotor().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void loop() {
        drive();
        lift();
        dump();
        sweep();

        // Sends the info to the driver station for debugging and important acoustics
        telemetry.addData("Sideways Motion Controls", sidewaysState ? "Dpad" : "Bumpers");
        telemetry.addLine("-------------------");
        telemetry.addData("Left Motor-Front Power", "%.2f", robot.getMotorDriveLeftFront().getPower());
        telemetry.addData("Left Motor-Rear Power", "%.2f", robot.getMotorDriveLeftRear().getPower());
        telemetry.addData("Right Motor-Front Power", "%.2f", robot.getMotorDriveRightFront().getPower());
        telemetry.addData("Right Motor-Rear Power", "%.2f", robot.getMotorDriveRightRear().getPower());
        telemetry.addLine("-------------------");
        telemetry.addData("Sweeper Power", "%.2f", robot.getSweeper().getSweeperPower());
        telemetry.addData("Sweeper-Lift Power", "%.2f", robot.getSweeper().getLiftPower());
        telemetry.addLine("-------------------");
        telemetry.addData("Lift Power", "%.2f", robot.getLift().getLiftPower());
        telemetry.addData("Lander Power", "%.2f", robot.getLift().getLanderPower());
        telemetry.addLine("-------------------");
        telemetry.addData("Dumper Position", "%.2f",robot.getDumper().getServoDumper().getPosition());
        telemetry.update();
    }

    /**
     * Drives the robot around
     */
    private void drive() {
        // Get joystick y values from gamepad 1
        float joystickLeftYOne = gamepad1.left_stick_y;
        float joystickRightYOne = gamepad1.right_stick_y;

        // Changes the control layout for turning and sideways motion on bumpers and dpad
        if (!sidewaysStateButtonDown && gamepad1.a) {
            sidewaysState = !sidewaysState;
            sidewaysStateButtonDown = true;
        } else if (sidewaysStateButtonDown && !gamepad1.a) {
            sidewaysStateButtonDown = false;
        }

        // Set the drive power based on the input of gamepad 1
        if (gamepad1.left_bumper) {
            // Move the robot sideways left or turn left using the left bumper
            if (!sidewaysState) {
                robot.getMotorDriveLeftFront().setPower(-SIDEWAYS_LF_SPEED);
                robot.getMotorDriveLeftRear().setPower(SIDEWAYS_LR_SPEED);
                robot.getMotorDriveRightFront().setPower(SIDEWAYS_RF_SPEED);
                robot.getMotorDriveRightRear().setPower(-SIDEWAYS_RR_SPEED);

            } else {
                robot.setDrivePowerTurn(TURN_SPEED, TURN_SPEED);
            }

        } else if (gamepad1.right_bumper) {
            // Move the robot sideways right or turn right using the right bumper
            if (!sidewaysState) {
                robot.getMotorDriveLeftFront().setPower(SIDEWAYS_LF_SPEED);
                robot.getMotorDriveLeftRear().setPower(-SIDEWAYS_LR_SPEED);
                robot.getMotorDriveRightFront().setPower(-SIDEWAYS_RF_SPEED);
                robot.getMotorDriveRightRear().setPower(SIDEWAYS_RR_SPEED);

            } else {
                robot.setDrivePowerTurn(-TURN_SPEED, -TURN_SPEED);
            }

        } else if (gamepad1.dpad_up) {
            // Moves the robot forwards using dpad up
            robot.setDrivePower(-DRIVE_SPEED, -DRIVE_SPEED);

        } else if (gamepad1.dpad_down) {
            // Moves the robot backwards using dpad down
            robot.setDrivePower(DRIVE_SPEED, DRIVE_SPEED);

        } else if (gamepad1.dpad_left) {
            // Moves the robots sideways left or turn left using left dpad
            if (sidewaysState) {
                robot.getMotorDriveLeftFront().setPower(-SIDEWAYS_LF_SPEED);
                robot.getMotorDriveLeftRear().setPower(SIDEWAYS_LR_SPEED);
                robot.getMotorDriveRightFront().setPower(SIDEWAYS_RF_SPEED);
                robot.getMotorDriveRightRear().setPower(-SIDEWAYS_RR_SPEED);
            } else {
                robot.setDrivePowerTurn(-TURN_SPEED, -TURN_SPEED);
            }

        } else if (gamepad1.dpad_right) {
            // Moves the robots sideways right or turn right using right dpad
            if (sidewaysState) {
                robot.getMotorDriveLeftFront().setPower(SIDEWAYS_LF_SPEED);
                robot.getMotorDriveLeftRear().setPower(-SIDEWAYS_LR_SPEED);
                robot.getMotorDriveRightFront().setPower(-SIDEWAYS_RF_SPEED);
                robot.getMotorDriveRightRear().setPower(SIDEWAYS_RR_SPEED);
            } else {
                robot.setDrivePowerTurn(TURN_SPEED, TURN_SPEED);
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
        // Raises or lowers the lift of the robot using lift motor attached to the lift
        if (gamepad2.a) {
            // Lowers the lift
            robot.getLift().setLiftPower(-LIFT_POWER);

        } else if (gamepad2.y) {
            // Raises the lift
            robot.getLift().setLiftPower(LIFT_POWER);

        } else {
            // Zeros the power of the lift if the driver does not press the lift button
            robot.getLift().setLiftPower(0.0);

        }

        // Raises or lower the lift using the lander motor
        if (gamepad2.x) {
            robot.getLift().setLanderPower(-LIFT_POWER);
        } else if (gamepad2.b) {
            robot.getLift().setLanderPower(LIFT_POWER);
        } else {
            robot.getLift().setLanderPower(0.0);
        }
    }

    /**
     * Controls the dumper which dumps the minerals
     */
    private void dump() {
        // Get both trigger values from the gamepad and get difference between the trigger values
        float leftTrigger = gamepad2.left_trigger;
        float rightTrigger = gamepad2.right_trigger;
        float totalTrigger = leftTrigger - rightTrigger;

        // Increments or decrements the dumper's servo position
        if (totalTrigger < -0.1f) {
            dumperPosition = dumperPosition + DUMPER_SPEED;
        } else if (totalTrigger > 0.1f) {
            dumperPosition = dumperPosition - DUMPER_SPEED;
        }
        dumperPosition = Range.clip(dumperPosition, Dumper.MIN_POSITION, Dumper.MAX_POSITION);

        // Opens up or down of the dumper
        robot.getDumper().setPosition(dumperPosition);
    }

    /**
     * Controls the sweeper mechanism on the robot
     */
    private void sweep() {
        float leftTrigger = gamepad1.left_trigger;
        float rightTrigger = gamepad1.right_trigger;
        float totalTrigger = rightTrigger - leftTrigger;

        // Raises the sweeper up or lowers the sweeper down using gamepad 2 joystick
        if (gamepad2.dpad_up) {
            robot.getSweeper().setLiftPower(SWEEPER_LIFT_POWER);
        } else if (gamepad2.dpad_down) {
            robot.getSweeper().setLiftPower(-SWEEPER_LIFT_POWER);
        } else {
            robot.getSweeper().setLiftPower(Range.clip(gamepad2.left_stick_y, -SWEEPER_LIFT_POWER, SWEEPER_LIFT_POWER));
        }

        // Sweeps minerals from the floor using gamepad 1 buttons
        robot.getSweeper().setSweeperPower(Range.clip(totalTrigger, -SWEEPER_POWER, SWEEPER_POWER));
    }
}
