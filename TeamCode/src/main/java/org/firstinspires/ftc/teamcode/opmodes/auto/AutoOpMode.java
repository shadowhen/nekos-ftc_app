package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.robot.AutoBot;
import org.firstinspires.ftc.teamcode.robot.AutoDrive;
import org.firstinspires.ftc.teamcode.robot.TensorFlowDetector;
import org.firstinspires.ftc.teamcode.robot.VuforiaDetector;
import org.firstinspires.ftc.teamcode.robot.VuforiaKey;

import java.util.List;

/**
 * This class allows the robot to drive autonomously without the driver's intervention, and because
 * of the nature of this class, other auto opmodes extends this class for specific autonomous tasks.
 *
 * @author Henry
 * @version 1.0
 */
public class AutoOpMode extends LinearOpMode {

    enum TFODSide {
        TOP, BOTTOM, LEFT, RIGHT
    }

    enum MineralPosition {
        LEFT, MIDDLE, RIGHT, NONE
    }

    public static final double DRIVE_SPEED = 0.5;
    public static final double TURN_SPEED = 0.25;
    public static final double SIDEWAYS_SPEED = 0.4;

    protected ElapsedTime timer;

    protected VuforiaDetector vuforia;
    protected TensorFlowDetector detector;

    protected List<Recognition> recognitions;

    protected AutoBot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new AutoBot(this);
        timer = new ElapsedTime();

        robot.init(hardwareMap, telemetry);
        robot.setAutoDrive(AutoDrive.FORWARD);
    }

    public void initDetector(boolean useWebcam) {
        if (useWebcam) {
            vuforia.init(hardwareMap, VuforiaKey.VUFORIA_KEY, VuforiaDetector.WEBCAM_NAME);
        } else {
            vuforia.init(hardwareMap, VuforiaKey.VUFORIA_KEY);
        }

        detector.init(hardwareMap, vuforia.getVuforia());
    }

    public void waitForSeconds(double seconds) {
        timer.reset();
        while (timer.seconds() < seconds) {
            telemetry.addData("Time", seconds - timer.seconds());
            telemetry.update();
        }
    }

    public boolean findGoldMineralFromSide(double timeoutS, TFODSide side, float min, float max) {
        timer.reset();
        while (timer.seconds() < timeoutS) {
            recognitions = detector.getDetector().getUpdatedRecognitions();
            for (Recognition recognition : recognitions) {
                if (recognition.getLabel().equals(TensorFlowDetector.LABEL_GOLD_MINERAL)) {
                    switch (side) {
                        case TOP:
                            if (recognition.getTop() > min && recognition.getTop() < max) {
                                return true;
                            }
                            break;
                        case BOTTOM:
                            if (recognition.getBottom() > min && recognition.getBottom() < max) {
                                return true;
                            }
                            break;
                        case LEFT:
                            if (recognition.getLeft() > min && recognition.getLeft() < max) {
                                return true;
                            }
                            break;
                        case RIGHT:
                            if (recognition.getRight() > min && recognition.getRight() < max) {
                                return true;
                            }
                            break;
                    }
                }
            }
        }

        return false;
    }

    public MineralPosition findGoldMineralFromPosition(double timeoutS) {
        int goldMineralX;
        int silverMineral1X;
        int silverMineral2X;

        timer.reset();
        while (timer.seconds() < timeoutS) {
            recognitions = detector.getDetector().getUpdatedRecognitions();
            if (recognitions.size() == 3) {
                goldMineralX = -1;
                silverMineral1X = -1;
                silverMineral2X = -1;
                for (Recognition recognition : recognitions) {
                    if (recognition.getLabel().equals(TensorFlowDetector.LABEL_GOLD_MINERAL)) {
                        goldMineralX = (int) recognition.getLeft();
                    } else if (silverMineral1X == -1) {
                        silverMineral1X = (int) recognition.getLeft();
                    } else {
                        silverMineral2X = (int) recognition.getLeft();
                    }
                }

                if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                    if (goldMineralX < silverMineral1X && goldMineralX < silverMineral1X) {
                        return MineralPosition.LEFT;
                    } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                        return MineralPosition.RIGHT;
                    } else {
                        return MineralPosition.MIDDLE;
                    }
                }
            }
        }

        return MineralPosition.NONE;
    }
}
