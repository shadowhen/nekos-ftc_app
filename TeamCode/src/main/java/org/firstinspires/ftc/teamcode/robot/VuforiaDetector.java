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

    private VuforiaLocalizer vuforia;

    public void init(HardwareMap hwMap, String key, boolean useWebcam, String webcamName) {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = key;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        
        if (useWebcam) {
            parameters.cameraName = hwMap.get(WebcamName.class, webcamName);
        }

        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    public void init(HardwareMap hwMap, String key) {
        init(hwMap, key, false, null);
    }

    public VuforiaLocalizer getVuforia() {
        return vuforia;
    }
}
