package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;

@TeleOp
public class HardwareTest extends LinearOpMode {

    private CRServo crServo;
    private ColorSensor colorSensor;

    private boolean ledState = false;

    public void runOpMode() {
        crServo = hardwareMap.crservo.get("servolowerarm");
        colorSensor = hardwareMap.colorSensor.get("colorsensor");

        colorSensor.enableLed(ledState);

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.right_stick_y > 0.2) {
                crServo.setPower(0.2);
            } else if (gamepad2.right_stick_y < -0.2) {
                crServo.setPower(-0.2);
            } else {
                crServo.setPower(0.0);
            }

            if (gamepad1.x) {
                ledState = !ledState;
            }

            colorSensor.enableLed(ledState);

            telemetry.addData("LED", ledState ? "ON" : "OFF");
            telemetry.addData("Power", crServo.getPower());
            telemetry.update();
        }

        crServo.close();
    }
}
