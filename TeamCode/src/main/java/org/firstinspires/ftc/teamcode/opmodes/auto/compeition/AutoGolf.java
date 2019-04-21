package org.firstinspires.ftc.teamcode.opmodes.auto.compeition;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.auto.AutoOpMode;
import org.firstinspires.ftc.teamcode.robot.Bot;
import org.firstinspires.ftc.teamcode.robot.MineralType;
import org.firstinspires.ftc.teamcode.robot.TensorFlowDetector;

/**
 * This class implements the autonomous plan which the robot drops from the lander, samples the gold
 * mineral, places the team marker in the alliance depot, and goes to park on the crater on the
 * opposing alliance side.
 * @author Henry
 * @version 1.0
 */
@Autonomous(name = "Golf - COMPETITION - Depot - Right Side - Does Everything", group = "auto")
public class AutoGolf extends AutoOpMode {

    private static final double HIGH_SPEED = 0.75;
    private static final double TURN_HIGH_SPEED = 0.7;

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

        // Lands on the ground by raising the lift
        liftByTime(Bot.VERTICAL_RAISE_SPEED, SLEEP_LANDING);

        if (centerMineral.equals(MineralType.GOLD)) {
            goCenter();
        } else {
            // Moves to the left sideways
            robot.moveSidewaysByEncoder(HIGH_SPEED, -250, 5);

            // Turn to align the robot for scanning
            robot.turnByEncoder(TURN_HIGH_SPEED, -100, 5);

            // Scans the left mineral for mineral type
            leftMineral = scanMineralType(3);

            if (leftMineral.equals(MineralType.GOLD)) {
                // This method would be most accurate when the robot executes this
                goLeft();
            } else {
                goRightAlt();
            }
        }

