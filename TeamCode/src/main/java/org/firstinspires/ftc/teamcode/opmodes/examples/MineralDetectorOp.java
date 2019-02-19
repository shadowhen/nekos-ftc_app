package org.firstinspires.ftc.teamcode.opmodes.examples;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.robot.MineralDetector;
import org.firstinspires.ftc.teamcode.robot.PrototypeDetector;

/**
 * This class implements the prototype detector which uses OpenCv for its computer vision library,
 * and while the program runs, the detector activates and can be seen on the robot controller.
 * In this case, the class is an example of a program using OpenCv for computer vision.
 *
 * @author Henry
 * @version 1.0
 */
@TeleOp(name = "Mineral Detector 1.0", group = "prototype")
public class MineralDetectorOp extends OpMode {

    private MineralDetector detector;

    @Override
    public void init() {
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");

        // Creates a new detector
        detector = new MineralDetector();

        // Initializes the detector so the camera can use the detector
        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance(), DogeCV.CameraMode.WEBCAM, false, webcamName);

        // Enable the detector which the detector uses the camera
        detector.enable();
    }

    @Override
    public void loop() {
        telemetry.addData("gold mineral x", detector.getGoldRect().x);
        telemetry.addData("silver mineral x", detector.getSilverRect().x);
    }

    @Override
    public void stop() {
        detector.disable();
    }
}
