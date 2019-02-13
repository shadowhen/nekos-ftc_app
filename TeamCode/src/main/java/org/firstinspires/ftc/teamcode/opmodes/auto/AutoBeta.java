package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * The autonomous program known as Beta is designed in a way that the robot ignores the mineral
 * and deposits the team marker in the depot. After depositing the team marker, the robot makes
 * its way to the pit for parking bonus. The pit itself is always on the opposite alliance side;
 * therefore, the robot goes the farthest right of the pit. For now, the program is competition
 * ready and has a high chance of success if everything goes smoothly.
 *
 * @author Henry
 * @version 1.1
 */
@Autonomous(name = "Auto Beta - Competition - NO LANDING", group = "auto")
public class AutoBeta extends AutoOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        // This line below makes the robot's mechanum wheels to work properly under normal conditions
        robot.setDriveZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        while (!isStarted()) {
            telemetry.addData(">", "Press START to start encoder drive forward");
            telemetry. update();
        }

        // Moves to the left sideways
        robot.moveSidewaysByEncoder(0.1, -60, 5);
        sleep(1000);

        // Runs through the minerals
        robot.moveByEncoder(DRIVE_SPEED, 1010+200+170, 1010+200+170, 5);
        sleep(1000);

        // Deposits the team marker in the depot for autonomous points
        setSweeperLiftPower(SWEEPER_DEPLOY_SPEED, 500);
        setSweeperLiftPower(SWEEPER_RETRACT_SPEED, 1000);

        // Turns the robot and moves towards the pit for parking points
        robot.moveByEncoder(TURN_SPEED, -740, 740, 5);
        sleep(1000);
        robot.moveByEncoder(DRIVE_SPEED, 1000, 1000, 10);
        robot.moveByEncoder(TURN_SPEED, -100, 100, 5);
        robot.moveByEncoder(DRIVE_SPEED, 1000, 1000, 10);
        setSweeperLiftPower(SWEEPER_DEPLOY_SPEED, 500);
    }
}
