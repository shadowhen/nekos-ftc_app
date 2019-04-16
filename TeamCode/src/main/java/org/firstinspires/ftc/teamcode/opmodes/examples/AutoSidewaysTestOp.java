package org.firstinspires.ftc.teamcode.opmodes.examples;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.ExpermientBot;

@Autonomous(group = "test")
public class AutoSidewaysTestOp extends LinearOpMode {

    private ExpermientBot robot;

    private double speed = 0.5;
    private double distance = 500;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new ExpermientBot(this);
        robot.init(hardwareMap, telemetry);

        while (!isStarted()) {
            telemetry.addData(">", "waiting to start...");
            telemetry.update();
        }

        robot.moveSidewaysByEncoder(speed, distance, 5);
        sleep(1000);
        robot.moveSidewaysByEncoder(speed, -distance, 5);

        /*robot.moveSideways(speed, speed, speed, speed, distance, 5);
        sleep(1000);
        robot.moveSideways(speed, speed, speed, speed, -distance, 5);*/

    }
}
