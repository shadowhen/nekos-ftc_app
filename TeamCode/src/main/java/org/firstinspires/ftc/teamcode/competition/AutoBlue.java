package org.firstinspires.ftc.teamcode.competition;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.AutoBot;

@Autonomous(name = "Competition: Blue Left Side", group = "Competition")
public class AutoBlue extends LinearOpMode {

    private AutoBot robot = new AutoBot();

    @SuppressLint("DefaultLocale")
    @Override
    public void runOpMode(){
        robot.init(hardwareMap, telemetry);

        // Calibrates and resets the gyro sensor
        robot.getGyro().calibrate();
        while (robot.getGyro().isCalibrating()){
            telemetry.addLine(String.format("Calibrating for about %.4f", time));
            telemetry.update();
        }

        telemetry.addData(">", "start");
        telemetry.update();

        waitForStart(); // Waits until START button is pressed

        telemetry.log().clear(); // Clears the logs

        robot.grabGlyph();
        sleep(1000);

        robot.scanPictograph(5);
        robot.liftGlyph(0.25, 2);

        switch (robot.getVuMark()){
            case RIGHT:
                robot.turn(0.2 ,60, 5);
                sleep(1000);

                robot.lowerGlyph(0.1, 2);
                robot.moveByEncoders(0.4, 600, 600, 5);
                break;
            case CENTER:
                robot.turn(0.2 ,50, 5);
                sleep(1000);

                robot.lowerGlyph(0.1, 2);
                robot.moveByEncoders(0.4, 580, 580, 5);
                break;
            case LEFT:
                robot.turn(0.2 ,40, 5);
                sleep(1000);

                robot.lowerGlyph(0.1, 2);
                robot.moveByEncoders(0.4, 560, 560, 5);
                break;
            default:
                robot.turn(0.2 ,40, 5);
                sleep(1000);

                robot.lowerGlyph(0.1, 2);
                robot.moveByEncoders(0.4, 600, 600, 5);
        }

        robot.releaseGlyph();
        sleep(1000);

        robot.moveByEncoders(0.4, -100, -100, 5);
        robot.close();
    }
}
