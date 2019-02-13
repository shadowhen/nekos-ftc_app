package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.robot.AutoBot;
import org.firstinspires.ftc.teamcode.robot.AutoDrive;
import org.firstinspires.ftc.teamcode.robot.MineralPosition;
import org.firstinspires.ftc.teamcode.robot.TensorFlowDetector;
import org.firstinspires.ftc.teamcode.robot.TfODSide;
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

    public static final double DRIVE_SPEED = 0.5;
    public static final double TURN_SPEED = 0.25;
    public static final double SIDEWAYS_SPEED = 0.4;

    public static final double SWEEPER_DEPLOY_SPEED = -0.5;
    public static final double SWEEPER_RETRACT_SPEED = 0.5;

    public static final long SLEEP_DRIVE = 500;
    public static final TfODSide CAMERA_SIDE = TfODSide.LEFT;

    protected ElapsedTime timer;

    // Computer Vision
    protected VuforiaDetector vuforia;
    protected TensorFlowDetector detector;

    // Handles with recognitions by Tensorflow Object Detection
    protected List<Recognition> recognitions;

    // Autonomous robot
    protected AutoBot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new AutoBot(this);
        timer = new ElapsedTime();
        vuforia = new VuforiaDetector();
        detector = new TensorFlowDetector();

        robot.init(hardwareMap, telemetry);
        robot.setAutoDrive(AutoDrive.FORWARD);

        telemetry.addData("status", "ready to start...");
        telemetry.update();
    }

    /**
     * Initializes the imu on the Rev Hub
     */
    public void initIMU() {
        // Calibrates the gyro sensor in the imu
        telemetry.addData("status", "calibrating the gyro...");
        telemetry.update();
        if (!isStopRequested() && !robot.getSensors().isGyroCalibrated()) {
            sleep(50);
            idle();
        }

        telemetry.addData("status", "ready to start...");
        telemetry.update();
    }

    /**
     * Initializes the vuforia and the tensorflow lite object detector
     * @param useWebcam
     */
    public void initDetector(boolean useWebcam) {
        telemetry.addData("status", "setting up vuforia...");
        telemetry.update();

        if (useWebcam) {
            vuforia.init(hardwareMap, VuforiaKey.VUFORIA_KEY, VuforiaDetector.WEBCAM_NAME);
        } else {
            vuforia.init(hardwareMap, VuforiaKey.VUFORIA_KEY);
        }

        detector.init(hardwareMap, vuforia.getVuforia());

        telemetry.addData("status", "ready to start...");
        telemetry.update();
    }

    /**
     * Set the lift power for certain amount of time
     * @param power Power
     * @param ms    Milliseconds to sleep
     */
    public void liftByTime(double power, long ms) {
        // Set the power for the robot to raise or lower
        robot.getLift().setLiftPower(power);

        // Sleeps for certian amount of time
        sleep(ms);

        // Zero the power to raise or lower the robot on the lander
        robot.getLift().setLiftPower(0.0);
    }

    /**
     * Set the power of the sweeper's lift motor using LinearOpMode's sleep method to wait
     * @param power Power
     * @param ms    Milliseconds to sleep
     */
    public void setSweeperLiftPower(double power, long ms) {
        // Deploys or retracts the sweeper
        robot.getSweeper().setLiftPower(power);

        // Waits for certain amount of time
        sleep(ms);

        // Zeros the power of the sweeper motor's power,
        // so the sweeper motor does not keeping going
        robot.getSweeper().setLiftPower(0.0);
    }

    public MineralPosition findGoldMineralFromPosition(double timeoutS) {
        return findGoldMineralFromPosition(timeoutS, CAMERA_SIDE);
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
    public MineralPosition findGoldMineralFromPosition(double timeoutS, TfODSide side) {
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
                // The position of minerals start off with negative number since zero is a value
                // returned by the recognitions
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

    /**
     * Scans for gold from looking at two minerals for certain amount of time.
     * @param timeoutS
     * @return Position of the gold mineral
     */
    public MineralPosition detectGoldFromTwoMinerals(double timeoutS) {
        // Detected Mineral Position for Gold
        MineralPosition mineralPosition = MineralPosition.NONE;

        // Positions of the minerals
        int goldMineralX = -1;
        int silverMineralX = -1;

        timer.reset();
        while (timer.seconds() < timeoutS && mineralPosition.equals(MineralPosition.NONE)) {
            recognitions = detector.getDetector().getRecognitions();

            // Will make a decision if the detected minerals is exactly TWO
            if (recognitions != null) {
                if (recognitions.size() == 2) {
                    for (Recognition recognition : recognitions) {
                        if (recognition.getLabel().equals(TensorFlowDetector.LABEL_GOLD_MINERAL)) {
                            goldMineralX = (int) recognition.getLeft();
                        } else {
                            silverMineralX = (int) recognition.getLeft();
                        }
                    }

                    // Determine the position based on the minerals
                    if (goldMineralX > silverMineralX) {
                        mineralPosition = MineralPosition.RIGHT;
                    } else if (goldMineralX < silverMineralX) {
                        mineralPosition = MineralPosition.LEFT;
                    }

                    telemetry.addData("mineral gold x", goldMineralX);
                    telemetry.addData("mineral silver x", silverMineralX);

                    // Reset the x-positions of the minerals
                    goldMineralX = -1;
                    silverMineralX = -1;
                }

                telemetry.addData("known minerals", recognitions.size());
            }

            telemetry.update();
        }

        return mineralPosition;
    }
}
