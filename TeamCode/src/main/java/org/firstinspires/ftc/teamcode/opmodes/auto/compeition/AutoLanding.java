package org.firstinspires.ftc.teamcode.opmodes.auto.compeition;

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
@Autonomous(name = "Landing - COMPETITION - Depot - Right Side - No Sampling", group = "auto")
public class AutoLanding extends AutoOpMode {

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
    }
}
