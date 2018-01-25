package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.robot.TestBot;

@TeleOp(name = "Robot Test", group = "Test")
public class RobotTest extends LinearOpMode{

    private TestBot robot;

    private double flapPosition  = 0.00;
    private double elbowPosition = 0.00;

    private static final double FLAP_SPEED  = 0.02;
    private static final double ELBOW_SPEED = 0.02;

    @Override
    public void runOpMode(){
        robot = new TestBot();
        robot.init(hardwareMap, telemetry);

        waitForStart();
        robot.setFlapPosition(0);

        while (opModeIsActive()){
            if (gamepad1.y){
                flapPosition += FLAP_SPEED;
            } else if (gamepad1.x){
                flapPosition -= FLAP_SPEED;
            }
            flapPosition = Range.clip(flapPosition, -0.5, 0.5);
            robot.setFlapPosition(flapPosition);

            if (gamepad1.dpad_up){
                elbowPosition += ELBOW_SPEED;
            } else if (gamepad1.dpad_down){
                elbowPosition -= ELBOW_SPEED;
            }
            elbowPosition = Range.clip(elbowPosition, -1.00, 1.00);
            robot.setJewelElbowPosition(elbowPosition);
        }
    }
}
