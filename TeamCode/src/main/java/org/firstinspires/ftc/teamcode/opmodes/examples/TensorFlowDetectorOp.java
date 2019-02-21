package org.firstinspires.ftc.teamcode.opmodes.examples;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.robot.TensorFlowDetector;
import org.firstinspires.ftc.teamcode.robot.VuforiaDetector;
import org.firstinspires.ftc.teamcode.robot.VuforiaKey;

import java.util.List;

/**
 * This class implements the tensor flow object detector using vuforia and the provided FTC offical
 * assets to detect sliver and gold minerals.
 * @author Henry
 * @version 0.1
 */
@TeleOp(name = "Tensor Flow Detector - DEV")
@Disabled
public class TensorFlowDetectorOp extends LinearOpMode {

    private VuforiaDetector vuforiaDetector;
    private TensorFlowDetector tfod;

    @Override
    public void runOpMode() {
        List<Recognition> recognitions;
        boolean detectedGold = false;
        float leftMost = 0.0f;

        vuforiaDetector = new VuforiaDetector();
        tfod = new TensorFlowDetector();

        vuforiaDetector.init(hardwareMap, VuforiaKey.VUFORIA_KEY);
        //vuforiaDetector.init(hardwareMap, VuforiaKey.VUFORIA_KEY, VuforiaDetector.WEBCAM_NAME);
        tfod.init(hardwareMap, vuforiaDetector.getVuforia());

        telemetry.addData(">", "Press START to start tracking minerals");
        telemetry.update();

        // Prevents the robot from detaching the REV hub
        while (!isStarted()) {
            telemetry.addData(">", "waiting for start command");
            telemetry.update();
        }

        if (tfod.getDetector() != null) {
            tfod.getDetector().activate();
        }

        while (opModeIsActive()) {
            boolean leftMostBool = false;

            // Get new list of updated recognitions
            recognitions = tfod.getDetector().getUpdatedRecognitions();

            if (recognitions != null) {
                for (Recognition recognition : recognitions) {
                    if (recognition.getLabel().equals(TensorFlowDetector.LABEL_GOLD_MINERAL)) {
                        detectedGold = true;
                        if (!leftMostBool) {
                            leftMostBool = true;

                            // Gets the "left most value" from -90 degrees flipped phone
                            leftMost = recognition.getTop();

                            telemetry.addData("name", recognition.getLabel());
                            telemetry.addData("image", "%3d:%3d", recognition.getImageWidth(), recognition.getImageHeight());
                            telemetry.addData("top", recognition.getTop());
                            telemetry.addData("bottom", recognition.getBottom());
                            telemetry.addData("left", recognition.getLeft());
                            telemetry.addData("right", recognition.getRight());
                            telemetry.addData("width", recognition.getWidth());
                            telemetry.addData("height", recognition.getHeight());
                        }
                    }
                }

                telemetry.addData("# objects", recognitions.size());
                telemetry.addData("detected gold", detectedGold);
                telemetry.addData("leftmost", leftMost);
            }
            telemetry.update();

            detectedGold = false;
        }

        if (tfod.getDetector() != null) {
            tfod.getDetector().deactivate();
        }
    }
}
