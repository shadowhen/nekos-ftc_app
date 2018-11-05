package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.TensorFlowDetector;
import org.firstinspires.ftc.teamcode.robot.VuforiaDetector;
import org.firstinspires.ftc.teamcode.robot.VuforiaKey;

@TeleOp(name = "Tensor FLow Detector - DEV")
public class TensorFlowDetectorOp extends LinearOpMode {

    private VuforiaDetector vuforiaDetector;
    private TensorFlowDetector tfod;

    @Override
    public void runOpMode() throws InterruptedException {
        vuforiaDetector = new VuforiaDetector();
        tfod = new TensorFlowDetector();

        vuforiaDetector.init(hardwareMap, VuforiaKey.VUFORIA_KEY);
        tfod.init(hardwareMap, vuforiaDetector.getVuforia());

        telemetry.addData(">", "Press START to start tracking minerals");
        telemetry.update();
        
        waitForStart();
    }
}
