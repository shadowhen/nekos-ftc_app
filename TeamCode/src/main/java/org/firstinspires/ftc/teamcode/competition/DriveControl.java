package org.firstinspires.ftc.teamcode.competition;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.robot.DriveBot;
import org.firstinspires.ftc.teamcode.robot.sensor.BotSensor;

import java.util.Arrays;

@TeleOp(name = "Drive Control", group = "Competition")
public class DriveControl extends OpMode {

    private DriveBot robot;
    private BotSensor sensor;

    // Control values
    private double[] joystick = new double[2];
    private double paddlePosition = 0;

    // Locks for certain controls on the gamepads
    private boolean lockedControl = false;
    private boolean lockedControlPressed = false;

    // Speed for hardware devices like the paddle servos
    private static final double DRIVE_SPEED  = 0.25;
    private static final double TURN_SPEED   = 0.25;
    private static final double PADDLE_SPEED = 0.02;

    @Override
    public void init() {
        robot = new DriveBot();
        sensor = new BotSensor();

        robot.init(hardwareMap, telemetry);
    }

    @Override
    public void init_loop() {
        //telemetry.addData("Color Sensor", sensor.getColorSensor().argb());
        telemetry.addData(">", "Press STarT");
    }

    @Override
    public void loop() {
        // Get joystick values from both gamepads
        joystick[0] = -gamepad1.left_stick_y;
        joystick[1] = -gamepad1.right_stick_y;

        limitDrivePower();  // Limits the drive power that the robot can go
        controlLock();      // Control lockout for certain controls on the gamepads

        // Controls the drive power of the robot
        controlDrivePower(joystick[0], joystick[1]);

        // Raises or lowers the paddle arm using a and y buttons on gamepad 2
        controlPaddleArm();

        // Open or closes the paddles using the left joystick and up, down, and right dpads
        // on the gamepad 2
        controlPaddleServo();

        // Controls the jewel slider by pressing x and b buttons
        // and controls the jewel arm servo by moving right joystick on gamepad 2
        controlJewelSystem();

        updateInfo(); // Updates telemetry info to the drive phone
    }

    @Override
    public void stop() {
        robot.close();
    }

    /**
     * Controls the drive power of the drive motors which the motors go
     */
    private void controlDrivePower(double leftPower, double rightPower) {
        if (gamepad1.dpad_up) {
            leftPower  =  DRIVE_SPEED;
            rightPower =  DRIVE_SPEED;
        } else if (gamepad1.dpad_down) {
            leftPower  = -DRIVE_SPEED;
            rightPower = -DRIVE_SPEED;
        } else if (gamepad1.dpad_left) {
            leftPower  = -TURN_SPEED;
            rightPower =  TURN_SPEED;
        } else if (gamepad1.dpad_right) {
            leftPower  =  TURN_SPEED;
            rightPower = -TURN_SPEED;
        }

        robot.setDrivePower(leftPower, rightPower);
    }

    private void limitDrivePower() {
        // Set the drive power limit so the drive motors would not go over the limit
        if (gamepad1.a){
            robot.setDriveSpeedLimit(1.00);
        } else if (gamepad1.b) {
            robot.setDriveSpeedLimit(0.75);
        } else if (gamepad1.y){
            robot.setDriveSpeedLimit(0.50);
        } else if (gamepad1.x){
            robot.setDriveSpeedLimit(0.25);
        }
    }

    private void controlPaddleArm(){
        // Moves the paddle arm up or down
        if (gamepad2.a) {
            robot.getMotorPaddleArm().setPower(-0.6);
        } else if (gamepad2.y) {
            robot.getMotorPaddleArm().setPower(0.6);
        } else {
            robot.getMotorPaddleArm().setPower(0);
        }
    }

