package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robot.AutoBot;
import org.firstinspires.ftc.teamcode.robot.sensor.BotSensor;

@Autonomous(name = "Auto Test Color Sesnor", group = "Test")
//@TeleOp
public class AutoTestTwo extends LinearOpMode {

    private ElapsedTime timer = new ElapsedTime();
    private AutoBot robot = new AutoBot();
    private boolean foundColor = false;

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, telemetry);
        waitForStart();

        // Attempt to knock off the opposite alliance jewel
        /*robot.getMotorSlider().setPower(-0.2);
        sleep(3000);
        robot.getMotorSlider().setPower(0);
        sleep(500);

        robot.getServoJewelArm().setPower(0.5);
        sleep(600);
        robot.getServoJewelArm().setPower(0.0);

        sleep(500);
        robot.getMotorSlider().setPower(-0.2);
        sleep(3200);
        robot.getMotorSlider().setPower(0);*/

        /*timer.reset();
        double timeoutS = 5;
        while (timer.seconds() < timeoutS) {
            if (robot.getSensor().getColorSensor().red() > robot.getSensor().getColorSensor().blue()) {
                robot.getServoJewelArm().setPower(0.5);
                sleep(500);
            } else if (robot.getSensor().getColorSensor().blue() > robot.getSensor().getColorSensor().red()) {
                robot.getServoJewelArm().setPower(-0.5);
                sleep(500);
            }
            robot.getServoJewelArm().setPower(0);

            telemetry.addData("Red", robot.getSensor().getColorSensor().red());
            telemetry.addData("Green", robot.getSensor().getColorSensor().green());
            telemetry.addData("Blue", robot.getSensor().getColorSensor().blue());
            telemetry.update();
        }*/

        if (robot.isRed(5)) {
            robot.getServoJewelArm().setPower(0.5);
            sleep(500);
            robot.getServoJewelArm().setPower(0);
        }

        /*switch (robot.isJewel(BotSensor.Jewel.RED, 5)) {
            case BLUE:
                robot.getServoJewelArm().setPower(0.5);
                sleep(500);
            case RED:
                robot.getServoJewelArm().setPower(-0.5);
                sleep(500);
        }
        robot.getServoJewelArm().setPower(0);*/

        /*robot.moveJewelSlider(0.2, 1500);
        robot.moveJewelArm(0.2, 1000);
        robot.moveJewelSlider(0.2, 1000);

        switch (robot.isJewel(BotSensor.Jewel.BLUE, 5)) {
            case BLUE:
                robot.moveJewelArm(-0.2, 500);
                break;
            case RED:
                robot.moveJewelArm(0.2, 500);
                break;
        }

        robot.moveJewelArm(0.2, 750);
        robot.moveJewelSlider(-0.2, 2000);

        if (isStopRequested()) {
            robot.close();
        }*/
    }
}
