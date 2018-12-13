package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.opmodes.auto.AutoOpMode;

/**
 * This class implements the autonomous turn which the drive can specified which direction
 * robot must turn to. When the drive presses the START button, the driver controls where
 * the robot must turn in certain direction using its gyro sensor. The driver can adjust
 * how much the robot turns and direction of the turn, and while the robot turns, the driver
 * can see the telemetry data from the robot controller if the methods provide the telemetry data.
 *
 * @author Henry
 * @verison 1.0
 */
@TeleOp(name = "Gyro Turn - Manual", group = "test")
public class GyroTurnOp extends AutoOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        // Calibrates the gyro sensor
        robot.getSensors().getGyro().calibrate();
        while (robot.getSensors().getGyro().isCalibrating()) {
            telemetry.addData("Status", "Calibrating gyro");
            telemetry.update();
        }

        telemetry.addData("Status", "Ready to start");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // When the player presses one of the buttons, the robot turns using its gyro sensor.
            if (gamepad1.a) {
                robot.turnByGyro(0.4, -90, 3, 5);
            } else if (gamepad1.b) {
                robot.turnByGyro(0.4, 90, 3, 5);
            }

            telemetry.addData("current heading", robot.getSensors().getGyro().getHeading());
            telemetry.addData("current intergated z", robot.getSensors().getGyro().getIntegratedZValue());
            telemetry.update();
        }
    }
}
