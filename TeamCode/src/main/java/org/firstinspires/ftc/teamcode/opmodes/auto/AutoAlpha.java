package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.robot.AutoDrive;
import org.firstinspires.ftc.teamcode.robot.TensorFlowDetector;
import org.firstinspires.ftc.teamcode.robot.VuforiaDetector;
import org.firstinspires.ftc.teamcode.robot.VuforiaKey;

/**
 * This class expands the AutoOpMode.class to tell the robot to knock off the gold mineral, drop
 * its team marker, and park in one of the spots which gives the team points.
 *
 * @author Henry
 * @version 1.0
 */
@Autonomous(name = "Auto Alpha", group = "auto")
public class AutoAlpha extends AutoOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        vuforia = new VuforiaDetector();
        detector = new TensorFlowDetector();

        vuforia.init(hardwareMap, VuforiaKey.VUFORIA_KEY, "Webcam 1");
        detector.init(hardwareMap, vuforia.getVuforia());

        // Prevents the robot from detaching the REV hub
        while (!isStarted()) {
            telemetry.addData(">", "waiting for start command");
            telemetry.update();
        }

        if (detector.getDetector() != null) {
            detector.getDetector().activate();
        }

        robot.moveByEncoder(DRIVE_SPEED, 500, 500, 5);

        sleep(SLEEP_DRIVE);

        switch (findGoldMineralFromPosition(5)) {
            case LEFT:
                robot.setAutoDrive(AutoDrive.SIDEWAYS);
                robot.moveByEncoder(SIDEWAYS_SPEED, 400, 400, 5);

                sleep(SLEEP_DRIVE);
                robot.setAutoDrive(AutoDrive.FORWARD);
                robot.moveByEncoder(DRIVE_SPEED, 400, 400, 5);

                sleep(SLEEP_DRIVE);
                robot.setAutoDrive(AutoDrive.SIDEWAYS);
                robot.moveByEncoder(SIDEWAYS_SPEED, 400, 400, 5);

                sleep(SLEEP_DRIVE);
                dropTeamMarker(400, 2000, 400);
                break;
            case RIGHT:
                robot.setAutoDrive(AutoDrive.SIDEWAYS);
                robot.moveByEncoder(SIDEWAYS_SPEED, 400, 400, 5);

                sleep(SLEEP_DRIVE);
                robot.setAutoDrive(AutoDrive.FORWARD);
                robot.moveByEncoder(DRIVE_SPEED, 400, 400, 5);

                sleep(SLEEP_DRIVE);
                robot.setAutoDrive(AutoDrive.SIDEWAYS);
                robot.moveByEncoder(SIDEWAYS_SPEED, 400, 400, 5);

                sleep(SLEEP_DRIVE);
                dropTeamMarker(400, 2000, 400);

                sleep(SLEEP_DRIVE);
                robot.moveByEncoder(DRIVE_SPEED, 500, 500, 5);
                break;
            default:
                // If the robot cannot find a gold mineral or finds the gold mineral at the center

                robot.moveByEncoder(DRIVE_SPEED, 500, 500, 5);
                sleep(SLEEP_DRIVE);
                dropTeamMarker(400, 2000, 400);
                sleep(SLEEP_DRIVE);
                robot.moveByEncoder(DRIVE_SPEED, 500, 500, 5);
        }

        if (detector.getDetector() != null) {
            detector.getDetector().shutdown();
        }
    }

    private void dropTeamMarker(double distance, long ms, double turnDistance) {
        robot.setAutoDrive(AutoDrive.FORWARD);

        robot.moveByEncoder(DRIVE_SPEED, distance, distance,5);

        sleep(SLEEP_DRIVE);

        robot.getSweeper().setLiftPower(0.8);
        sleep(ms);
        robot.getSweeper().setLiftPower(0);

        robot.moveByEncoder(TURN_SPEED, -turnDistance, turnDistance, 5);
    }
}
