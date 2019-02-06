package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * The class is based on the AutoBeta class with the robot being able to land on the ground from
 * the lander. Unlike AutoBeta, the class implements the landing in the autonomous, making this
 * class more viable for autonomous period and alliance matching.
 *
 * @author Henry
 * @version 1.0
 */
@Autonomous(name = "Auto Charlie", group = "auto")
@Disabled
public class AutoCharlie extends AutoOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        // No reason for this line but commenting lines below may affect the chances of success
        robot.setDriveZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // The robot would hold on the lander
        robot.getLift().getLiftMotor().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        while (!isStarted()) {
            telemetry.addData(">", "Press START to start encoder drive forward");
            telemetry. update();
        }

        robot.getLift().getLiftMotor().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        // Lower the robot onto the floor from the lander
        robot.moveLiftByDistance(0.5, 1000, 5);

        // Moves to the left sideways
        robot.moveSidewaysByEncoder(0.1, -60, 5);
        sleep(1000);

        // Runs through the minerals
        robot.moveByEncoder(DRIVE_SPEED, 1010, 1010, 5);
        sleep(1000);

        // Deposits the team marker in the depot for autonomous points
        setSweeperLiftPower(-0.5, 500);
        setSweeperLiftPower(0.5, 1000);

        // Turns the robot and moves towards the pit for parking points
        robot.moveByEncoder(TURN_SPEED, -740, 740, 5);
        sleep(1000);
        robot.moveByEncoder(DRIVE_SPEED, 1500, 1500, 10);
        setSweeperLiftPower(-0.5, 500);
    }
}