    /**
     * Controls the servos which opens and closes the paddles
     */
    private void controlPaddleServo() {
        // User controlled paddles (opens or closes the paddles)
        if (gamepad2.left_stick_y > 0.2) {
            paddlePosition += PADDLE_SPEED;

        } else if (gamepad2.left_stick_y < -0.2) {
            paddlePosition -= PADDLE_SPEED;

        } else if (gamepad2.dpad_up) {
            paddlePosition = -0.5;

        } else if (gamepad2.dpad_down) {
            paddlePosition = 0.5;

        } else if (gamepad2.dpad_right) {
            paddlePosition = 0.0;
        }

        // Keep the paddle position from going over a specific limit
        paddlePosition = Range.clip(paddlePosition, -0.5, 0.5);

        // Set the paddle position that opens or closes the paddles
        robot.setPaddlePosition(paddlePosition);
    }

    /**
     * Controls the lock which decides whether the user can control the jewel slider and jewel arm
     */
    private void controlLock() {
        // Locks or unlocks certain controls by pressing the dpad left on gamepad 2
        if (gamepad2.dpad_left && !lockedControlPressed) {
            lockedControl = !lockedControl;
            lockedControlPressed = true;
        } else if (!gamepad2.dpad_left && lockedControlPressed) {
            lockedControlPressed = false;
        }
    }

    /**
     * Controls the jewel slider motor and jewel CRServo arm
     */
    private void controlJewelSystem() {

        // If the user has not lock out the controls of the slider motor and jewel arm servo,
        // the user can control them unless he/she locks by pressing left bumper button on
        // gamepad 2.
        if (!lockedControl) {

            // Controls the servo jewel arm using right joystick on gamepad 2
            if (gamepad2.right_stick_y < -0.2) {
                robot.getServoJewelArm().setPower(-0.2);
            } else if (gamepad2.right_stick_y > 0.2) {
                robot.getServoJewelArm().setPower(0.2);
            } else {
                robot.getServoJewelArm().setPower(0.0);
            }

            // Controls the slider with x and b buttons on gamepad 2
            if (gamepad2.b) {
                robot.getMotorSlider().setPower(0.2);
            } else if (gamepad2.x) {
                robot.getMotorSlider().setPower(-0.2);
            } else {
                robot.getMotorSlider().setPower(0.0);
            }

        } else {
            // Prevent the slider and jewel arm from moving
            robot.getMotorSlider().setPower(0.0);
            robot.getServoJewelArm().setPower(0.0);
        }
    }

    /**
     * Sends info into telemetry and updates the telemetry to receive the latest information
     */
    private void updateInfo() {
        // Controls for driving the robot
        telemetry.addData("Speed Limit", "%.2f", robot.getDriveSpeedLimit());
        telemetry.addLine("Controls: ")
                .addData("A","1.00")
                .addData("B", "0.75")
                .addData("Y", "0.50")
                .addData("X", "0.25");

        telemetry.addLine("-------------------------");

        // Drive speed
        telemetry.addLine("Drive Speed: ")
                .addData("Left", "%.2f %.2f",
                        robot.getMotorDrive()[0].getPower(),
                        robot.getMotorDrive()[2].getPower())
                .addData("Right", "%.2f %.2f",
                        robot.getMotorDrive()[1].getPower(),
                        robot.getMotorDrive()[3].getPower());
        telemetry.addData("Controls", "%.2f %.2f", joystick[0], joystick[1]);

        telemetry.addLine("-------------------------");

        // CR Servo
        telemetry.addData("CR Servo Power", robot.getServoJewelArm().getPower());
        telemetry.addData("Controls", "Gamepad 2: Right Joystick");

        telemetry.addLine("-------------------------");

        // Paddles
        telemetry.addData("Current Position", paddlePosition);
        telemetry.addData("Paddle Position", "%.2f %.2f",
                robot.getServoPaddle()[0].getPosition(),
                robot.getServoPaddle()[1].getPosition());
        telemetry.addData("Controls", "Gamepad 2: Left Joystick");

        telemetry.addLine("-------------------------");

        telemetry.addData("Lock Control", lockedControl ? "ON" : "OFF");
        telemetry.addData("Left Trigger", gamepad1.left_trigger);
        telemetry.addData("Right Trigger", gamepad1.right_trigger);

        telemetry.addLine("-------------------------");
        //telemetry.addData("RGB", Arrays.toString(sensor.getRGB()));
        //telemetry.addData("HSV", Arrays.toString(sensor.getHSV()));

        telemetry.update();
    }
}
