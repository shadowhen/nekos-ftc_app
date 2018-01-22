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

        robot.moveByEncoders(0.3, 200, 200);
        if (robot.detectBlue()){
            robot.turnByGyro(0.1, 90);
        } else if (robot.detectRed()){
            robot.turnByGyro(0.1, -90);
        }
        robot.moveByEncoders(0.3, 200, 200);
    }
}
