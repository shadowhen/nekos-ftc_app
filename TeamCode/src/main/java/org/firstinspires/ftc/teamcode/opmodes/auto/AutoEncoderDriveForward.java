package org.firstinspires.ftc.teamcode.opmodes.auto;

public class AutoEncoderDriveForward extends AutoOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        robot.moveByEncoder(0.4, 100, 100, 5);
    }
}
