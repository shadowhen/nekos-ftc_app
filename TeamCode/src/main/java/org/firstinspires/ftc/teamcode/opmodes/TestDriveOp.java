package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Cat")
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
    public void loop() {
        motorDriveLeftFront.setPower(gamepad1.left_stick_y);
        motorDriveLeftRear.setPower(gamepad1.left_stick_y);
        motorDriveRightFront.setPower(gamepad1.right_stick_y);
        motorDriveRightRear.setPower(gamepad1.right_stick_y);
    }
}
