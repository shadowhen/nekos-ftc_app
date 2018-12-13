package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.robot.DriveBot;

/**
 * This class allows the drivers to calibrate the drive motors' encoders.
 *
 * @author Henry
 * @version 1.0
 */
@TeleOp(name = "Calibrate Encoders - X to reset", group = "test")
public class CalibrateEncoderOp extends LinearOpMode {

    private DriveBot robot;
    private boolean resetEncoderButtonDown;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new DriveBot();
        robot.init(hardwareMap, telemetry);

        robot.setDriveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData(">", "Ready to start.");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.x && !resetEncoderButtonDown) {
                resetEncoderButtonDown = true;
                robot.setDriveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
            } else if (!gamepad1.x && resetEncoderButtonDown) {
                resetEncoderButtonDown = false;
            }

            robot.setDrivePower(gamepad1.left_stick_y, gamepad1.right_stick_y);

            telemetry.addData("Reset Status", resetEncoderButtonDown ? "Resetting... Let go the button." : "Done");
            telemetry.addData("Joystick Y", "%.2f %.2f", gamepad1.left_stick_y, gamepad1.right_stick_y);
            telemetry.addData("Left Current Pos", "%d %d",
                    robot.getMotorDriveLeftFront().getCurrentPosition(),
                    robot.getMotorDriveLeftRear().getCurrentPosition());
            telemetry.addData("Right Current Pos", "%d %d",
                    robot.getMotorDriveRightFront().getCurrentPosition(),
                    robot.getMotorDriveRightRear().getCurrentPosition());
            telemetry.update();
        }
    }
}
