package org.firstinspires.ftc.teamcode.debug;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.AutoBot;

@TeleOp(name = "Sensor Debug", group = "Debug")
public class SensorTest extends LinearOpMode{

    private AutoBot robot = new AutoBot();
    private boolean ledState = false;

    @Override
    public void runOpMode() throws InterruptedException {
        boolean bCurState;
        boolean bLastState = false;
        robot.init(hardwareMap, telemetry);
        robot.enableLED(ledState);

        waitForStart();
        robot.clearLog();

        while (opModeIsActive()){
            bCurState = gamepad1.x;

            if (bCurState && !bLastState){
                bLastState = true;
                ledState = !ledState;
            } else if (!bCurState){
                bLastState = false;
            }

            robot.enableLED(ledState);

            telemetry.addData("Red", robot.detectRed() ? "visible" : "not visible");
            telemetry.addData("Blue", robot.detectBlue() ? "visible" : "not visible");
            telemetry.addData("LED", ledState ? "on" : "off");
            telemetry.update();
        }
    }
}
