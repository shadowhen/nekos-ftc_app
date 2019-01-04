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
 * @version 1.0
 */
@Autonomous(name = "Auto Beta - Competition", group = "auto")
public class AutoBeta extends AutoOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        // No reason for this line but commenting lines below may affect the chances of success
        robot.setDriveZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        robot.getLift().getLiftMotor().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.getLift().getLanderMotor().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        while (!isStarted()) {
            telemetry.addData(">", "Press START to start encoder drive forward");
            telemetry. update();
        }

        robot.getLift().getLiftMotor().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        robot.getLift().getLanderMotor().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        // Lower the robot onto the floor from the lander
        setLiftPower(0.5, 1000);
        sleep(500);

        // Moves to the left sideways
        robot.moveSidewaysByEncoder(0.1, -60, 5);
        sleep(1000);

        // Runs through the minerals
        robot.moveByEncoder(DRIVE_SPEED, 1010, 1010, 5);
        sleep(1000);

        // Deposits the team marker in the depot for autonomous points
        setSweeperLiftPower(0.5, 500);
        setSweeperLiftPower(-0.5, 500);

        // Turns the robot and moves towards the pit for parking points
        robot.moveByEncoder(TURN_SPEED, -740, 740, 5);
        sleep(1000);
        robot.moveByEncoder(DRIVE_SPEED, 1500, 1500, 10);
        setSweeperLiftPower(0.5, 500);
    }
}
