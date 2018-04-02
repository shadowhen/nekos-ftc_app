package org.firstinspires.ftc.teamcode.robot.sensor;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * This class provides vuforia functionality that allows vuforia to access the phone
 * scans vuMark based what the phone camera sees.
 */
public class BotVuforia {

    // Localizers
    private VuforiaLocalizer vuforia;
    private VuforiaLocalizer.Parameters parameters;

    // VuMark trackables
    private VuforiaTrackables relicTrackables;
    private VuforiaTrackable relicTemplate;

    private static final String vuforiaKey = "";

    public BotVuforia() {

    }

    public void setup(HardwareMap hardwareMap) {
        setup(hardwareMap, false);
    }

    public void setup(HardwareMap hardwareMap, boolean useCamera) {

        if (useCamera) {
            // Creates a cameraMonitorViewID, so the robot controller uses the camera on the phone
            int cameraMonitorViewId = hardwareMap.appContext.getResources().
                    getIdentifier("cameraMonitorViewId", "id",
                            hardwareMap.appContext.getPackageName());

            parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        } else {
            parameters = new VuforiaLocalizer.Parameters();
        }

        // Vuforia Key used in order to use vuforia software in the robot controller phone
        parameters.vuforiaLicenseKey = vuforiaKey;

        // Uses the camera on the back of the robot controller phone
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        // Disabling extended tracking allows the robot controller to pinpoint VuMarks more
        // accurately at a cost of some distance
        parameters.useExtendedTracking = false;

        relicTrackables = vuforia.loadTrackablesFromAsset("RelicVuMark");

        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // debug
    }

    public void activate(){
        relicTrackables.activate();
    }

    public void deactivate() {
        relicTrackables.deactivate();
    }

    public VuforiaTrackable getTrackable() {
        return relicTemplate;
    }

    public String format(OpenGLMatrix transformationMatrix) {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }
}
