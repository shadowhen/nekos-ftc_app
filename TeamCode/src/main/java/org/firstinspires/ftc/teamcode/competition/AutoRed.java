package org.firstinspires.ftc.teamcode.competition;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.AutoBot;

@Autonomous(name = "Red Right Side")
//@Disabled
public class AutoRed extends LinearOpMode {

    private AutoBot robot = new AutoBot();

    @Override
    public void runOpMode(){
        robot.init(hardwareMap, telemetry);

        waitForStart(); // Waits until START button is pressed

        robot.clearLog(); // Clears the logs

        robot.grabGlyph();
        sleep(1000);

        robot.scanForKey(5);
        robot.liftGlyph(0.25, 4);

        switch (robot.getVuMark()){
            case LEFT:
                robot.turnByGyro(0.1 ,-115, 5);
                sleep(1000);

                robot.moveByEncoders(0.4, 600, 600, 5);
                break;
            case CENTER:
                robot.turnByGyro(0.1 ,-105, 5);
                sleep(1000);

                robot.moveByEncoders(0.4, 580, 580, 5);
                break;
            case RIGHT:
                robot.turnByGyro(0.1 ,-95, 5);
                sleep(1000);

                robot.moveByEncoders(0.4, 560, 560, 5);
                break;
            default:
                robot.turnByGyro(0.1 ,-95, 5);
                sleep(1000);

                robot.moveByEncoders(0.4, 600, 600, 5);
        }

        robot.releaseGlyph();
        sleep(1000);

        robot.moveByEncoders(0.4, -100, -100, 5);

        while (opModeIsActive()){
            telemetry.addData("VuMark", robot.getVuMark());
            telemetry.update();
        }
    }
}
