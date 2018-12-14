package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoOpMode;

/**
 * This class extends the AutoOpMode to implement a test program for the distance sensor.
 *
 * @author Henry
 * @version 1.0
 */
@TeleOp(name = "Distance Sensor Test")
public class DistanceSensorTestOp extends AutoOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        robot.setDriveMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData(">", "Robot is ready.");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Distance from sensor", robot.getSensors().getDistanceSensor().getDistance(DistanceUnit.MM));
            telemetry.update();
        }
    }
}
