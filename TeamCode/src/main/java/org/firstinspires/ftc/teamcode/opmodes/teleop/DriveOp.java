package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.DriveBot;

@TeleOp(name = "Drive Mode")
public class DriveOp extends OpMode {

    private DriveBot robot;

    @Override
    public void init() {
        telemetry.log().add("Initializing...");
        robot = new DriveBot(hardwareMap, telemetry);
        telemetry.log().add("Initialization completed.");

        telemetry.addData(">", "Robot is ready.");
        telemetry.update();
    }

    @Override
    public void start() {
        telemetry.log().clear();
    }

    @Override
    public void loop() {
        robot.setDrivePower(-gamepad1.left_stick_y, -gamepad1.right_stick_y);

        telemetry.addData("Drive Power", robot.getDrivePower());
    }
}
