package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.robot.MineralPosition;
import org.firstinspires.ftc.teamcode.robot.TensorFlowDetector;
import org.firstinspires.ftc.teamcode.robot.VuforiaDetector;
import org.firstinspires.ftc.teamcode.robot.VuforiaKey;

import java.util.List;

/**
 * This class is an test autonomous program which the robot moves and pushes the gold mineral.
 * During autonomous mode, the robot goes by the minerals and scans the minerals until the gold
 * mineral appears on the screen. Once the robot lines up, the robot pushes the gold mineral
 * using its pusher. After the pusher pushes the gold mineral, the robot sends a telemetry
 * message to the driver controller to inform the drivers that the robot has finished its
 * autonomous tasks.
 *
 * @author Henry
 * @version 1.0
 */
@Autonomous(name = "Push the Gold Mineral", group = "test")
@Disabled
public class AutoPushGoldMineral extends AutoOpMode {

    private VuforiaDetector vuforia;
    private TensorFlowDetector detector;

    private boolean foundGold = false;
    private List<Recognition> recognitions;
    private float leftMostGold = 0.0f;

    private MineralPosition mineralPosition;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        vuforia = new VuforiaDetector();
        detector = new TensorFlowDetector();

        vuforia.init(hardwareMap, VuforiaKey.VUFORIA_KEY);
        detector.init(hardwareMap, vuforia.getVuforia());

        // Prevents the robot detaching from the REV Hub
        while (!isStarted()) {
            telemetry.addData("Goal", "Push the gold mineral!");
            telemetry.addData(">", "waiting for start command");
            telemetry.update();
        }

        // Activates the detector
        if (detector.getDetector() != null) {
            detector.getDetector().activate();
        }

        // Moves to the left sideways
        robot.moveSidewaysByEncoder(0.1, -60, 5);
        sleep(1000);

        // The robot finds the gold mineral's position using its external camera
        mineralPosition = findGoldMineralFromPosition(5);
        switch (mineralPosition) {
            case LEFT:
                robot.moveSidewaysByEncoder(0.1, -50, 5);
                robot.moveByEncoder(DRIVE_SPEED, 1010, 1010, 5);
                break;
            case RIGHT:
                robot.moveSidewaysByEncoder(0.1, 50, 5);
                robot.moveByEncoder(DRIVE_SPEED, 1010, 1010, 5);
                break;
            default:
                // If the camera cannot find the gold mineral or the find the gold mineral
                // in the center, the robot would not need to sidestep.
                robot.moveByEncoder(DRIVE_SPEED, 1010, 1010, 5);
        }

        // Deactivates the detector
        if (detector.getDetector() != null) {
            detector.getDetector().deactivate();
        }

        // Once the robot finishes its tasks, the robot controller sends telemetry message
        // informing the drivers that the robot has finished all of its autonomous tasks.
        while (opModeIsActive()) {
            telemetry.addData("Status", "Finished task!");
            telemetry.update();
        }
    }
}
