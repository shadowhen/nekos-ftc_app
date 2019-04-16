package org.firstinspires.ftc.teamcode.opmodes.auto.delay;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.opmodes.auto.AutoOpMode;
import org.firstinspires.ftc.teamcode.robot.Bot;

/**
 * This class implements autonomous plan Beta which the robot lands from the depot side, drives
 * to the depot to drop the team marker, drives to the crater on the opposite alliance side,
 * and then park on the crater partially.
 *
 * @author Henry
 * @version 1.3
 */
@Autonomous(name = "Beta DELAY - COMPETITION - Depot - Right Side - No Sampling", group = "delay")
public class AutoBetaDelay extends AutoOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        // This line below makes the robot's mechanum wheels to work properly under normal conditions
        robot.setDriveZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        while (!isStarted()) {
            telemetry.addData("INSTRUCTIONS", "PLACE THE HOOK ON THE RIGHT SIDE");
            telemetry.addData(">", "Press START to start autonomous");
            telemetry.update();
        }

        // Lands on the ground by raising the lift
        liftByTime(Bot.VERTICAL_RAISE_SPEED, SLEEP_LANDING);

        // Moves to the left sideways
        robot.moveSidewaysByEncoder(DRIVE_SPEED, -200, 5);

        // Runs through the minerals
        robot.moveByEncoder(DRIVE_SPEED, 1180, 1180, 5);

        // Deposits the team marker in the depot for autonomous points
        setSweeperLiftPower(SWEEPER_DEPLOY_SPEED, 500);
        sleep(1000);
        setSweeperLiftPower(SWEEPER_RETRACT_SPEED, 600);

        // Turns the robot and moves towards the pit for parking points
        robot.moveByEncoder(TURN_SPEED, -740, 740, 5);
        robot.moveByEncoder(DRIVE_SPEED, 1000, 1000, 10);
        robot.moveByEncoder(TURN_SPEED, -100, 100, 5);
        robot.moveByEncoder(DRIVE_SPEED, 1000-150, 1000-150, 10);

        // Parks the robot partially on the crater by deploying its sweeper
        setSweeperLiftPower(SWEEPER_DEPLOY_SPEED, 500);
    }
}
