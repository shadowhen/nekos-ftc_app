package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
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

    public enum TFODSide {
        TOP, BOTTOM, LEFT, RIGHT
    }

    public enum MineralPosition {
        LEFT, CENTER, RIGHT, NONE
    }

    public static final double DRIVE_SPEED = 0.5;
    public static final double TURN_SPEED = 0.25;
    public static final double SIDEWAYS_SPEED = 0.4;

    public static final long SLEEP_DRIVE = 500;

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

    /**
     * The robot waits for certain amount of time.
     * @param seconds Time in seconds
     */
    public void waitForSeconds(double seconds) {
        timer.reset();
        while (timer.seconds() < seconds && opModeIsActive()) {
            telemetry.addData("Waiting time", seconds - timer.seconds());
            telemetry.update();
        }
    }

    public void setLiftPower(double power, long ms) {
        robot.getLift().setLiftPower(power);
        robot.getLift().setLanderPower(power);
        sleep(ms);
        robot.getLift().setLiftPower(0.0);
        robot.getLift().setLanderPower(0.0);
    }

    /**
     * Set the power of the sweeper's lift motor using LinearOpMode's sleep method to wait
     * @param power Power
     * @param ms    Milliseconds to sleep
     */
    public void setSweeperLiftPower(double power, long ms) {
        robot.getSweeper().setLiftPower(power);
        sleep(ms);
        robot.getSweeper().setLiftPower(0.0);
    }

    /**
     * Finds the gold mineral from a camera's presecptive using its side to find a gold mineral
     * between two value.
     * @param timeoutS Timeout in seconds
     * @param side     Image side to compare distance from
     * @param min      Min
     * @param max      Max
     * @return
     */
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
        return findGoldMineralFromPosition(timeoutS, TFODSide.LEFT);
    }

    /**
     * Finds the gold mineral when the robot's camera sees three minerals which include
     * a two silver minerals and one gold mineral. The robot makes the decision which gold
     * mineral is located either on the left, center, or right. When the robot finds gold mineral
     * at one of the positions, the method returns the location of the gold mineral.
     *
     * Requirements:
     * The camera has to see TWO SILVER MINERALS and ONE GOLD MINERAL to make a decision.
     *
     * @param timeoutS Timeout in seconds
     * @return Gold Mineral Position
     */
    public MineralPosition findGoldMineralFromPosition(double timeoutS, TFODSide side) {
        // Used to determine the position of the gold mineral
        int goldMineralX;
        int silverMineral1X;
        int silverMineral2X;

        // Reset the timer, so the loop will terminate if the camera cannot find the gold mineral's
        // position
        timer.reset();
        while (timer.seconds() < timeoutS && opModeIsActive()) {
            recognitions = detector.getDetector().getUpdatedRecognitions();

            // Update the status and timeout
            telemetry.addData(">", "scanning for gold mineral");
            telemetry.addData("timeout", timeoutS - timer.seconds());


            // Because the camera looks for exactly three minerals, a number less than or greater
            // than three would greatly affect the accuracy of the position of the gold mineral.
            // While a camera could look at two minerals, the camera looks for at three
            // possible decisions which consist left, center, and right. If the camera does not
            // see exactly three minerals, the robot would have to make guess on which direction to
            // go. On the other hand, using OpenCV could avoid this problem, and the robot only
            // needs to center the camera towards the target.
            if (recognitions != null && recognitions.size() == 3) {
                goldMineralX = -1;
                silverMineral1X = -1;
                silverMineral2X = -1;
                for (Recognition recognition : recognitions) {
                    switch (side) {
                        case LEFT:
                            if (recognition.getLabel().equals(TensorFlowDetector.LABEL_GOLD_MINERAL)) {
                                goldMineralX = (int) recognition.getLeft();
                            } else if (silverMineral1X == -1) {
                                silverMineral1X = (int) recognition.getLeft();
                            } else {
                                silverMineral2X = (int) recognition.getLeft();
                            }
                            break;
                        case RIGHT:
                            if (recognition.getLabel().equals(TensorFlowDetector.LABEL_GOLD_MINERAL)) {
                                goldMineralX = (int) recognition.getRight();
                            } else if (silverMineral1X == -1) {
                                silverMineral1X = (int) recognition.getRight();
                            } else {
                                silverMineral2X = (int) recognition.getRight();
                            }
                            break;
                        case TOP:
                            if (recognition.getLabel().equals(TensorFlowDetector.LABEL_GOLD_MINERAL)) {
                                goldMineralX = (int) recognition.getTop();
                            } else if (silverMineral1X == -1) {
                                silverMineral1X = (int) recognition.getTop();
                            } else {
                                silverMineral2X = (int) recognition.getTop();
                            }
                            break;
                        case BOTTOM:
                            if (recognition.getLabel().equals(TensorFlowDetector.LABEL_GOLD_MINERAL)) {
                                goldMineralX = (int) recognition.getBottom();
                            } else if (silverMineral1X == -1) {
                                silverMineral1X = (int) recognition.getBottom();
                            } else {
                                silverMineral2X = (int) recognition.getBottom();
                            }
                            break;
                    }

                }

                if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                    if (goldMineralX < silverMineral1X && goldMineralX < silverMineral1X) {
                        return MineralPosition.LEFT;
                    } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                        return MineralPosition.RIGHT;
                    } else {
                        return MineralPosition.CENTER;
                    }
                }

                telemetry.addData("Gold Mineral", goldMineralX);
                telemetry.addData("Silver MineralX1", silverMineral1X);
                telemetry.addData("Silver MineralX2", silverMineral2X);

            }

            telemetry.update();
        }

        return MineralPosition.NONE;
    }
}
