package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

/**
 * This class utilizes OpenCV in autonomous demonstration to go left or right depending
 * where the gold mineral is placed either left or right of the silver mineral
 * @author Henry
 * @version 1.0
 */
@Autonomous(name = "Auto Demo - OpenCV", group = "demo")
@Disabled
public class AutoDemoOpenCv extends AutoOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        initOpenCv(true);
        telemetry.log().setCapacity(10);

        while (!isStarted()) {
            telemetry.addData(">", "ready to start...");
            telemetry.update();
        }

        // Enables OpenCV
        openCvDetector.enable();

        // Decides whether to move left or right depending where the gold mineral is either left
        // or right of the silver mineral
        if (openCvDetector.getGoldRect().x > openCvDetector.getSilverRect().x) {
            telemetry.log().add("gold is on the right. moving to the right,,,");
            robot.moveSidewaysByEncoder(0.5, 200, 5);
        } else if (openCvDetector.getGoldRect().x < openCvDetector.getSilverRect().x) {
            telemetry.log().add("gold is on the left. moving to the left,,,");
            robot.moveSidewaysByEncoder(0.5, -200, 5);
        } else {
            telemetry.log().add("cannot find the gold on either side...");
        }

        while (opModeIsActive()) {
            telemetry.addData("status", "demonstration has ended");
            telemetry.update();
            idle();
        }

        // Disables OpenCV
        openCvDetector.disable();
    }
}
