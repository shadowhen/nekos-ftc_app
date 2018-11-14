package org.firstinspires.ftc.teamcode.opmodes.auto;

public class AutoTurnOp extends AutoOpMode {

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

        robot.turnByGyro(0.4, 90, 3, 5);
        while (opModeIsActive()) {
            telemetry.addData("current heading", robot.getSensors().getGyro().getHeading());
            telemetry.addData("current intergated z", robot.getSensors().getGyro().getIntegratedZValue());
            telemetry.update();
        }
    }
}
