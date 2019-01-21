package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.robot.AutoBot;

@TeleOp(name = "REV Hub IMU", group = "test")
public class RevHubImuOp extends LinearOpMode {

    private static final double TURN_SPEED = 0.25;

    private AutoBot robot;

    private BNO055IMU imu;
    private Orientation lastAngles = new Orientation();
    private double globalAngle;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new AutoBot();
        robot.init(hardwareMap, telemetry);
        robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);

        setupImu();

        while (!isStarted()) {
            telemetry.addData("Status", "waiting for start");
            telemetry.addData("imu calib status", imu.getCalibrationStatus().toString());
            telemetry.update();
        }

        while (opModeIsActive()) {
            boolean aButton = gamepad1.a;
            boolean bButton = gamepad1.b;
            boolean xButton = gamepad1.x;

            if (aButton && !bButton) {
                rotate(-90, TURN_SPEED);
            } else if (!aButton && bButton) {
                rotate(90, TURN_SPEED);
            }

            if (xButton) {
                resetAngle();
            }

            telemetry.addData("a button", aButton);
            telemetry.addData("b button", bButton);
            telemetry.addData("x button", xButton);
            telemetry.update();
        }
    }

    private void setupImu() {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        telemetry.addData("Status", "calibrating...");
        telemetry.update();

        while (!isStopRequested() && !imu.isGyroCalibrated()) {
            sleep(50);
            idle();
        }
    }

    private double getAngle() {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double deltaAngle = angles.firstAngle -lastAngles.firstAngle;
        if (deltaAngle < -180) {
            deltaAngle += 360;
        } else if (deltaAngle > 180) {
            deltaAngle -= 360;
        }

        globalAngle += deltaAngle;
        lastAngles = angles;

        return globalAngle;
    }

    private double checkDirection() {
        double correction, angle, gain = .10;
        angle = getAngle();
        if (angle == 0) {
            correction = 0;
        } else {
            correction = -angle;
        }
        correction = correction * gain;

        return correction;
    }

    private void resetAngle() {
        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        globalAngle = 0;
    }

    private void rotate(int degrees, double power) {
        double leftPower = 0, rightPower = 0;
        resetAngle();
        if (degrees < 0) {
            leftPower = -power;
            rightPower = power;
        } else if (degrees > 0) {
            leftPower = power;
            rightPower = -power;
        }

        robot.setDrivePower(leftPower, rightPower);

        if (degrees < 0) {
            while (opModeIsActive() && ((getAngle() == 0) || (getAngle() > degrees))) {
                telemetry.addData("Status", "turning right");
                telemetry.update();
            }
        } else {
            while (opModeIsActive() && (getAngle() > degrees)) {
                telemetry.addData("Status", "turning left");
                telemetry.update();
            }
        }

        robot.setDrivePower(0, 0);
        sleep(1000);
        resetAngle();
    }
}
