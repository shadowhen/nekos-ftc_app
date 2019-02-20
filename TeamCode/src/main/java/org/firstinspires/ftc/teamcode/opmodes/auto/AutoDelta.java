package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.robot.Bot;
import org.firstinspires.ftc.teamcode.robot.MineralPosition;
import org.firstinspires.ftc.teamcode.robot.MineralType;
import org.firstinspires.ftc.teamcode.robot.TensorFlowDetector;

/**
 * This class allows the robot to scan for the gold mineral for the sampling points, and the robot
 * carries out the same tasks as Auto Beta after knocking off the gold mineral.
 *
 * @author Henry
 * @version 1.0
 */
@Autonomous(name = "Auto Delta - COMPETITION - DEPOT - RIGHT SIDE - MEET ALL POINTS", group = "auto")
public class AutoDelta extends AutoOpMode {

    private MineralType centerMineral;
    private MineralType leftMineral;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        initDetector(true);

        centerMineral = MineralType.NONE;
        leftMineral = MineralType.NONE;

        if (detector.getDetector() != null) {
            detector.getDetector().activate();
        }

        // Prevents the robot from detaching the REV hub
        while (!isStarted()) {
            recognitions = detector.getDetector().getUpdatedRecognitions();
            if (recognitions != null && recognitions.size() == 1) {
                if (recognitions.get(0).getLabel().equals(TensorFlowDetector.LABEL_GOLD_MINERAL)) {
                    centerMineral = MineralType.GOLD;
                } else {
                    centerMineral = MineralType.SILVER;
                }
            }

            telemetry.addData("INSTRUCTIONS", "PLACE THE HOOK ON RIGHT SIDE");
            telemetry.addData(">", "waiting for start command");
            telemetry.addData("center mineral", centerMineral.toString());
            telemetry.update();
        }

        //centerMineral = scanMineralType(3);

        // Lands on the ground by raising the lift
        liftByTime(Bot.VERTICAL_RAISE_SPEED, 3100);

        // Scans for the first mineral
        //centerMineral = scanMineralType(3);

        if (centerMineral.equals(MineralType.GOLD)) {
            goCenter();
        } else {
            // Moves to the left sideways
            robot.moveSidewaysByEncoder(DRIVE_SPEED, -250, 5);

            // Turn to align the robot for scanning
            robot.turnByEncoder(TURN_SPEED, -100, 5);

            // Scans the left mineral for mineral type
            leftMineral = scanMineralType(3);

            if (leftMineral.equals(MineralType.GOLD)) {
                goLeft();
            } else {
                goRight();
            }
        }

        if (detector.getDetector() != null) {
            detector.getDetector().deactivate();
        }
    }

    private void goCenter() {
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
        robot.moveSidewaysByEncoder(DRIVE_SPEED, 200, 5);
        robot.turnByEncoder(TURN_SPEED, -100, 5);
        robot.moveSidewaysByEncoder(DRIVE_SPEED, 190, 5);
        robot.moveByEncoder(1, 1000+(1000-150), 1000+(1000-150), 10);
        //robot.moveByEncoder(TURN_SPEED, -100, 100, 5);
        //robot.moveByEncoder(1, 1000-150, 1000-150, 10);
        setSweeperLiftPower(SWEEPER_DEPLOY_SPEED, 500);
    }

    private void goLeft() {
        double speed = 1.0;

        robot.turnByEncoder(TURN_SPEED, 100+10, 5);
        robot.moveByEncoder(0.8, 400, 400, 5);
        robot.moveSidewaysByEncoder(DRIVE_SPEED, -300-30, 5);

        robot.moveByEncoder(speed, 500+100, 500+100, 5);
        robot.turnByEncoder(TURN_SPEED, 200, 5);

        // From Charlie
        setSweeperLiftPower(SWEEPER_DEPLOY_SPEED, 500);
        sleep(500);
        setSweeperLiftPower(SWEEPER_RETRACT_SPEED, 600);

        robot.moveByEncoder(speed, -1000, -1000, 5);

        robot.turnByEncoder(TURN_SPEED, -1110, 5);
        robot.moveByEncoder(speed, 300, 300, 5);
        setSweeperLiftPower(SWEEPER_DEPLOY_SPEED, 500);
    }

    private void goRight() {
        double speed = 0.8;

        robot.turnByEncoder(speed, 100, 5);
        robot.moveByEncoder(speed, 400, 400, 5);
        robot.moveSidewaysByEncoder(0.8, 550, 5);

        robot.moveByEncoder(speed, 500+80+100, 500+80+100, 5);
        robot.turnByEncoder(speed, -300, 5);
        robot.moveByEncoder(speed, 400, 400, 5);

        setSweeperLiftPower(SWEEPER_DEPLOY_SPEED, 500);
        sleep(500);
        setSweeperLiftPower(SWEEPER_RETRACT_SPEED, 600);

        robot.moveByEncoder(speed, 200, 200, 5);

        robot.turnByEncoder(speed, -300, 5);

        robot.moveByEncoder(speed, 200, 200, 5);

        robot.turnByEncoder(speed, -250, 5);
        robot.moveByEncoder(1, 1800, 1800, 5);
        setSweeperLiftPower(SWEEPER_DEPLOY_SPEED, 500);
    }
}
