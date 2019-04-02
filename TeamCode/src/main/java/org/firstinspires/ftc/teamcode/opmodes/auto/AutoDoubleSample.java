package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

/**
 * This class implements the autonomous plan which the robot basically samples the minerals
 * on the alliance side.
 * @author Henry
 * @version 1.0
 */
@Autonomous(name = "Auto Double Sample - CRATER - LEFT SIDE - PARKING - SAMPLING - TEAM MARKER - Double Sample", group = "auto")
@Disabled
public class AutoDoubleSample extends AutoOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        initDetector(true);

        while (!isStarted()) {
            telemetry.addData("INSTRUCTIONS", "PLACE THE HOOK ON LEFT SIDE");
            telemetry.update();
            telemetry.addData(">", "waiting for start command");
        }

    }
}