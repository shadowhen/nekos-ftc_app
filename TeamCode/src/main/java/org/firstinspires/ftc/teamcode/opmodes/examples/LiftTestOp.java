package org.firstinspires.ftc.teamcode.opmodes.examples;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.robot.DriveBot;

@TeleOp(name = "LIft Test", group = "test")
public class LiftTestOp extends OpMode {

    private static final double LIFT_SPEED = 0.5;

    private DriveBot robot;

    private boolean resetButtonDown;

    @Override
    public void init() {
        robot = new DriveBot();
        robot.init(hardwareMap, telemetry);

        // The lift motor will use encoder for this test
        robot.getLift().getLiftMotor().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.getLift().getLiftMotor().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void init_loop() {
        telemetry.addData(">", "waiting for start...");
        telemetry.update();
    }

    @Override
    public void loop() {
        if (gamepad1.x && !resetButtonDown) {
            resetButtonDown = true;

            // Reset the encoder position on hte lift motor
            robot.getLift().getLiftMotor().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.getLift().getLiftMotor().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        } else if (!gamepad1.x && resetButtonDown) {
            resetButtonDown = false;
        }

        // Controls the lift by raising or lowering using the buttons
        if (gamepad1.a && !gamepad1.b) {
            robot.setLiftPower(-LIFT_SPEED);
        } else if (!gamepad1.a && gamepad1.b) {
            robot.setLiftPower(LIFT_SPEED);
        } else {
            robot.setLiftPower(0.0);
        }

        if (gamepad1.dpad_up && !gamepad1.dpad_down) {
            robot.getDumper().setPower(0.25);
        } else if (!gamepad1.dpad_up && gamepad1.dpad_down) {
            robot.getDumper().setPower(-0.25);
        } else {
            robot.getDumper().setPower(0.0);
        }

        telemetry.addData(">", "To reset position - press X");
        telemetry.addData("A - Raises lift", "B - Lowers lift");
        telemetry.addData("Up - Raises the arm", "Down - Lowers the arm");
        telemetry.addData("lift power", robot.getLift().getLiftPower());
        telemetry.addData("lift position", robot.getLift().getLiftMotor().getCurrentPosition());
        telemetry.update();
    }
}
