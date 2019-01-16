package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.robot.DriveBot;

public class LiftTestOp extends OpMode {

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

    }
}
