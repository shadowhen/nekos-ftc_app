package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.robot.Bot;
import org.firstinspires.ftc.teamcode.robot.MineralPosition;
import org.firstinspires.ftc.teamcode.robot.VuforiaKey;

/**
 * This class implements the autonomous plan Alpha which the robot lands from the crater side
 * and drives to the crater to park only.
 *
 * @author Henry
 * @version 1.1
 */
@Autonomous(name = "Auto Alpha - COMPETITION - CRATER - LEFT SIDE - LANDING - PARK ONLY", group = "auto")
public class AutoAlpha extends AutoOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        // Prevents the robot from detaching the REV hub
        while (!isStarted()) {
            telemetry.addData("INSTRUCTIONS", "PLACE THE HOOK ON THE LEFT SIDE");
            telemetry.addData(">", "Press START to start autonomous");
            telemetry.update();
        }

        // Lands on the ground by raising the lift
        liftByTime(Bot.VERTICAL_RAISE_SPEED, SLEEP_LANDING);

        // Drives to the crater for parking
        robot.moveSidewaysByEncoder(DRIVE_SPEED, 200, 5);
        robot.moveByEncoder(DRIVE_SPEED, 600, 600, 5);
        sleep(1000);

        // Parks on the crater by deploying the sweeper
        setSweeperLiftPower(SWEEPER_DEPLOY_SPEED, 500);
    }
}
