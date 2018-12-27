package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.robot.AutoDrive;

/**
 * In this class, the robot drives forward autonomously using encoders on the driver motors which
 * move the wheel clockwise or counterclockwise.
 *
 * @author Henry
 * @version 1.0
 */
@Autonomous(name = "Auto Encoder Drive Forward", group = "test")
public class AutoEncoderDriveForward extends AutoOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        robot.setDriveZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.setAutoDrive(AutoDrive.FORWARD);

        while (!isStarted()) {
            telemetry.addData(">", "Press START to start encoder drive forward");
            telemetry.update();
        }

        // It turned left
        //robot.moveByEncoder(DRIVE_SPEED, 500, 500, 5);

        // It went straight
        //robot.moveByEncoder(TURN_SPEED, -200, 200, 5);

        robot.setAutoDrive(AutoDrive.SIDEWAYS_LEFT);
        robot.moveByEncoder(SIDEWAYS_SPEED, 500, 500, 5);
    }
}