        if (detector.getDetector() != null) {
            detector.getDetector().deactivate();
        }
    }

    // Safe, ready
    private void goCenter() {
        // Moves to the left sideways
        robot.moveSidewaysByEncoder(HIGH_SPEED, -200+100, 5);

        // Runs through the minerals
        robot.moveByEncoder(HIGH_SPEED, 1180, 5);

        // Deposits the team marker in the depot for autonomous points
        setSweeperLiftPower(SWEEPER_DEPLOY_SPEED, 500);
        sleep(1000);
        setSweeperLiftPower(SWEEPER_RETRACT_SPEED, 600);

        // Turns the robot and moves towards the pit for parking points
        robot.moveByEncoder(TURN_HIGH_SPEED, -740, 740, 5);
        robot.moveSidewaysByEncoder(HIGH_SPEED, 200, 5);
        robot.turnByEncoder(TURN_HIGH_SPEED, -100, 5);
        robot.moveSidewaysByEncoder(HIGH_SPEED, 190,5);
        robot.moveByEncoder(1, 1000+(1000-150),10);
        setSweeperLiftPower(SWEEPER_DEPLOY_SPEED, 500);
    }

    // Safe, ready
    private void goLeft() {
        double speed = 1.0;

        // Lines up to the hit the gold mineral on the left
        robot.turnByEncoder(TURN_HIGH_SPEED, 100+30, 5);
        robot.moveByEncoder(HIGH_SPEED, 400, 5);
        robot.moveSidewaysByEncoder(HIGH_SPEED, -300-30, 5);

        // Runs towards the gold mineral to earn sampling points
        robot.moveByEncoder(HIGH_SPEED, 500+100, 500+100, 5);
        robot.turnByEncoder(TURN_HIGH_SPEED, 200, 5);
        robot.moveSidewaysByEncoder(0.75, -100, 5);

        // Drop the team marker by deploying the sweeper
        setSweeperLiftPower(SWEEPER_DEPLOY_SPEED, 500);
        sleep(500);

        // Retracts the sweeper, so the robot can deploy the sweeper later for parking
        setSweeperLiftPower(SWEEPER_RETRACT_SPEED, 600);

        // Goes backward and then turns ~180 degrees
        robot.moveByEncoder(speed, -1000, 5);
        robot.turnByEncoder(TURN_HIGH_SPEED, -1110+100, 5);

        // Runs to the crater and deploys the sweeper to park partially
        robot.moveByEncoder(speed, 300, 5);
        setSweeperLiftPower(SWEEPER_DEPLOY_SPEED, 500);
    }

    // Not ready
    private void goRight() {
        double speed = 0.8;

        // Lines up with the gold mineral on the right
        robot.turnByEncoder(TURN_HIGH_SPEED, 140, 5);
        robot.moveByEncoder(HIGH_SPEED, 600, 5);
        robot.moveSidewaysByEncoder(0.75, 550-80, 5);

        // Runs towards the gold mineral to earn sampling points
        robot.moveByEncoder(HIGH_SPEED, 500+80+100, 5);

        // Moves into position where the robot can drop the team marker
        robot.turnByEncoder(TURN_HIGH_SPEED, -300, 5);
        robot.moveByEncoder(HIGH_SPEED, 300, 5);

        // Drops the team marker
        setSweeperLiftPower(SWEEPER_DEPLOY_SPEED, 500);
        sleep(500);
        setSweeperLiftPower(SWEEPER_RETRACT_SPEED, 600);

        // Get into position where the robot can make run for the crater to park
        robot.moveByEncoder(speed, 200, 200, 5);
        robot.turnByEncoder(speed, -300, 5);
        robot.moveByEncoder(speed, 200+100, 5);
        robot.turnByEncoder(speed, -230-10, 5);
        robot.moveSidewaysByEncoder(0.7, 100, 5);

        // Runs to the crater and then parks by deploying the sweeper
        robot.moveByEncoder(1, 1800,5);
        setSweeperLiftPower(SWEEPER_DEPLOY_SPEED, 500);
    }

    // Not Ready
    private void goRightAlt() {
        // Lines up with the gold mineral on the right
        robot.turnByEncoder(TURN_HIGH_SPEED, 140, 5);
        robot.moveByEncoder(HIGH_SPEED, 600, 5);
        robot.moveSidewaysByEncoder(0.75, 550-80, 5);

        // Runs towards the gold mineral to earn sampling points
        robot.moveByEncoder(HIGH_SPEED, 400, 5);

        // Backs out
        robot.moveByEncoder(HIGH_SPEED, -310, 5);

        // Heads for the barrier
        robot.turnByEncoder(HIGH_SPEED, -560, 5);

        // Move forward, turn right, and move towards the depot
        robot.moveByEncoder(HIGH_SPEED, 500+350+250, 5);
        robot.moveByEncoder(HIGH_SPEED, -290, 290, 5);
        robot.moveByEncoder(HIGH_SPEED, 200, 5);

        // Drops the team marker
        setSweeperLiftPower(SWEEPER_DEPLOY_SPEED, SWEEPER_DEPLOY_SLEEP);
        setSweeperLiftPower(SWEEPER_RETRACT_SPEED, SWEEPER_RETRACT_SLEEP);

        // Move back and turn around ~180 degrees
        robot.moveByEncoder(HIGH_SPEED, -1000, 5);
        robot.turnByEncoder(TURN_HIGH_SPEED, -1010, 5);
        robot.moveByEncoder(HIGH_SPEED, 300, 5);

        // Park in the crater
        setSweeperLiftPower(SWEEPER_DEPLOY_SPEED, 500);
    }

    // Not ready
    private void avoidMinerals() {
        // Moves to the barrier
        robot.turnByEncoder(TURN_HIGH_SPEED, 100+10, 5);
        robot.moveSidewaysByEncoder(HIGH_SPEED, -500, 5);
        robot.moveByEncoder(HIGH_SPEED, 400, 5);

        // Moves to the depot and drop team marker
        robot.turnByEncoder(HIGH_SPEED, 400, 5);
        robot.moveByEncoder(HIGH_SPEED, 500, 5);
        setSweeperLiftPower(SWEEPER_DEPLOY_SPEED, SWEEPER_DEPLOY_SLEEP);
        sleep(500);
        setSweeperLiftPower(SWEEPER_RETRACT_SPEED, SWEEPER_RETRACT_SLEEP);

        // Moves backward so the robot can retract the sweeper
        robot.moveByEncoder(HIGH_SPEED, -400, 5);

        // Turns ~180 degrees and moves towards the crater to park
        robot.turnByEncoder(TURN_HIGH_SPEED, 1010, 5);
        robot.moveByEncoder(HIGH_SPEED, 1000, 5);
        setSweeperLiftPower(SWEEPER_DEPLOY_SPEED, SWEEPER_DEPLOY_SLEEP);
    }
}
