package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.AutoBot;

@Autonomous(name = "Auto Test (EXPERIMENTAL)")
//@Disabled
public class AutoTest extends LinearOpMode {

    private AutoBot robot = new AutoBot();

    @Override
    public void runOpMode(){
        robot.init(hardwareMap, telemetry);
        waitForStart();
        robot.clearLog(); // Clears the logs

        /*robot.grabGlyph();
        sleep(1000);

        robot.liftGlyph(0.25, 2);
        robot.scanForKey(5);

        switch (robot.getVuMark()){
            case RIGHT:
                robot.turnByGyro(0.1 ,60, 5);
                sleep(1000);

                robot.moveByEncoders(0.4, 600, 600, 5);
                break;
            case CENTER:
                robot.turnByGyro(0.1 ,50, 5);
                sleep(1000);

                robot.moveByEncoders(0.4, 580, 580, 5);
                break;
            case LEFT:
                robot.turnByGyro(0.1 ,40, 5);
                sleep(1000);

                robot.moveByEncoders(0.4, 560, 560, 5);
                break;
            default:
                robot.turnByGyro(0.1 ,60, 5);
                sleep(1000);

                robot.moveByEncoders(0.4, 600, 600, 5);
        }

        robot.releaseGlyph();
        sleep(1000);

        robot.moveByEncoders(0.4, -100, -100, 5);

        while (opModeIsActive()){
            telemetry.addData("VuMark", robot.getVuMark());
            telemetry.update();
        }*/

        /*robot.grabGlyph();
        sleep(500);
        robot.liftGlyph(0.25, 2);

        robot.moveByEncoders(0.4, 560, 560, 5);
        robot.lowerGlyph(0.25, 2);
        robot.releaseGlyph();
        sleep(500);
        robot.moveByEncoders(0.4, -50, -50, 5);*/
    }
}
