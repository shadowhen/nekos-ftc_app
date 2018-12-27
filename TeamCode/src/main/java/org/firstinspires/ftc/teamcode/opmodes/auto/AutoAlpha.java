package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.robot.AutoDrive;
import org.firstinspires.ftc.teamcode.robot.TensorFlowDetector;
import org.firstinspires.ftc.teamcode.robot.VuforiaDetector;
import org.firstinspires.ftc.teamcode.robot.VuforiaKey;

import java.util.List;

/**
 * This class expands the AutoOpMode.class to tell the robot to knock off the gold mineral, drop
 * its team marker, and park in one of the spots which gives the team points.
 *
 * @author Henry
 * @version 1.0
 */
@Autonomous(name = "Auto Alpha", group = "auto")
public class AutoAlpha extends AutoOpMode {

    private VuforiaDetector vuforia;
    private TensorFlowDetector detector;

    private List<Recognition> recognitions;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        vuforia = new VuforiaDetector();
        detector = new TensorFlowDetector();

        vuforia.init(hardwareMap, VuforiaKey.VUFORIA_KEY);
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

        switch (findGoldMineralFromPosition(5)) {
            case LEFT:
                robot.setAutoDrive(AutoDrive.SIDEWAYS_LEFT);
                robot.moveByEncoder(SIDEWAYS_SPEED, 400, 400, 5);

                robot.setAutoDrive(AutoDrive.FORWARD);
                robot.moveByEncoder(DRIVE_SPEED, 400, 400, 5);

                robot.setAutoDrive(AutoDrive.SIDEWAYS_RIGHT);
                robot.moveByEncoder(SIDEWAYS_SPEED, 400, 400, 5);

                //dropTeamMarker(400, 2, 400);
                break;
            case MIDDLE:
                robot.moveByEncoder(DRIVE_SPEED, 400, 400, 5);

                //dropTeamMarker(400, 2, 400);
                break;
            case RIGHT:
                robot.setAutoDrive(AutoDrive.SIDEWAYS_RIGHT);
                robot.moveByEncoder(SIDEWAYS_SPEED, 400, 400, 5);

                robot.setAutoDrive(AutoDrive.FORWARD);
                robot.moveByEncoder(DRIVE_SPEED, 400, 400, 5);

                robot.setAutoDrive(AutoDrive.SIDEWAYS_LEFT);
                robot.moveByEncoder(SIDEWAYS_SPEED, 400, 400, 5);

                //dropTeamMarker(400, 2, 400);
                break;
        }
    }

    private void dropTeamMarker(double distance, double seconds, double turnDistance) {
        robot.setAutoDrive(AutoDrive.FORWARD);
        robot.moveByEncoder(DRIVE_SPEED, distance, distance,5);
        robot.getSweeper().getLiftMotor().setPower(0.8);
        waitForSeconds(seconds);
        robot.getSweeper().getLiftMotor().setPower(0);

        robot.moveByEncoder(TURN_SPEED, -turnDistance, turnDistance, 5);
    }
}
