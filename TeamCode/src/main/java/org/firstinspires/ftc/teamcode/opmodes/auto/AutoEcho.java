package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.robot.Bot;
import org.firstinspires.ftc.teamcode.robot.MineralType;
import org.firstinspires.ftc.teamcode.robot.TensorFlowDetector;

import java.util.List;

/**
 * This class implements the autonomous plan which the robot lands on the ground,
 * samples the mineral, drop the team marker in the depot, and then rush
 * towards the crater to park.
 * @author Henry
 * @version 1.0
 */
@Autonomous(name = "Auto Echo - DEPOT - RIGHT SIDE - PARKING - SAMPLING - TEAM MARKER", group = "auto")
@Disabled
public class AutoEcho extends AutoOpMode {

    private MineralType centerMineral;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        initDetector(true);

        while (!isStarted()) {
            List<Recognition> recognitions = detector.getDetector().getUpdatedRecognitions();
            if (recognitions != null && recognitions.size() == 1) {
                if (recognitions.get(0).getLabel().equals(TensorFlowDetector.LABEL_GOLD_MINERAL)) {
                    centerMineral = MineralType.GOLD;
                } else {
                    centerMineral = MineralType.SILVER;
                }
            }

            telemetry.addData("INSTRUCTIONS", "PLACE THE HOOK ON RIGHT SIDE");
            telemetry.addData(">", "waiting for start command");
            telemetry.update();
        }


        // Lands on the ground from the lander
        liftByTime(Bot.VERTICAL_RAISE_SPEED, SLEEP_LANDING);

        if (centerMineral.equals(MineralType.GOLD)) {

        } else {

        }
    }

    private void goCenter() {

    }

    private void goLeft() {

    }

    private void goRight() {

    }
}