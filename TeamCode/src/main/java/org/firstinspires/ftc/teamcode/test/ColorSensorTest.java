package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

@TeleOp(name = "Color Sensor Test")
public class ColorSensorTest extends LinearOpMode {

    private ColorSensor colorSensor;

    @Override
    public void runOpMode() throws InterruptedException {
        boolean led = false;
        colorSensor = hardwareMap.get(ColorSensor.class, "color");
        colorSensor.enableLed(true);
        colorSensor.enableLed(led);

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.x) {
                led = !led;
            }

            telemetry.addData("Red", colorSensor.red());
            telemetry.addData("Green", colorSensor.green());
            telemetry.addData("Blue", colorSensor.blue());
            telemetry.addData("ARGB", colorSensor.argb());
            telemetry.addData("Alpha", colorSensor.alpha());
            telemetry.addData("Led", led);
            telemetry.update();
        }
    }
}
