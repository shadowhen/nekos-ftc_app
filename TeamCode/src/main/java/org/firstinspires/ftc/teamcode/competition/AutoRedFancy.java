package org.firstinspires.ftc.teamcode.competition;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.AutoBot;
import org.firstinspires.ftc.teamcode.robot.sensor.BotSensor;

@Autonomous(name = "Competition: Red Right Side (Fancy)", group = "Competition")
public class AutoRedFancy extends LinearOpMode {

    private AutoBot robot = new AutoBot();
    private double newDist;
    private int    newTurn;

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

        // Down the arm
        robot.getMotorPaddleArm().setPower(0.5);
        sleep(550);
        robot.getMotorPaddleArm().setPower(0.0);

        sleep(500);

        // Pull slide litte more out
        robot.getMotorSlider().setPower(-0.2);
        sleep(2900);
        robot.getMotorSlider().setPower(0);

        // Swing the red
        switch (robot.isJewel(BotSensor.Jewel.RED, 5)) {
            case BLUE:
                robot.getServoJewelArm().setPower(-0.5);
                sleep(575);
            case RED:
                robot.getServoJewelArm().setPower(0.5);
                sleep(575);
        }
        robot.getServoJewelArm().setPower(0);

        robot.getMotorSlider().setPower(0.2);
        sleep(4000);
        robot.getMotorSlider().setPower(0);

        robot.closePaddles(); // grabs the glyph
        sleep(500);


        // Vuforia reading
        switch (robot.read(5)) {
            case LEFT:
                newDist = 582;
                newTurn = -10;
                break;
            case CENTER:
                newDist = 626;
                newTurn = -7;
                break;
            case RIGHT:
                newDist = 750;
                newTurn = -3;
                break;
            default:
                newDist = 582;
                newTurn = -10;
        }

        robot.closePaddles(); // grabs the glyph
        sleep(500);

        robot.getMotorPaddleArm().setPower(0.3);
        sleep(2000);
        robot.getMotorPaddleArm().setPower(0);

        ///////

        // Go forward to line up
        robot.moveByEncoders(0.4, newDist, newDist, 5);

        sleep(1000);

        // turn to line for box
        robot.turn(0.2 , -60, 5);

        sleep(1000);

        // go to box
        robot.moveByEncoders(0.4, 100, 100, 5);

        sleep(500);

        // drop arm
        robot.getMotorPaddleArm().setPower(-0.3);
        sleep(1000);
        robot.getMotorPaddleArm().setPower(0);

        // releases the glyph
        robot.openPaddles();
        sleep(500);

        // push in
        robot.moveByEncoders(0.4, 100, 100, 5);

        sleep(500);

        // back up
        robot.moveByEncoders(0.4, -50, -50, 5);
        robot.close();

        // Attempt to knock off the opposite alliance jewel
        /*robot.getMotorSlider().setPower(-0.2);
        sleep(3000);
        robot.getMotorSlider().setPower(0);
        sleep(500);

        // center the jewel arm
        robot.getServoJewelArm().setPower(0.5);
        sleep(600);
        robot.getServoJewelArm().setPower(0.0);

        // slider move out
        sleep(500);
        robot.getMotorSlider().setPower(-0.2);
        sleep(3600);
        robot.getMotorSlider().setPower(0);

        robot.isRed(5);

        // scan jewel
        switch (robot.isJewel(BotSensor.Jewel.RED, 5)) {
            case BLUE:
                // knock off scanned jewel
                robot.getServoJewelArm().setPower(0.5);
                sleep(500);
                robot.getServoJewelArm().setPower(-0.5);
                sleep(500);
            case RED:
                // knock off opposite jewel
                robot.getServoJewelArm().setPower(-0.5);
                sleep(500);
                robot.getServoJewelArm().setPower(0.5);
                sleep(500);
        }
        robot.getServoJewelArm().setPower(0);*/

        // slider back in partially
        /*robot.getMotorSlider().setPower(0.2);
        sleep(3600);
        robot.getMotorSlider().setPower(0);*/

        ////

        /*robot.moveJewelSlider(0.1, 2);  // slides the arm out

        // Checks if the robot sees either jewel
        if (robot.isJewelColor(BotSensor.Jewel.BLUE, 1)){
            robot.moveJewelArm(0.5, 1);
        } else if (robot.isJewelColor(BotSensor.Jewel.RED, 1)){
            robot.moveJewelArm(-0.5, 1);
        }

        robot.moveJewelSlider(-0.1, 2.5); // slides the arm in*/

        // raise the arm
        /*robot.getMotorPaddleArm().setPower(0.3);
        sleep(2000);
        robot.getMotorPaddleArm().setPower(0);*/

        // Decides based on the pictograph what values to use
        /*switch (robot.read(5)) {
            case LEFT:
                newDist = 500;
                newTurn = -10;
                break;
            case CENTER:
                newDist = 500;
                newTurn = -7;
                break;
            case RIGHT:
                newDist = 700;
                newTurn = -3;
                break;
            default:
                newDist = 500;
                newTurn = -3;
        }*/
    }
}
