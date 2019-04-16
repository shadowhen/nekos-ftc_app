package org.firstinspires.ftc.teamcode.opmodes.auto.compeition;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.opmodes.auto.AutoOpMode;
import org.firstinspires.ftc.teamcode.robot.Bot;
import org.firstinspires.ftc.teamcode.robot.MineralType;
import org.firstinspires.ftc.teamcode.robot.TensorFlowDetector;

/**
 * The class implements autonomous plan Charlie which the robot lands on the crater side, drives to
 * depot for dropping a team marker, and then drives to and parks on the crater partially.
 *
 * @author Henry
 * @version 1.1
 */
@Autonomous(name = "Fox - COMPETITION - Crater - Left Side - Claiming Depot and Park", group = "auto")
public class AutoFox extends AutoOpMode {

    private double highSpeed = 0.75;
    private double extermeSpeed = 1.0;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        initDetector(true);

        MineralType centerMineral = MineralType.NONE;
        MineralType leftMineral;

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

        robot.moveByEncoder(extermeSpeed, -300, 300, 5);
        robot.moveSidewaysByEncoder(extermeSpeed, 250, 5);
        robot.moveByEncoder(extermeSpeed, 1100,5);

        // Drop the team marker by deploying the sweeper
        setSweeperLiftPower(SWEEPER_DEPLOY_SPEED, 500);
        sleep(500);

        // Retracts the sweeper so the robot can move more freely
        setSweeperLiftPower(SWEEPER_RETRACT_SPEED, 600);

        // Drives to the crater on our alliance side to park
        robot.moveByEncoder(extermeSpeed, -1100, -1000, 5);
        robot.turnByEncoder(extermeSpeed, -1210+200, 5);
        robot.moveByEncoder(extermeSpeed, 300, 300, 5);

        // Park partially on the crater by deploying the sweeper
        setSweeperLiftPower(SWEEPER_DEPLOY_SPEED, 500);

        if (detector.getDetector() != null) {
            detector.getDetector().deactivate();
        }
    }

    private void goCenter() {
        // Moves to the left sideways
        robot.moveSidewaysByEncoder(extermeSpeed, -200, 5);

        // Runs through the minerals
        robot.moveByEncoder(extermeSpeed, 700, 5);

        robot.moveByEncoder(extermeSpeed, -300, 5);

        robot.turnByEncoder(extermeSpeed, -500, 5);

        robot.moveByEncoder(extermeSpeed, 400, 5);
    }

    private void goLeft() {
        double speed = 1.0;

        // Lines up to the hit the gold mineral on the left
        robot.turnByEncoder(extermeSpeed, 100+10, 5);
        robot.moveByEncoder(extermeSpeed, 400, 5);
        robot.moveSidewaysByEncoder(extermeSpeed, -300-30, 5);

        // Runs towards the gold mineral to earn sampling points
        robot.moveByEncoder(extermeSpeed, 400, 5);

        robot.moveByEncoder(extermeSpeed, -300, 5);

        robot.turnByEncoder(extermeSpeed, -500, 5);

        robot.moveByEncoder(extermeSpeed, 250, 5);
    }

    private void goRight() {
        double speed = 0.8;

        // Lines up with the gold mineral on the right
        robot.turnByEncoder(extermeSpeed, 100, 5);
        robot.moveByEncoder(extermeSpeed, 400, 5);
        robot.moveSidewaysByEncoder(extermeSpeed, 550, 5);

        // Runs towards the gold mineral to earn sampling points
        robot.moveByEncoder(extermeSpeed, 400, 5);

        robot.moveByEncoder(extermeSpeed, -400, 5);

        robot.turnByEncoder(extermeSpeed, -500, 5);

        robot.moveByEncoder(extermeSpeed, 500+350+250, 5);
    }

}
