package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Drive Op 1.0", group = "drive")
@Disabled
public class DriveOp extends OpMode {

    private static final double DRIVE_SPEED = 0.5;

    // Drive motors
    private DcMotor motorDriveLeftFront;
    private DcMotor motorDriveLeftRear;
    private DcMotor motorDriveRightFront;
    private DcMotor motorDriveRightRear;

    // Sideways movement
    private boolean sidewaysMovement;
    private boolean sidewaysMovementButtonDown;

    @Override
    public void init() {
        // Get hardware references from the robot controller's configuration for hardware devices
        motorDriveLeftFront  = hardwareMap.get(DcMotor.class, "motor_drive_lf");
        motorDriveLeftRear   = hardwareMap.get(DcMotor.class, "motor_drive_lr");
        motorDriveRightFront = hardwareMap.get(DcMotor.class, "motor_drive_rf");
        motorDriveRightRear  = hardwareMap.get(DcMotor.class, "motor_drive_rr");

        // Reverses the right drive motors' direction
        motorDriveRightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        motorDriveRightRear.setDirection(DcMotorSimple.Direction.REVERSE);

        // Tells the user that the robot is ready to start
        telemetry.addData(">", "ROBOT READY!!!!");
        telemetry.update();
    }

    @Override
    public void loop() {
        // Get joystick y values from gamepad one
        float joystickLeftYOne = gamepad1.left_stick_y;
        float joystickRightYOne = gamepad1.right_stick_y;

        // Get joystick x values from gamepad one
        float joystickLeftXOne = gamepad1.left_stick_x;
        float joystickRightXOne = gamepad1.right_stick_x;

        // When the user presses the button, the robot switches from normal to sideways movement
        if (gamepad1.a && !sidewaysMovementButtonDown) {
            sidewaysMovement = !sidewaysMovement;
            sidewaysMovementButtonDown = true;
        } else if (!gamepad1.a && sidewaysMovementButtonDown) {
            sidewaysMovementButtonDown = false;
        }

        if (sidewaysMovement) {
            if (gamepad1.dpad_down) {
                motorDriveLeftFront.setPower(DRIVE_SPEED);
                motorDriveLeftRear.setPower(DRIVE_SPEED);
                motorDriveRightFront.setPower(DRIVE_SPEED);
                motorDriveRightRear.setPower(DRIVE_SPEED);
            } else if (gamepad1.dpad_up) {
                motorDriveLeftFront.setPower(-DRIVE_SPEED);
                motorDriveLeftRear.setPower(-DRIVE_SPEED);
                motorDriveRightFront.setPower(-DRIVE_SPEED);
                motorDriveRightRear.setPower(-DRIVE_SPEED);

            } else if (gamepad1.dpad_left) {
                motorDriveLeftFront.setPower(DRIVE_SPEED);
                motorDriveLeftRear.setPower(-DRIVE_SPEED);
                motorDriveRightFront.setPower(-DRIVE_SPEED);
                motorDriveRightRear.setPower(DRIVE_SPEED);

            } else if (gamepad1.dpad_right) {
                motorDriveLeftFront.setPower(-DRIVE_SPEED);
                motorDriveLeftRear.setPower(DRIVE_SPEED);
                motorDriveRightFront.setPower(DRIVE_SPEED);
                motorDriveRightRear.setPower(-DRIVE_SPEED);
            } else {
                motorDriveLeftFront.setPower(-joystickLeftXOne);
                motorDriveLeftRear.setPower(joystickLeftXOne);
                motorDriveRightFront.setPower(joystickLeftXOne);
                motorDriveRightRear.setPower(-joystickLeftXOne);
            }

        } else {
            if (gamepad1.dpad_down) {
                motorDriveLeftFront.setPower(DRIVE_SPEED);
                motorDriveLeftRear.setPower(DRIVE_SPEED);
                motorDriveRightFront.setPower(DRIVE_SPEED);
                motorDriveRightRear.setPower(DRIVE_SPEED);
            } else if (gamepad1.dpad_up) {
                motorDriveLeftFront.setPower(-DRIVE_SPEED);
                motorDriveLeftRear.setPower(-DRIVE_SPEED);
                motorDriveRightFront.setPower(-DRIVE_SPEED);
                motorDriveRightRear.setPower(-DRIVE_SPEED);

            } else if (gamepad1.dpad_left) {
                motorDriveLeftFront.setPower(DRIVE_SPEED);
                motorDriveLeftRear.setPower(DRIVE_SPEED);
                motorDriveRightFront.setPower(-DRIVE_SPEED);
                motorDriveRightRear.setPower(-DRIVE_SPEED);

            } else if (gamepad1.dpad_right) {
                motorDriveLeftFront.setPower(-DRIVE_SPEED);
                motorDriveLeftRear.setPower(-DRIVE_SPEED);
                motorDriveRightFront.setPower(DRIVE_SPEED);
                motorDriveRightRear.setPower(DRIVE_SPEED);
            } else {
                // Set the drive motors' power from the joystick values
                motorDriveLeftFront.setPower(joystickLeftYOne);
                motorDriveLeftRear.setPower(joystickLeftYOne);
                motorDriveRightFront.setPower(joystickRightYOne);
                motorDriveRightRear.setPower(joystickRightYOne);
            }

        }

        telemetry.addData("LF Power", motorDriveLeftFront.getPower());
        telemetry.addData("LR Power", motorDriveLeftRear.getPower());
        telemetry.addData("RF Power", motorDriveRightFront.getPower());
        telemetry.addData("RR Power", motorDriveRightRear.getPower());
        telemetry.addData("Movement Mode", sidewaysMovement ? "Sideways" : "Normal");
        telemetry.update();
    }
}
