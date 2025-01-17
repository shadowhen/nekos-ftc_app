package org.firstinspires.ftc.teamcode.opmodes.auto.compeition;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.opmodes.auto.AutoOpMode;
import org.firstinspires.ftc.teamcode.robot.Bot;

/**
 * The class implements autonomous plan Charlie which the robot lands on the crater side, drives to
 * depot for dropping a team marker, and then drives to and parks on the crater partially.
 *
 * @author Henry
 * @version 1.1
 */
@Autonomous(name = "Charlie - COMPETITION - Crater - Left Side - Claiming Depot and Park", group = "auto")
public class AutoCharlie extends AutoOpMode {

    private static final double HIGH_SPEED = 0.75;
    private static final double TURN_HIGH_SPEED = 0.7;
    
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

        // Lands on the field by raising the lift
        liftByTime(Bot.VERTICAL_RAISE_SPEED, SLEEP_LANDING);

        // Moves sideways to unlatch from the lander latch
        robot.moveSidewaysByEncoder(HIGH_SPEED, 200, 5);

        // Drive to the depot
        robot.moveByEncoder(HIGH_SPEED, 300, 300, 5);
        robot.moveByEncoder(TURN_HIGH_SPEED, -450, 450, 5);
        // TODO:
        robot.moveByEncoder(HIGH_SPEED, 1000, 1000, 5);
        robot.moveByEncoder(TURN_HIGH_SPEED, -300, 300, 5);
        robot.moveByEncoder(HIGH_SPEED, 1000, 1000, 5);

        // Drop the team marker by deploying the sweeper
        setSweeperLiftPower(SWEEPER_DEPLOY_SPEED, 500);
        sleep(500);

        // Retracts the sweeper so the robot can move more freely
        setSweeperLiftPower(SWEEPER_RETRACT_SPEED, 600);

        // Drives to the crater on our alliance side to park
        robot.moveByEncoder(HIGH_SPEED, -1000, -1000, 5);
        robot.turnByEncoder(TURN_HIGH_SPEED, -1110, 5);
        robot.moveByEncoder(HIGH_SPEED, 300, 300, 5);

        // Park partially on the crater by deploying the sweeper
        setSweeperLiftPower(SWEEPER_DEPLOY_SPEED, 500);
    }

}
