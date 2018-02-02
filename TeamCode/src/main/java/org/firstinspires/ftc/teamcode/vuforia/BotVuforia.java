package org.firstinspires.ftc.teamcode.vuforia;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * The robot controller phone opens and uses BotVuforia and allows
 * the BotVuforia to access the phone's back camera.
 */
public class BotVuforia {

    private VuforiaLocalizer vuforia;
    private VuforiaLocalizer.Parameters parameters;
    private VuforiaTrackables relicTrackables;
    private VuforiaTrackable relicTemplate;

    private static final String vuforiaKey = "AaMQfMP/////AAAAGQLuPVsah040rgBG6ibEjIZHVopUrLF3WdeaKkvODCWhHyb0BuB6vDgpbhn7euumA4oc5HPADqdea5FCNZRpZalYZVbZrtIdnW9xSb67rZKsM5Y6vPFczWZJWPr1MghlAs/JderV8BAY51l0daddAUgslWLifHlYBAKqpuDN2HWDOCOEfnmrh9bExxxBTadzlT4X25KW9ZbfGwQ8m00Kby2Fob9djMutOqys1QrAYMOwZ5HeEaMPkhDboXDelpLW1j1ZsuzsOYqRcwwb0mzYJE9ujitmGZHqjsHpr9fV7DuobkDmIiDOMmZdEQSHHp55umE3/xwNj3Fsx02zub7LeN8KfCpb1M51P2G7m4Jx4RKj";

    public BotVuforia() {
        this(null);
    }

    public BotVuforia(HardwareMap hwMap) {
        if (hwMap != null){
            setupVuforia(hwMap);
        }
    }

    public void setupVuforia(HardwareMap hardwareMap){
        // Creates a cameraMonitorViewID, so the robot controller uses the camera on the phone
        int cameraMonitorViewId = hardwareMap.appContext.getResources().
                getIdentifier("cameraMonitorViewId", "id",
                        hardwareMap.appContext.getPackageName());
        parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        // BotVuforia Key used in order to use vuforia software in the robot controller phone
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

    public void activateRelicTrackables(){
        relicTrackables.activate();
    }

    public void deactivatRelicTrackables() {
        relicTrackables.deactivate();
    }

    public VuforiaTrackable getRelicTemplate() {
        return relicTemplate;
    }

    public String format(OpenGLMatrix transformationMatrix) {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }
}
