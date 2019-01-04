package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

/**
 * This class implements the vuforia, so the robot controller can utilize the vuforia for
 * object detection and scanning keys on the wall.
 * @author Henry
 * @version 1.0
 */
public class VuforiaDetector {

    public static final String WEBCAM_NAME = "Webcam 1";
    private VuforiaLocalizer vuforia;

    /**
     * Initializes the vuforia
     *
     * @param hwMap       Hardware Map
     * @param key         Vuforia Key
     * @param webcamName  Webcam name
     */
    public void init(HardwareMap hwMap, String key, String webcamName) {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = key;

        if (!webcamName.isEmpty()) {
            WebcamName webcam = hwMap.get(WebcamName.class, webcamName);
            if (webcam.isAttached()) {
                parameters.cameraName = webcam;
            }
        } else {
            parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        }
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    public void init(HardwareMap hwMap, String key) {
        init(hwMap, key, "");
    }

    /**
     * Returns vuforia
     * @return Vuforia
     */
    public VuforiaLocalizer getVuforia() {
        return vuforia;
    }
}
