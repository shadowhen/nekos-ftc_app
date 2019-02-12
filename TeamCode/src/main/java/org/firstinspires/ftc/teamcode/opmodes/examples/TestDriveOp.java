package org.firstinspires.ftc.teamcode.opmodes.examples;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * This class is an example teleop program for new interns and programmers. The program
 * serves as an example of ordinary OpMode program.
 *
 * @author Jadyn
 * @version 1.0
 */
@TeleOp(name = "Cat", group = "test")
@Disabled
public class TestDriveOp extends OpMode {

    private DcMotor motorDriveLeftFront;
    private DcMotor motorDriveLeftRear;
    private DcMotor motorDriveRightFront;
    private DcMotor motorDriveRightRear;

    @Override
    public void init() {
        motorDriveLeftFront = hardwareMap.dcMotor.get("motor_drive_lf");
        motorDriveLeftRear  = hardwareMap.dcMotor.get("motor_drive_lr");
        motorDriveRightFront = hardwareMap.dcMotor.get("motor_drive_rf");
        motorDriveRightRear  = hardwareMap.dcMotor.get("motor_drive_rr");

        motorDriveLeftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        motorDriveLeftRear.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void init_loop() {
        // Tells the user that the robot is ready to start
        telemetry.addData(">", "waiting for START...");
        telemetry.update();
    }

    @Override
    public void loop() {
        motorDriveLeftFront.setPower(gamepad1.left_stick_y);
        motorDriveLeftRear.setPower(gamepad1.left_stick_y);
        motorDriveRightFront.setPower(gamepad1.right_stick_y);
        motorDriveRightRear.setPower(gamepad1.right_stick_y);
    }
}
