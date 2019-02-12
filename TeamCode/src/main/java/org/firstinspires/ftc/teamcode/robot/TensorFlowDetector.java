package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

/**
 * This class implements the tensor flow object detector, so the robot controller can utilize
 * the tensor flow software.
 * @author Henry
 * @version 1.0
 */
public class TensorFlowDetector {

    // The name of the asset to use for gold and silver minerals
    public static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";

    // Labels for the minerals
    public static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    public static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    // Tensor Flow Object Detection Library
    private TFObjectDetector tfod;

    /**
     * Initialises the Tensor Flow Object Detector
     * @param hwMap   Hardware Map
     * @param vuforia Vuforia
     */
    public void init(HardwareMap hwMap, VuforiaLocalizer vuforia) {
        // Gets the id for the phone to display the inferface from tensor overflow's perspective
        int tfodMonitorViewId = hwMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hwMap.appContext.getPackageName());

        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }

    /**
     * Returns the detector
     * @return Detector
     */
    public TFObjectDetector getDetector() {
        return tfod;
    }
}
