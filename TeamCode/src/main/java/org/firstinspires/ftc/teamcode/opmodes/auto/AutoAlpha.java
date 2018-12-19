package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.robot.TensorFlowDetector;
import org.firstinspires.ftc.teamcode.robot.VuforiaDetector;
import org.firstinspires.ftc.teamcode.robot.VuforiaKey;

import java.util.List;

/**
 * This class expands the AutoOpMode.class to tell the robot to knock off the gold mineral, drop
 * its team marker, and park in one of the spots which gives the team points.
 *
 * @author Henry
 * @version 1.0
 */
@Autonomous(name = "Auto Alpha", group = "auto")
public class AutoAlpha extends AutoOpMode {

    private VuforiaDetector vuforia;
    private TensorFlowDetector detector;

    private List<Recognition> recognitions;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        vuforia = new VuforiaDetector();
        detector = new TensorFlowDetector();

        vuforia.init(hardwareMap, VuforiaKey.VUFORIA_KEY);
        detector.init(hardwareMap, vuforia.getVuforia());

        // Prevents the robot from detaching the REV hub
        while (opModeIsActive() && !isStarted()) {
            telemetry.addData(">", "waiting for start command");
            telemetry.update();
        }

        if (detector.getDetector() != null) {
            detector.getDetector().activate();
        }

        // INSTRUCTIONS FOR THIS PROGRAM
        // 1.) Lowers down the robot
        // 2.) Turn left?
        // 3.) Go straight?
        // 4.) Turn right?
        // 5.) Go straight?
        // 6.) Turn right
        // 7.) Scans the gold mineral and drive until gold mineral appears
        // 8.) Finish the driveby
        // 9.) Turn left by 45 degrees
        // 10.) Go straight?
        // 11.) Turn left
        // 12.) Drop the game market
        // 13.) Park

        // Lowers the robot using the lift. WIll add later
        // TODO: Add a function that the robot lowers itself onto the ground

        robot.turnByGyro(0.4, -90, 2, 5);
        sleep(500);
        robot.moveByEncoder(0.4, 100, 100, 5);
        sleep(500);
        robot.turnByGyro(0.4, 90, 2, 5);
        sleep(500);
        robot.moveByEncoder(0.4, 100, 100, 5);
        sleep(500);
        while (robot.onHeading(0.4, robot.getSensors().getGyro().getIntegratedZValue() + 90, 0.15)) {
            telemetry.addData("Do something", "Going to turn and scan gold minerals");
            telemetry.update();
        }

        // TODO: Implement a function that the robot would drive to its destination.
        robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.setDriveTargetPosition(100, 100);
        while (robot.isDriveMotorsBusy()) {
            recognitions = detector.getDetector().getUpdatedRecognitions();
            robot.setDrivePower(0.4, 0.4);

            for (Recognition recognition : recognitions) {
                if (recognition.getLabel().equals(TensorFlowDetector.LABEL_GOLD_MINERAL)) {
                    if (recognition.getTop() > 10.0 && recognition.getTop() < 100.0) {
                        robot.setDrivePower(0, 0);
                    }
                }
            }
        }
        // The robot drives to its destination, but the robot also scans for the gold mineral at
        // the same time. If the robot sees a gold mineral, the robot stops and then the pushes
        // the mineral away to earn some points.

        sleep(500);
        robot.turnByGyro(0.4, -45, 2, 5);
        sleep(500);
        robot.moveByEncoder(0.4, 100, 100, 5);
        sleep(500);
        robot.turnByGyro(0.4, -90, 2, 5);
        sleep(500);
        robot.moveByEncoder(0.4, 500, 500, 5);
        sleep(500);

        // Drop the team marker and be happy. Send help.

        if (detector.getDetector() != null) {
            detector.getDetector().deactivate();
        }
    }
}
