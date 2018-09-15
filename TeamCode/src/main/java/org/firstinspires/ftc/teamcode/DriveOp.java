package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Drive Op")
public class DriveOp extends OpMode {

    private static final double DRIVE_SPEED = 0.5;

    // Drive motors
    private DcMotor motorDriveLeftFront;
    private DcMotor motorDriveLeftRear;
    private DcMotor motorDriveRightFront;
    private DcMotor motorDriveRightRear;

    private boolean sidewaysMovement;
    private boolean sidewaysMovementButtonDown;

    @Override
    public void init() {
        motorDriveLeftFront  = hardwareMap.get(DcMotor.class, "motor_drive_lf");
        motorDriveLeftRear   = hardwareMap.get(DcMotor.class, "motor_drive_lr");
        motorDriveRightFront = hardwareMap.get(DcMotor.class, "motor_drive_rf");
        motorDriveRightRear  = hardwareMap.get(DcMotor.class, "motor_drive_rr");

        motorDriveRightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        motorDriveRightRear.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addData(">", "ROBOT READY");
        telemetry.update();
    }

    @Override
    public void loop() {
        // Get joystick y value from gamepad one
        float joystickLeftYOne = gamepad1.left_stick_y;
        float joystickRightYOne = gamepad1.right_stick_y;

        // Switches from normal to sideways movement
        if (gamepad1.a && !sidewaysMovementButtonDown) {
            sidewaysMovement = !sidewaysMovement;
            sidewaysMovementButtonDown = true;
        } else if (!gamepad1.a) {
            sidewaysMovementButtonDown = false;
        }

        // Set the power of the drive motors
        if (gamepad1.dpad_up) {
            motorDriveLeftFront.setPower(DRIVE_SPEED);
            motorDriveLeftRear.setPower(DRIVE_SPEED);
            motorDriveRightFront.setPower(DRIVE_SPEED);
            motorDriveRightRear.setPower(DRIVE_SPEED);
        } else if (gamepad1.dpad_down) {
            motorDriveLeftFront.setPower(-DRIVE_SPEED);
            motorDriveLeftRear.setPower(-DRIVE_SPEED);
            motorDriveRightFront.setPower(-DRIVE_SPEED);
            motorDriveRightRear.setPower(-DRIVE_SPEED);

        } else if (gamepad1.dpad_left) {
            motorDriveLeftFront.setPower(-DRIVE_SPEED);
            motorDriveLeftRear.setPower(DRIVE_SPEED);
            motorDriveRightFront.setPower(DRIVE_SPEED);
            motorDriveRightRear.setPower(-DRIVE_SPEED);

        } else if (gamepad1.dpad_right) {
            motorDriveLeftFront.setPower(DRIVE_SPEED);
            motorDriveLeftRear.setPower(-DRIVE_SPEED);
            motorDriveRightFront.setPower(-DRIVE_SPEED);
            motorDriveRightRear.setPower(DRIVE_SPEED);

        } else if (sidewaysMovement) {
            motorDriveLeftFront.setPower(joystickLeftYOne);
            motorDriveLeftRear.setPower(-joystickLeftYOne);
            motorDriveRightFront.setPower(-joystickRightYOne);
            motorDriveRightRear.setPower(joystickRightYOne);

        } else {
            motorDriveLeftFront.setPower(joystickLeftYOne);
            motorDriveLeftRear.setPower(joystickLeftYOne);
            motorDriveRightFront.setPower(joystickRightYOne);
            motorDriveRightRear.setPower(joystickRightYOne);

        }

        telemetry.addData("LF Power", motorDriveLeftFront.getPower());
        telemetry.addData("LR Power", motorDriveLeftRear.getPower());
        telemetry.addData("RF Power", motorDriveRightFront.getPower());
        telemetry.addData("RR Power", motorDriveRightRear.getPower());
        telemetry.update();
    }
}
