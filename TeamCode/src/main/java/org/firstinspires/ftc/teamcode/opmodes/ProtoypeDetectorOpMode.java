package org.firstinspires.ftc.teamcode.opmodes;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.PrototypeDetector;

/**
 * This class implements the prototype detector which uses OpenCv for its computer vision library,
 * and while the program runs, the detector activates and can be seen on the robot controller.
 * In this case, the class is an example of a program using OpenCv for computer vision.
 *
 * @author Henry
 * @version 0.1
 */
@TeleOp(name = "Prototype Detector - 0.1", group = "prototype")
public class ProtoypeDetectorOpMode extends OpMode {

    private PrototypeDetector detector;

    @Override
    public void init() {
        // Creates a new detector
        detector = new PrototypeDetector();

        // Initializes the detector so the camera can use the detector
        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());

        // Enable the detector which the detector uses the camera
        detector.enable();
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        telemetry.addData("IsDetectedGold", detector.getDetectedGold() ? "detected" : "not detected");
    }

    @Override
    public void stop() {
        detector.disable();
    }
}
