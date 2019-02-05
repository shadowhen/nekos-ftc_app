package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * The class provides an autonomous sweeper lift test for depositing the team marker in the depot.
 * @author Henry
 * @version 1.0
 */
@Autonomous(name = "Auto Sweeper Lift Test", group = "test")
@Disabled
public class AutoSweeperLiftTest extends AutoOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        // No reason for this line but commenting lines below may affect the chances of success
        robot.setDriveZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        robot.getLift().getLiftMotor().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.getLift().getLanderMotor().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        while (!isStarted()) {
            telemetry.addData(">", "Press START to start encoder drive forward");
            telemetry.update();
        }

        // Deposits the team marker in the depot for autonomous points
        setSweeperLiftPower(0.5, 500);
        sleep(2000);
        setSweeperLiftPower(-0.5, 500);
    }
}
