package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.robot.MineralPosition;
import org.firstinspires.ftc.teamcode.robot.TensorFlowDetector;
import org.firstinspires.ftc.teamcode.robot.VuforiaKey;

/**
 * This class expands the AutoOpMode.class to tell the robot to knock off the gold mineral, drop
 * its team marker, and park in one of the spots which gives the team points.
 *
 * @author Henry
 * @version 1.0
 */
@Autonomous(name = "Auto Delta", group = "auto")
public class AutoDelta extends AutoOpMode {

    private MineralPosition mineralPosition;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        initDetector(false);

        mineralPosition = MineralPosition.NONE;

        if (detector.getDetector() != null) {
            detector.getDetector().activate();
        }

        // Prevents the robot from detaching the REV hub
        while (!isStarted()) {
            int goldMineralX = -1;
            int silverMineralX1 = -1;
            int silverMineralX2 = -1;
            recognitions = detector.getDetector().getUpdatedRecognitions();

            if (recognitions != null && recognitions.size() == 2) {
                for (Recognition recognition : recognitions) {
                    if (recognition.getLabel().equals(TensorFlowDetector.LABEL_GOLD_MINERAL)) {
                        goldMineralX = (int)recognition.getTop();
                    } else if (silverMineralX1 == -1) {
                        silverMineralX1 = (int)recognition.getTop();
                    } else {
                        silverMineralX2 = (int)recognition.getTop();
                    }
                }

                if (goldMineralX != -1 && goldMineralX > silverMineralX1) {
                    mineralPosition = MineralPosition.CENTER;
                } else if (goldMineralX != -1 && goldMineralX < silverMineralX1) {
                    mineralPosition = MineralPosition.RIGHT;
                } else if (silverMineralX1 != -1 && silverMineralX2 != -1){
                    mineralPosition = MineralPosition.LEFT;
                } else {
                    mineralPosition = MineralPosition.NONE;
                }
            }

            robot.getSweeper().getSweeperServo().setPower(0.0);
            telemetry.addData(">", "waiting for start command");
            telemetry.addData("mineral position", mineralPosition.toString());
            telemetry.update();
        }

        robot.moveByEncoder(DRIVE_SPEED, -505, -505, 5);
        robot.moveSidewaysByEncoder(SIDEWAYS_SPEED, 100, 5);

        sleep(500);

        switch (mineralPosition) {
            case LEFT:
                robot.moveSidewaysByEncoder(SIDEWAYS_SPEED, 100, 5);
                sleep(500);
                robot.moveByEncoder(DRIVE_SPEED, -500, -500, 5);
                robot.moveByEncoder(0.5, 1480, -1480, 5);

                setLiftPower(-0.5, 500);
                setLiftPower(0.5, 1000);

                robot.moveByEncoder(0.5, -740, 740, 5);
                robot.moveByEncoder(0.75, 1000, 1000, 5);
                break;
            case RIGHT:
                robot.moveSidewaysByEncoder(SIDEWAYS_SPEED, -100, 5);
                robot.moveByEncoder(DRIVE_SPEED,  -500, -500, 5);
                robot.moveByEncoder(0.5, 1480, -1480, 5);

                setLiftPower(-0.5, 500);
                setLiftPower(0.5, 1000);

                robot.moveByEncoder(0.5, -740, 740, 5);
                robot.moveByEncoder(0.75, 1000, 1000, 5);
                break;
            default:
                robot.moveSidewaysByEncoder(SIDEWAYS_SPEED, -500, 5);
                robot.moveByEncoder(DRIVE_SPEED,  -500, -500, 5);
                robot.moveByEncoder(0.5, 1480, -1480, 5);

                setLiftPower(-0.5, 500);
                setLiftPower(0.5, 1000);

                robot.moveByEncoder(0.5, -740, 740, 5);
                robot.moveByEncoder(0.75, 1000, 1000, 5);
        }

        setLiftPower(-0.5, 500);

        if (detector.getDetector() != null) {
            detector.getDetector().deactivate();
        }
    }
}
