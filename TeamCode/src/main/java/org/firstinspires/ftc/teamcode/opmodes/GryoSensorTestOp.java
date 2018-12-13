package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.opmodes.auto.AutoOpMode;

@TeleOp(name = "Gyro Sensor Test", group = "test")
public class GryoSensorTestOp extends AutoOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        telemetry.addData(">", "Robot is ready.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // The drive presses the buttons which the robot turns using the gyro sensor
            if (gamepad1.x) {
                robot.turnByGyro(0.4, 90, 3, 5);
            } else if (gamepad1.b) {
                robot.turnByGyro(0.4, -90, 3, 5);
            }

            telemetry.addData("heading", robot.getSensors().getGyro().getHeading());
            telemetry.addData("integrated z", robot.getSensors().getGyro().getIntegratedZValue());
            telemetry.update();
        }
    }
}