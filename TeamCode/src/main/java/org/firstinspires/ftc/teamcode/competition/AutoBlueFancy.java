package org.firstinspires.ftc.teamcode.competition;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.AutoBot;
import org.firstinspires.ftc.teamcode.robot.sensor.BotSensor;

@Autonomous(name = "Competition: Blue Right Side (Fancy)", group = "Competition")
public class AutoBlueFancy extends LinearOpMode {

    private AutoBot robot = new AutoBot();

    private double newDist; // Distance for encoders
    private int    newTurn; // Turn for gyro

    @Override
    public void runOpMode(){
        robot.init(hardwareMap, telemetry);
        waitForStart();

        // Attempt to knock off the opposite alliance jewel
        // Pull the slide out
        robot.getMotorSlider().setPower(-0.2);
        sleep(3050);
        robot.getMotorSlider().setPower(0);
        sleep(500);

        // Down the jewel rod arm
        robot.getServoJewelArm().setPower(0.5);
        sleep(550);
        robot.getServoJewelArm().setPower(0.0);

        sleep(500);

        // Pull slide litte more out
        robot.getMotorSlider().setPower(-0.2);
        sleep(2900);
        robot.getMotorSlider().setPower(0);

        // Swing the red jewel with rod
        switch (robot.isJewel(BotSensor.Jewel.RED, 5)) {
            case BLUE:
                robot.getServoJewelArm().setPower(0.5);
                sleep(575);
            case RED:
                robot.getServoJewelArm().setPower(-0.5);
                sleep(575);
        }
        robot.getServoJewelArm().setPower(0);

        // pulls slide in
        robot.getMotorSlider().setPower(0.2);
        sleep(2900);
        robot.getMotorSlider().setPower(0);

        // upward the arm
        robot.getServoJewelArm().setPower(-0.5);
        sleep(500);
        robot.getServoJewelArm().setPower(0);

        // pulls slide in little more
        robot.getMotorSlider().setPower(0.2);
        sleep(3050);
        robot.getMotorSlider().setPower(0);

        sleep(500);

        // grab glyph
        robot.closePaddles(0.4);    // closes the paddles to grab the glyph

        sleep(250);

        // read key
        switch (robot.read(5)){
            case RIGHT:
                newDist = 582;
                newTurn = -10;
                break;
            case CENTER:
                newDist = 626;
                newTurn = -7;
                break;
            case LEFT:
                newDist = 750;
                newTurn = -3;
                break;
            default:
                newDist = 582;
                newTurn = -10;
        }

        // Go certain distance to line up with the cryptobox
        robot.moveByEncoders(0.4, newDist, newDist, 5);

        sleep(1000);

        // Turns left to face the box
        robot.turn(0.2, 60, 5);

        sleep(1000);

        // move forward towards box
        robot.moveByEncoders(0.4, 100, 100, 5);

        sleep(500);

        // drop arm
        robot.getMotorPaddleArm().setPower(-0.3);
        sleep(1000);
        robot.getMotorPaddleArm().setPower(0);

        //robot.movePaddleArm(-0.1, 1); // lowers the arm in order to drop the glyph

        // releases the glpyh
        robot.openPaddles();
        sleep(500);

        // push in
        robot.moveByEncoders(0.4, 100, 100, 5);

        sleep(500);

        // back up
        robot.moveByEncoders(0.4, -50, -50, 5);
        robot.close(); // cleanup code

        /*robot.moveJewelSlider(0.3, 2);  // slides the arm out

        // CR servo arm movement
        if (robot.isJewelColor(BotSensor.Jewel.BLUE, 3)){
            robot.moveJewelArm(0.5, 1);
        } else if (robot.isJewelColor(BotSensor.Jewel.RED, 3)){
            robot.moveJewelArm(-0.5, 1);
        }

        robot.moveJewelSlider(-0.3, 2.5); // slides the arm in
        //robot.movePaddleArm(0.5, 3); // raises the paddle arm up

        robot.getMotorPaddleArm().setPower(0.3);
        sleep(1000);
        robot.getMotorPaddleArm().setPower(0);*/

        // reading vuforia
        /*switch (robot.read(5)) {
            case LEFT:
                newDist = 406;
                newTurn = -10;
                break;
            case CENTER:
                newDist = 711;
                newTurn = -7;
                break;
            case RIGHT:
                newDist = 991;
                newTurn = -3;
                break;
            default:
                newDist = 406;
                newTurn = -10;
        }*/

        // turns the robot based on cryptokey from the pictograph
        /*robot.turn(0.3 , newTurn, 5);
        sleep(1000);*/

    }
}
