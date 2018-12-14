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

        // INSTRUCTIONS FOR THIS PROGRAM
        // 1.) Lowers down the robot
        // 2.) Turn left?
        // 3.) Go straight?
        // 4.) Turn right?
        // 5.) Go straight?
        // 6.) Turn right
        // 7.) Scans the gold mineral and drive until gold mineral appears
        // 8.) Finish the driveby
        // 9.) Turn left by 45 degrees
        // 10.) Go straight?
        // 11.) Turn left
        // 12.) Drop the game market
        // 13.) Park

        // Lowers the robot using the lift. WIll add later
        // TODO: Add a function that the robot lowers itself onto the gtound

        robot.turnByGyro(0.4, -90, 2, 5);
        sleep(500);
        robot.moveByEncoder(0.4, 100, 100, 5);
        sleep(500);
        robot.turnByGyro(0.4, 90, 2, 5);
        sleep(500);
        robot.moveByEncoder(0.4, 100, 100, 5);
        sleep(500);
        while (robot.onHeading(0.4, robot.getSensors().getGyro().getIntegratedZValue() + 90, 0.15)) {
            telemetry.addData("Do something", "Going to turn and scan gold minerals");
            telemetry.update();
        }

        // TODO: Implement a function that the robot would drive to its destination.
        // The robot drives to its destination, but the robot also scans for the gold mineral at
        // the same time. If the robot sees a gold mineral, the robot stops and thens the pushes
        // the mineral away to earn some points.

        sleep(500);
        robot.turnByGyro(0.4, -45, 2, 5);
        sleep(500);
        robot.moveByEncoder(0.4, 100, 100, 5);
        sleep(500);
        robot.turnByGyro(0.4, -90, 2, 5);
        sleep(500);
        robot.moveByEncoder(0.4, 500, 500, 5);
        sleep(500);

        // Drop the team marker and be happy. Send help.
    }
}
