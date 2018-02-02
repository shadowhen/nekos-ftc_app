package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.AutoBot;

public class AutoTest extends LinearOpMode {

    private AutoBot robot = new AutoBot();

    private static final double DRIVE_SPEED = 0.3;

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, telemetry);

        waitForStart();

        robot.moveByEncoders(DRIVE_SPEED, 200, 200, 5);
        robot.turn(0.2, 135);

        switch (robot.getJewelColor()) {
            case RED:
                break;
            case BLUE:
                break;
            default:

        }
    }
}
