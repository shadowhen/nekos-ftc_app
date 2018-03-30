package org.firstinspires.ftc.teamcode.competition;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.AutoBot;

@Autonomous(name = "Auto Demo", group = "Competition")
public class AutoDemo extends LinearOpMode {

    private AutoBot robot = new AutoBot();

    private double newDist; // Distance for encoders
    private int    newTurn; // Turn for gyro

    @Override
    public void runOpMode(){
        robot.init(hardwareMap, telemetry);
        waitForStart();

        // closes the paddles to grab the glyph
        robot.closePaddles();

        // delay
        sleep(500);

        /*robot.moveJewelSlider(0.3, 2);  // slides the arm out

        // CR servo arm movement
        if (robot.isJewelColor(BotSensor.Jewel.BLUE, 1)){
            robot.moveJewelArm(0.5, 1);
        } else if (robot.isJewelColor(BotSensor.Jewel.RED, 1)){
            robot.moveJewelArm(-0.5, 1);
        }

        robot.moveJewelSlider(-0.3, 2.5);*/ // slides the arm in
        //robot.movePaddleArm(0.5, 3); // raises the paddle arm up

        // raise the arm
        robot.getMotorPaddleArm().setPower(0.3);
        sleep(2000);
        robot.getMotorPaddleArm().setPower(0);

        sleep(500);

        // check for pictograph
        switch (robot.read(5)){
            case RIGHT:
                newDist = 530;
                newTurn = 11;
                break;
            case CENTER:
                newDist = 480;
                newTurn = 6;
                break;
            case LEFT:
                newDist = 420;
                newTurn = 3;
                break;
            default:
                newDist = 420;
                newTurn = 3;
        }

        sleep(500);

        // turns the robot based on cryptokey from the pictograph
        robot.turn(0.3 , newTurn, 5);

        sleep(1000);

        // move up to the box
        robot.moveByEncoders(0.4, newDist, newDist, 5);

        sleep(500);

        // lower the arm to put down the glpyh
        robot.getMotorPaddleArm().setPower(-0.3);
        sleep(1000);
        robot.getMotorPaddleArm().setPower(0);

        //robot.movePaddleArm(-0.1, 1); // lowers the arm in order to drop the glyph

        sleep(500);

        // releases the glpyh
        robot.openPaddles();

        sleep(500);

        robot.moveByEncoders(0.4, 100, 100, 5);

        sleep(500);

        robot.moveByEncoders(0.4, -50, -50, 5);
        robot.close(); // cleanup code
    }
}
