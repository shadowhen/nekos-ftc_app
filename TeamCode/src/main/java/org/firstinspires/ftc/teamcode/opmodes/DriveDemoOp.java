package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.robot.Bot;
import org.firstinspires.ftc.teamcode.robot.DriveBot;

/**
 * This class implements the driving functions of the robot with only one constraint of one driver
 * only. Unlike the competitive drive teleop, this program would have one driver driving the robot.
 * @author Henry
 * @version 1.0
 */
@TeleOp(name = "Drive Demo Op (One Driver Only)", group = "test")
public class DriveDemoOp extends OpMode {

    private static final int CONTROL_DEFAULT = 0;
    private static final int CONTROL_SWEEPER = 1;
    private static final int CONTROL_LIFT = 2;
    private static final int CONTROL_ARM = 3;

    private DriveBot robot;

    private boolean sidewaysMovement = false;
    private boolean sidewaysMovementButtonPressed = false;

    @Override
    public void init() {
        robot = new DriveBot();
        robot.init(hardwareMap, telemetry);
    }

    @Override
    public void init_loop() {
        telemetry.addData(">", "waiting for START");
    }

    @Override
    public void loop() {
        boolean usingDpad = false;
        double liftPower = 0.0;
        double armPower = 0.0;
        double sliderPower = 0.0;
        double deployPower = 0.0;
        double sweeperPower = 0.0;
        int controlNumber = 0;

        // Turns on or off the sideways movement
        if (!sidewaysMovementButtonPressed && gamepad1.a) {
            sidewaysMovementButtonPressed = true;
            sidewaysMovement = !sidewaysMovement;
        } else if (sidewaysMovementButtonPressed && !gamepad1.a) {
            sidewaysMovementButtonPressed = false;
        }

        // MOVEMENT
        if (gamepad1.dpad_up || gamepad1.dpad_down || gamepad1.dpad_left || gamepad1.dpad_right) {
            usingDpad = true;
            if (gamepad1.dpad_up) {
                robot.setDrivePower(Bot.DRIVE_SPEED, Bot.DRIVE_SPEED);
            } else if (gamepad1.dpad_down) {
                robot.setDrivePower(-Bot.DRIVE_SPEED, -Bot.DRIVE_SPEED);
            } else if (sidewaysMovement) {
                if (gamepad1.dpad_left) {
                    robot.setDrivePowerSideways(Bot.DRIVE_SPEED);
                } else if (gamepad1.dpad_right) {
                    robot.setDrivePowerSideways(-Bot.DRIVE_SPEED);
                }
            } else {
                if (gamepad1.dpad_left) {
                    robot.setDrivePowerTurn(Bot.TURN_SPEED);
                } else if (gamepad1.dpad_right) {
                    robot.setDrivePowerTurn(-Bot.TURN_SPEED);
                }
            }
        }

        // Uses the joysticks if no dpad button have been pushed
        if (!usingDpad) {
            if (sidewaysMovement) {
                robot.setDrivePowerSideways(gamepad1.left_stick_x, gamepad1.right_stick_x);
            } else {
                robot.setDrivePower(gamepad1.left_stick_y, gamepad1.right_stick_y);
            }
        }

        // Controls to use various features on the robot
        if (gamepad1.x) {
            // ARM CONTROL
            controlNumber = CONTROL_ARM;

            if (gamepad1.left_trigger > 0) {
                armPower += Bot.DUMPER_MOTOR_SPEED;
            }
            if (gamepad1.right_trigger > 0) {
                armPower -= Bot.DUMPER_MOTOR_SPEED;
            }
        } else if (gamepad1.y) {
            // LIFT CONTROL
            controlNumber = CONTROL_LIFT;

            if (gamepad1.left_trigger > 0) {
                liftPower -= Bot.VERTICAL_LIFT_SPEED;
            }
            if (gamepad1.right_trigger > 0) {
                liftPower += Bot.VERTICAL_LIFT_SPEED;
            }
        } else if (gamepad1.b) {
            // SWEEPER CONTROL
            controlNumber = CONTROL_SWEEPER;

            // Controls the deployment of the sweeper
            deployPower = Range.clip(gamepad1.right_trigger - gamepad1.left_trigger,
                    -Bot.SWEEPER_LIFT_SPEED, Bot.SWEEPER_LIFT_SPEED);

            // Controls the sweeper slider
            if (gamepad1.right_bumper) {
                sliderPower += Bot.SLIDER_SPEED;
            }
            if (gamepad1.left_bumper) {
                sliderPower -= Bot.SLIDER_SPEED;
            }
        } else {
            sweeperPower = Range.clip(gamepad1.right_trigger - gamepad1.left_trigger,
                    -Bot.SWEEPER_SPEED, Bot.SWEEPER_SPEED);
        }

        robot.getDumper().setPower(armPower);
        robot.setLiftPower(liftPower);
        robot.getSweeper().setLiftPower(deployPower);
        robot.getSweeper().setSliderPower(sliderPower);
        robot.getSweeper().setSweeperPower(sweeperPower);

        // Shows controls if the driver presses a button
        telemetry.addLine("Control Mode");
        switch (controlNumber) {
            case CONTROL_DEFAULT:
                telemetry.addLine("Sweeping Minerals");
                telemetry.addData("LT", "OUT");
                telemetry.addData("RT", "IN");
                break;
            case CONTROL_SWEEPER:
                telemetry.addLine("Sweeper Control");
                telemetry.addData("LT", "RETRACT SWEEPER");
                telemetry.addData("RT", "DEPLOY SWEEPER");
                telemetry.addData("LB", "RETRACT SLIDE");
                telemetry.addData("RB", "DEPLOY SLIDE");
                break;
            case CONTROL_LIFT:
                telemetry.addLine("Lift Control");
                telemetry.addData("LT", "LOWER LIFT");
                telemetry.addData("RT", "RAISE LIFT");
                break;
            case CONTROL_ARM:
                telemetry.addLine("Arm Control");
                telemetry.addData("LT", "LOWER ARM");
                telemetry.addData("RT", "RAISER ARM");
                break;
        }
        telemetry.addLine("===============================");

        telemetry.addLine("Controls");
        telemetry.addData("Movement", "Joysticks and Dpad");
        telemetry.addData("A", "Movement - " + (sidewaysMovement ? "Sideways" : "Normal"));
        telemetry.addData("B", "Sweeper Deployment");
        telemetry.addData("X", "Arm Control");
        telemetry.addData("Y", "Lift Control");
    }
}
