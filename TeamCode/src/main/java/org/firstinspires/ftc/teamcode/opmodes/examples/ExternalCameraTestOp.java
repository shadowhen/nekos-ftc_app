package org.firstinspires.ftc.teamcode.opmodes.examples;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

/**
 * This class serves the purpose to test the external camera rather the camera on the Android
 * phone.
 *
 * @author Henry
 * @version 1.0
 */
@TeleOp(name = "External Camera Test", group = "test")
public class ExternalCameraTestOp extends LinearOpMode {

    private static final String CAMERA_NAME = "Webcam 1";

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    private boolean hasInitTfod;

    @Override
    public void runOpMode() throws InterruptedException {
        initVuforia();

        while (!isStarted()) {
            if (!hasInitTfod) {
                if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
                    initTfod();
                    hasInitTfod = true;
                }
            }
            telemetry.addData(">", "waiting for start...");
            if (!ClassFactory.getInstance().canCreateTFObjectDetector()) {
                telemetry.addData("status", "cannot create TFObjectDetector");
            } else {
                telemetry.addData("status", "can create TFObjectDetector");
            }
            telemetry.update();
        }

        while (opModeIsActive()) {
            telemetry.addData(">", "running");
            telemetry.update();
        }
    }

    private void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.cameraName = hardwareMap.get(WebcamName.class, CAMERA_NAME);
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    private void initTfod() {
        int cameraViewId = hardwareMap.appContext.getResources().
                getIdentifier("tfodMonitorViewId", "id",
                        hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters parameters = new TFObjectDetector.Parameters(cameraViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(parameters, vuforia);
    }
}
