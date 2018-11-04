package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * In this class, the robot drives forward autonomously using encoders on the driver motors which
 * move the wheel clockwise or counterclockwise.
 *
 * @author Henry
 */
@Autonomous(name = "Auto Encoder Drive Forward")
public class AutoEncoderDriveForward extends AutoOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        waitForStart();

        robot.moveByEncoder(0.4, 100, 100, 5);
    }
}
