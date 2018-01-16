package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.DriveBot;

@TeleOp(name = "Tele Test")
public class TeleTest extends LinearOpMode{

    private DriveBot robot = new DriveBot();
    private ColorSensor colorSensor;
    private boolean ledState = false;

    @Override
    public void runOpMode() throws InterruptedException {
        final double rate = 0.02;
        double postiton = 0;
        robot.init(hardwareMap, telemetry);
        colorSensor = hardwareMap.colorSensor.get("colorsensor");
        waitForStart();

        while (opModeIsActive()){
            if (gamepad1.x){
                ledState = false;
            } else if (gamepad1.y){
                ledState = true;
            }

            if (gamepad1.dpad_up){
                postiton += rate;
            }
            if (gamepad1.dpad_down){
                postiton -= rate;
            }
            postiton = Range.clip(postiton, -1, 1);

            colorSensor.enableLed(ledState);
            robot.setJewelArmPower(-gamepad1.left_stick_y);
            robot.setJewelElbowPosition(postiton);
            robot.setJewelSliderPower(-gamepad1.right_stick_y);

            telemetry.addData("LED", ledState ? "ON" : "OFF");
            telemetry.addData("Red", colorSensor.red());
            telemetry.addData("Blue", colorSensor.blue());
            telemetry.update();
        }
    }
}
