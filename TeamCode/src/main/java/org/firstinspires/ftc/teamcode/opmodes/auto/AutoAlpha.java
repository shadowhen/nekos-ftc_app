package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.robot.Bot;
import org.firstinspires.ftc.teamcode.robot.MineralPosition;
import org.firstinspires.ftc.teamcode.robot.VuforiaKey;

/**
 * This class presents Autonomous Plan Alpha which the robot drops from the lander on the
 * depot side, sample the gold mineral, drop the team marker, and park partially on the crater.
 *
 * @author Henry
 * @version 1.0
 */
@Autonomous(name = "Auto Alpha - COMPETITION - CRATER - LEFT SIDE - LANDING - PARK ONLY", group = "auto")
public class AutoAlpha extends AutoOpMode {

    private MineralPosition mineralPosition;

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
        liftByTime(Bot.VERTICAL_RAISE_SPEED, 3100);

        // Moves to the left sideways
        robot.moveSidewaysByEncoder(DRIVE_SPEED, 200, 5);

        // Moves forward so the robot can get into position
        robot.moveByEncoder(DRIVE_SPEED, 600, 600, 5);

        sleep(1000);

        setSweeperLiftPower(SWEEPER_DEPLOY_SPEED, 500);
    }
}
