package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.AutoBot;

@Autonomous(name = "Auto Linear Drive")
public class AutoLinearDriveOp extends BaseAutoOp{

    @Override
    public void runOpMode() throws InterruptedException {

        super.runOpMode();

        robot.moveByEncoder(AutoBot.DRIVE_SPEED, 200, 200, 5);

        while (opModeIsActive()) {
            telemetry.addData(">", "All commands are completed.");
            telemetry.update();

            idle();
        }
    }
}
