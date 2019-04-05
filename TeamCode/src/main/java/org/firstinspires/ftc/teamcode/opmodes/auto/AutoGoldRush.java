package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.robot.Bot;
import org.firstinspires.ftc.teamcode.robot.MineralType;
import org.firstinspires.ftc.teamcode.robot.TensorFlowDetector;

/**
 * This class implements the gold rush autonomous which the robot lands from the lander,
 * samples the minerals, and then parks on the crater.
 * @author Henry
 * @version 1.0
 */
@Autonomous(name = "Auto Gold Rush - CRATER - RIGHT SIDE - SAMPLE", group = "auto")
@Disabled
public class AutoGoldRush extends AutoOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        initDetector(true);

        MineralType leftMineral;
        MineralType centerMineral = MineralType.NONE;

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

        // Moves sideways left into starting position
        robot.moveSidewaysByEncoder(DRIVE_SPEED, -250, 5);

        if (centerMineral.equals(MineralType.GOLD)) {
            goCenter();
        } else {
            // Turn counterclockwise to scan the mineral on the left from the robot's perspective
            robot.turnByEncoder(TURN_SPEED, -100, 5);

            // Scans the mineral on the left
            leftMineral = scanMineralType(5);

            if (leftMineral.equals(MineralType.SILVER)) {
                goLeft();
            } else {
                goRight();
            }
        }
    }

    private void goLeft() {
        // Lines up to the hit the gold mineral on the left
        robot.turnByEncoder(TURN_SPEED, 100+10, 5);
        robot.moveByEncoder(DRIVE_SPEED, 400, 400, 5);
        robot.moveSidewaysByEncoder(DRIVE_SPEED, -300-30, 5);

        // Runs into the mineral for sampling points
        robot.moveByEncoder(DRIVE_SPEED, 500+100, 500+100, 5);

        // Park in the crater
        setSweeperLiftPower(SWEEPER_DEPLOY_SPEED, 500);
    }

    private void goCenter() {
        // Runs through the minerals
        robot.moveByEncoder(DRIVE_SPEED, 1180, 1180, 5);

        // Park in the crater
        setSweeperLiftPower(SWEEPER_DEPLOY_SPEED, 500);
    }

    private void goRight() {
        // Lines up with the gold mineral on the right
        robot.turnByEncoder(TURN_SPEED, 100, 5);
        robot.moveByEncoder(DRIVE_SPEED, 400, 400, 5);
        robot.moveSidewaysByEncoder(0.8, 550, 5);

        // Runs towards the gold mineral to earn sampling points
        robot.moveByEncoder(DRIVE_SPEED, 500+80+100, 500+80+100, 5);

        // Park in the crater
        setSweeperLiftPower(SWEEPER_DEPLOY_SPEED, 500);
    }
}
