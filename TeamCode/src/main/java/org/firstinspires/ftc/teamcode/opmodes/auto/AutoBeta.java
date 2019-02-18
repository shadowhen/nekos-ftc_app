package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.robot.Bot;

/**
 * The autonomous program known as Beta is designed in a way that the robot ignores the mineral
 * and deposits the team marker in the depot. After depositing the team marker, the robot makes
 * its way to the pit for parking bonus. The pit itself is always on the opposite alliance side;
 * therefore, the robot goes the farthest right of the pit. For now, the program is competition
 * ready and has a high chance of success if everything goes smoothly.
 *
 * @author Henry
 * @version 1.2
 */
@Autonomous(name = "Auto Beta - COMPETITION - DEPOT - RIGHT SIDE - LANDING - NO MINERALS", group = "auto")
public class AutoBeta extends AutoOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        // This line below makes the robot's mechanum wheels to work properly under normal conditions
        robot.setDriveZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        while (!isStarted()) {
            telemetry.addData("INSTRUCTIONS", "PLACE THE HOOK ON THE RIGHT SIDE");
            telemetry.addData(">", "Press START to start autonomous");
            telemetry. update();
        }

        // Lands on the ground by raising the lift
        liftByTime(Bot.VERTICAL_RAISE_SPEED, 3100);

        // Moves to the left sideways
        robot.moveSidewaysByEncoder(DRIVE_SPEED, -200, 5);
        sleep(1000);

        // Runs through the minerals
        robot.moveByEncoder(DRIVE_SPEED, 1180, 1180, 5);

        sleep(1000);

        // Deposits the team marker in the depot for autonomous points
        setSweeperLiftPower(SWEEPER_DEPLOY_SPEED, 500);
        sleep(1000);
        setSweeperLiftPower(SWEEPER_RETRACT_SPEED, 600);

        // Turns the robot and moves towards the pit for parking points
        robot.moveByEncoder(TURN_SPEED, -740, 740, 5);
        sleep(1000);

        robot.moveByEncoder(DRIVE_SPEED, 1000, 1000, 10);
        robot.moveByEncoder(TURN_SPEED, -100, 100, 5);
        robot.moveByEncoder(DRIVE_SPEED, 1000-150, 1000-150, 10);
        setSweeperLiftPower(SWEEPER_DEPLOY_SPEED, 500);
    }
}
