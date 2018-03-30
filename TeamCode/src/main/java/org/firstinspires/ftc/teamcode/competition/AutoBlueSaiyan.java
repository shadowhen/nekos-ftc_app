package org.firstinspires.ftc.teamcode.competition;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.AutoBot;
import org.firstinspires.ftc.teamcode.robot.sensor.BotSensor;

@Autonomous(name = "Competition: Blue Left Side (Super Saiyan)", group = "Competition")
public class AutoBlueSaiyan extends LinearOpMode {

    private AutoBot robot = new AutoBot();

    private double newDist; // Distance for encoders
    private int    newTurn; // Turn for gyro

    @Override
    public void runOpMode(){
        robot.init(hardwareMap, telemetry);
        waitForStart();

        // Attempt to knock off the opposite alliance jewel
        // note: negative
        // Pull the slide out
        robot.getMotorSlider().setPower(-0.2);
        sleep(3050);
        robot.getMotorSlider().setPower(0);
        sleep(500);

        // Down the jewel rod
        robot.getServoJewelArm().setPower(0.5);
        sleep(550);
        robot.getServoJewelArm().setPower(0.0);

        sleep(500);

        // Pull slide litte more out
        robot.getMotorSlider().setPower(-0.2);
        sleep(2900);
        robot.getMotorSlider().setPower(0);

        // Swing the red with rod
        switch (robot.isJewel(BotSensor.Jewel.RED, 5)) {
            case BLUE:
                robot.getServoJewelArm().setPower(0.5);
                sleep(575);
            case RED:
                robot.getServoJewelArm().setPower(-0.5);
                sleep(575);
        }
        robot.getServoJewelArm().setPower(0); // stop

        // note: positive
        // Pull slide litte more in
        robot.getMotorSlider().setPower(0.2);
        sleep(3500);
        robot.getMotorSlider().setPower(0);
        sleep(500);

        // upward the jewel rod
//        robot.getServoJewelArm().setPower(-0.5);
//        sleep(500);
//        robot.getServoJewelArm().setPower(0.0);
//
//        sleep(500);
//
//        // Pull the slide in
//        robot.getMotorSlider().setPower(0.2);
//        sleep(3050);
//        robot.getMotorSlider().setPower(0);

        // closes the paddles to grab the glyph
        robot.closePaddles();
        sleep(250);

        /*robot.moveJewelSlider(0.3, 2);  // slides the arm out

        // CR servo arm movement
        if (robot.isJewelColor(BotSensor.Jewel.BLUE, 1)){
            robot.moveJewelArm(0.5, 1);
        } else if (robot.isJewelColor(BotSensor.Jewel.RED, 1)){
            robot.moveJewelArm(-0.5, 1);
        }

        robot.moveJewelSlider(-0.3, 2.5);*/ // slides the arm in
        //robot.movePaddleArm(0.5, 3); // raises the paddle arm up

        // read key
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

        // raise the arm
        robot.getMotorPaddleArm().setPower(0.3);
        sleep(2000);
        robot.getMotorPaddleArm().setPower(0);

        // turns the robot based on cryptokey from the pictograph
        robot.turn(0.3 , newTurn, 5);
        sleep(1000);

        // go forward
        robot.moveByEncoders(0.4, newDist, newDist, 5);

        // lower paddle arm
        robot.getMotorPaddleArm().setPower(-0.3);
        sleep(1000);
        robot.getMotorPaddleArm().setPower(0);

        //robot.movePaddleArm(-0.1, 1); // lowers the arm in order to drop the glyph

        // releases the glpyh
        robot.openPaddles();
        sleep(1000);

        // push the block in
        robot.moveByEncoders(0.4, 100, 100, 5);

        sleep(500);

        // back out from the box to park
        robot.moveByEncoders(0.4, -50, -50, 5);

        robot.close(); // cleanup code
    }
}
