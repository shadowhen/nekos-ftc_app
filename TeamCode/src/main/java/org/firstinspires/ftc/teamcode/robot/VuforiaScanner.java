package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.util.VuforiaKey;

public class VuforiaScanner {

    private VuforiaLocalizer vuforia;
    private VuforiaLocalizer.Parameters parameters;

    public VuforiaScanner() {
        this(false, null, false);
    }

    public VuforiaScanner(boolean useBackCamera) {
        this(useBackCamera, null, false);
    }

    public VuforiaScanner(boolean useBackCamera, HardwareMap hwMap, boolean useMonitorView) {
        if (hwMap != null && useMonitorView) {
            int cameraMonitorViewId = hwMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hwMap.appContext.getPackageName());
            parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        } else {
            parameters = new VuforiaLocalizer.Parameters();
        }

        parameters.vuforiaLicenseKey = VuforiaKey.KEY;

        if (useBackCamera) {
            parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        } else {
            parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        }

        vuforia = ClassFactory.createVuforiaLocalizer(parameters);


    }
}
