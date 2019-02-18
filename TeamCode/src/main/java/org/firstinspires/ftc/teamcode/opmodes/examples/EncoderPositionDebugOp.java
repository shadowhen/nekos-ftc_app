package org.firstinspires.ftc.teamcode.opmodes.examples;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.robot.DriveBot;

/**
 * THis class implements the drive bot to debug the encoder positions for autonomous paths.
 *
 * @author Henry
 * @version 1.0
 */
@TeleOp(name = "Encoder Position Debugging", group = "test")
@Disabled
public class EncoderPositionDebugOp extends LinearOpMode {

    private static final double DRIVE_SPEED = 0.4;
    private static final double TURN_SPEED = 0.4;
    private static final double SIDEWAYS_SPEED = 0.4;

    private DriveBot robot;
    private boolean resetButtonDown;

    @Override
    public void runOpMode() {
        robot = new DriveBot();
        robot.init(hardwareMap, telemetry);

        // The robot will use encoders for the driving portion
        robot.setDriveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Equivalent to waitForStart()
        while (!isStarted()) {
            telemetry.addData(">", "waiting for start");
            telemetry.update();
        }

        // Loop until the driver presses STOP
        while (opModeIsActive()) {
            // THe driver controls the robot using the gamepads
            drive();

            // Reset the encoders if the button is pressed down
            if (gamepad1.a && !resetButtonDown) {
                resetButtonDown = true;
                robot.setDriveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
            } else if (!gamepad1.a && resetButtonDown) {
                resetButtonDown = false;
            }

            // Send telemetry data to the driver station for debugging and statistics
            telemetry.addData("Controls", "Reset Button - A");
            telemetry.addData("Left Motors' Power", "%.2f %.2f",
                    robot.getMotorDriveLeftFront().getPower(),
                    robot.getMotorDriveLeftRear().getPower());
            telemetry.addData("Right Motors' Power", "%.2f %.2f",
                    robot.getMotorDriveRightFront().getPower(),
                    robot.getMotorDriveRightRear().getPower());
            telemetry.addData("Left Motors' Position", "%07d %07d",
                    robot.getMotorDriveLeftFront().getCurrentPosition(),
                    robot.getMotorDriveLeftRear().getCurrentPosition());
            telemetry.addData("Right Motors' Position", "%07d %07d",
                    robot.getMotorDriveRightFront().getCurrentPosition(),
                    robot.getMotorDriveRightRear().getCurrentPosition());
            telemetry.update();
        }
    }

    /**
     * Drives the robot using the gamepad controls
     */
    private void drive() {
        float joystickLeftYOne = gamepad1.left_stick_y;
        float joystickRightYOne = gamepad1.right_stick_y;

        robot.setDrivePower(joystickLeftYOne, joystickRightYOne);

        if (gamepad1.dpad_up) {
            robot.setDrivePower(DRIVE_SPEED, DRIVE_SPEED);
        }
        if (gamepad1.dpad_down) {
            robot.setDrivePower(-DRIVE_SPEED, -DRIVE_SPEED);
        }
        if (gamepad1.dpad_left) {
            robot.setDrivePower(-TURN_SPEED, TURN_SPEED);
        }
        if (gamepad1.dpad_right) {
            robot.setDrivePower(TURN_SPEED, -TURN_SPEED);
        }
        if (gamepad1.left_bumper) {
            robot.setDrivePowerSideways(-SIDEWAYS_SPEED, -SIDEWAYS_SPEED);
        }
        if (gamepad1.right_bumper) {
            robot.setDrivePowerSideways(SIDEWAYS_SPEED, SIDEWAYS_SPEED);
        }
    }
}
