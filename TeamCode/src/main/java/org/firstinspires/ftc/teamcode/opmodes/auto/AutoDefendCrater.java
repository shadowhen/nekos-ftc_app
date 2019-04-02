package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.robot.MineralType;

/**
 * This class implements the autonomous plan which the robot plays defensively by
 * first sampling the center and right of the crater from the robot's perspective and
 * then park on the right side of the crater to prevent from the opposing robots from parking.
 * @author Henry
 * @version 1.0
 */
@Autonomous(name = "Auto Defend Crater - CRATER - LEFT SIDE - SAMPLE MIDDLE & RIGHT")
@Disabled
public class AutoDefendCrater extends AutoOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        initDetector(true);

        MineralType centerMineral;

        // TODO: Add scanning the mineral in front of the robot
        while (!isStarted()) {
            telemetry.addData("INSTRUCTIONS", "PLACE THE HOOK ON RIGHT SIDE");
            telemetry.update();
            telemetry.addData(">", "waiting for start command");
        }

        // Moves sideways right since the robot is on the right side (left side on the lander)
        robot.moveSidewaysByEncoder(DRIVE_SPEED, 250, 5);

        // Move forward towards the center mineral
        robot.moveByEncoder(DRIVE_SPEED, 500, 5);

        // Check if the center mineral is gold
        centerMineral = scanMineralType(5);
        if (centerMineral.equals(MineralType.GOLD)) {
            sampleMineral(100, -100);
        }

        // Moves sideways right where the robot directly faces the right mineral
        robot.moveSidewaysByEncoder(DRIVE_SPEED, 500, 5);

        // Scans the right mineral
        if (centerMineral.equals(MineralType.SILVER) && scanMineralType(5).equals(MineralType.GOLD)) {
            // Samples the right mineral
            sampleMineral(100, -100);
        }

        // Moves sideways right
        robot.moveSidewaysByEncoder(DRIVE_SPEED, 500, 5);

        // Turns counterclockwise to line up with the crater
        robot.turnByEncoder(TURN_SPEED, -200, 5);

        // Moves forward towards the crater
        robot.moveByEncoder(DRIVE_SPEED, 400, 5);

        // Park in the crater
        setSweeperLiftPower(SWEEPER_DEPLOY_SPEED, 500);
    }

    private void sampleMineral(double distance, double returnDistance) {
        robot.moveByEncoder(DRIVE_SPEED, distance, 5);
        sleep(500);
        robot.moveByEncoder(DRIVE_SPEED, returnDistance, 5);
    }
}
