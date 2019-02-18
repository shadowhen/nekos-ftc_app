package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.robot.Bot;

/**
 * The class is based on the AutoBeta class with the robot being able to land on the ground from
 * the lander. Unlike AutoBeta, the class implements the landing in the autonomous, making this
 * class more viable for autonomous period and alliance matching.
 *
 * @author Henry
 * @version 1.0
 */
@Autonomous(name = "Auto Charlie - COMPETITION - CRATER - LEFT SIDE - LANDING - CLAIM DEPOT - PARK OUR CRATER", group = "auto")
public class AutoCharlie extends AutoOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        // No reason for this line but commenting lines below may affect the chances of success
        robot.setDriveZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // The robot would hold on the lander
        robot.getLift().getLiftMotor().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        while (!isStarted()) {
            telemetry.addData("INSTRUCTIONS", "PLACE THE HOOK ON THE LEFT SIDE");
            telemetry.addData(">", "Press START to start encoder drive forward");
            telemetry. update();
        }

        liftByTime(Bot.VERTICAL_RAISE_SPEED, 3100);

        robot.moveSidewaysByEncoder(SIDEWAYS_SPEED, 200, 5);

        robot.moveByEncoder(DRIVE_SPEED, 300, 300, 5);

        robot.moveByEncoder(TURN_SPEED, -450, 450, 5);
        robot.moveByEncoder(DRIVE_SPEED, 1000, 1000, 5);

        robot.moveByEncoder(TURN_SPEED, -300, 300, 5);
        robot.moveByEncoder(DRIVE_SPEED, 1000, 1000, 5);

        setSweeperLiftPower(SWEEPER_DEPLOY_SPEED, 500);
        sleep(500);
        setSweeperLiftPower(SWEEPER_RETRACT_SPEED, 600);

        robot.moveByEncoder(DRIVE_SPEED, -1000, -1000, 5);

        robot.turnByEncoder(TURN_SPEED, -1110, 5);
        robot.moveByEncoder(DRIVE_SPEED, 300, 300, 5);
        setSweeperLiftPower(SWEEPER_DEPLOY_SPEED, 500);
    }

}
