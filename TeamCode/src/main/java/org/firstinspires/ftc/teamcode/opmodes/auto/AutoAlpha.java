package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * This class expands the AutoOpMode.class to tell the robot to knock off the gold mineral, drop
 * its team marker, and park in one of the spots which gives the team points.
 *
 * @author Henry
 * @version 1.0
 */
@Autonomous(name = "Auto Alpha", group = "auto")
public class AutoAlpha extends AutoOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        waitForStart();
    }
}
