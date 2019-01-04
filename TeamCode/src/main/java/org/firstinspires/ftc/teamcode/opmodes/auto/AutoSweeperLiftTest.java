package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * The autonomous program known as Beta is designed in a way that the robot ignores the mineral
 * and deposits the team marker in the depot. After depositing the team marker, the robot makes
 * its way to the pit for parking bonus. The pit itself is always on the opposite alliance side;
 * therefore, the robot goes the farthest right of the pit. For now, the program is competition
 * ready and has a high chance of success if everything goes smoothly.
 *
 * @author Henry
 * @version 1.0
 */
@Autonomous(name = "Auto Sweeper Lift Test", group = "test")
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
