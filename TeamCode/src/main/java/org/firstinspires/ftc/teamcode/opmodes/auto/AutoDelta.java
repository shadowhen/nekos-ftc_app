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

    private MineralPosition mineralPosition;
    private MineralType centerMineral;
    private MineralType leftMineral;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        initDetector(true);

        mineralPosition = MineralPosition.NONE;

        if (detector.getDetector() != null) {
            detector.getDetector().activate();
        }

        // Prevents the robot from detaching the REV hub
        while (!isStarted()) {
            telemetry.addData("INSTRUCTIONS", "PLACE THE HOOK ON RIGHT SIDE");
            telemetry.addData(">", "waiting for start command");
            telemetry.update();
        }

        // Lands on the ground by raising the lift
        liftByTime(Bot.VERTICAL_RAISE_SPEED, 3100);

        // Scans for the first mineral
        centerMineral = scanMineralType(3);

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
        robot.moveSidewaysByEncoder(DRIVE_SPEED, -200, 5);
        robot.moveByEncoder(DRIVE_SPEED, 500, 500, 5);
    }

    private void goLeft() {
        robot.turnByEncoder(TURN_SPEED, 100, 5);
        robot.moveByEncoder(DRIVE_SPEED, 400, 400, 5);
        robot.moveSidewaysByEncoder(DRIVE_SPEED, -300, 5);
    }

    private void goRight() {
        robot.turnByEncoder(TURN_SPEED, 100, 5);
        robot.moveByEncoder(DRIVE_SPEED, 400, 400, 5);
        robot.moveSidewaysByEncoder(DRIVE_SPEED, 550, 5);
    }
}
