package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.AutoBot;

public abstract class BaseAutoOp extends LinearOpMode {

    protected AutoBot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.log().add("Initializing...");

        robot = new AutoBot();
        robot.init(hardwareMap, telemetry);

        // Inform user that the robot is ready to start
        telemetry.log().add("Initialization completed.");
        telemetry.addData(">", "Robot is ready.");
        telemetry.update();

        waitForStart();
        telemetry.log().clear();
    }
}
