package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.AutoBot;

/**
 * This class allows the robot to drive autonomously without the driver's intervention, and because
 * of the nature of this class, other auto opmodes extends this class for specific autonomous tasks.
 *
 * @author Henry
 * @version 1.0
 */
public class AutoOpMode extends LinearOpMode {

    protected AutoBot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new AutoBot(this);

        robot.init(hardwareMap, telemetry);
    }
}
