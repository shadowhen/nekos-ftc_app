package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.robot.sensor.BotSensor;
import org.firstinspires.ftc.teamcode.robot.sensor.BotVuforia;

import java.util.Arrays;
import java.util.Locale;

public class AutoBot extends Bot {

    private BotSensor sensor;
    private ElapsedTime timer;

    // Vuforia
    private RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.UNKNOWN;
    private BotVuforia vuforia;

    // Values for encoder target position
    private static final double COUNTS_PER_REV = 1120; // Counts Per Revolution from Tetrix Motor
    private static final double WHEELS_DIAMETER_MM = 83.5;
    private static final double COUNTS_PER_MM = COUNTS_PER_REV / (WHEELS_DIAMETER_MM * Math.PI);

    public AutoBot() {
        super();
        sensor = new BotSensor();
        vuforia = new BotVuforia();
    }

    @Override
    public void init(HardwareMap ahwMap, Telemetry _telemetry) {
        super.init(ahwMap, _telemetry);

        timer = new ElapsedTime();

        telemetry.addData("Status", "Initializing");
        telemetry.update();

        // Reset all encoders and enables them on all drive motors
        for (int i = 0; i < getMotorDrive().length; i++){
            getMotorDrive()[i].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            getMotorDrive()[i].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            getMotorDrive()[i].setPower(0);
        }

        // Setup the sensor hardware on the robot
        sensor.setup(hwMap, telemetry, true);

        // Allows robot to use vuforia as its pictograph scanner
        vuforia.setup(hwMap, true);
        vuforia.activate(); // Allow to vuforia to track vuMarks

        telemetry.addData(">", "Press START to activate the robot.");
        telemetry.update();
    }

    @Override
    public void close() {
        // Closes all hardware connections to prevent memory leak
        super.close();
        sensor.close();

        // Disables vuforia from tracking vuMarks from the robot controller phone
        vuforia.deactivate();
    }

    /**
     * Set the target position of the drive motors using two double distance values that
     * get converted in target values for the drive motors' target position system.
     * @param leftDist  Left Distance
     * @param rightDist Right Distance
     */
    private void setTargetPosition(double leftDist, double rightDist) {
        for (int i = 0; i < getMotorDrive().length; i++) {
            getMotorDrive()[i].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            getMotorDrive()[i].setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        // Set the target position by multiplying distance with counts per mm and adding with current position of the drive motors
        getMotorDrive()[0].setTargetPosition((int)(leftDist * COUNTS_PER_MM) + getMotorDrive()[0].getCurrentPosition());
        getMotorDrive()[1].setTargetPosition((int)(rightDist * COUNTS_PER_MM) + getMotorDrive()[1].getCurrentPosition());
        getMotorDrive()[2].setTargetPosition((int)(leftDist * COUNTS_PER_MM) + getMotorDrive()[2].getCurrentPosition());
        getMotorDrive()[3].setTargetPosition((int)(rightDist * COUNTS_PER_MM) + getMotorDrive()[3].getCurrentPosition());
    }

    public void moveByEncoders(double speed, double leftDist, double rightDist, double timeoutS) {
        moveByEncoders(speed, leftDist, rightDist, timeoutS, true);
    }

    /**
     * Set the target position of the drive motors based on the distance values
     * and the drive motors run to its target position based on the target values before
     * the timeout occurs.
     *
     * @param speed     Speed of the drive motors
     * @param leftDist  Distance that the left drive motors must reach
     * @param rightDist Distance that the right drive motors must reach
     * @param timeoutS  Amount of seconds before robot reaches its target
     */
    public void moveByEncoders(double speed, double leftDist, double rightDist, double timeoutS, boolean antiVeer) {
        final double TURN_SPEED = 0.25;
        boolean running = true;

        int baseZValue, currentZValue; // Z Values from the gyro sensor
        int[] currentTarget   = new int[4];
        int[] currentPosition = new int[4];

        telemetry.log().add(String.format(Locale.ENGLISH,"Moving at path: %.2f %.2f", leftDist, rightDist));

        // Set the target position of the drive motors
        setTargetPosition(leftDist, rightDist);

        timer.reset();
        baseZValue = sensor.getGyroSensor().getIntegratedZValue();
        do {
            currentZValue = sensor.getGyroSensor().getIntegratedZValue();

            // Keeps the robot veering off its course using a gyro sensor's z value
            if (antiVeer) {
                if (currentZValue < (baseZValue - 3)) {
                    setDrivePower(TURN_SPEED, -TURN_SPEED);

                } else if (currentZValue > (baseZValue + 3)) {
                    setDrivePower(-TURN_SPEED, TURN_SPEED);

                } else {
                    setDrivePower(Math.abs(speed), Math.abs(speed));
                }
            } else {
                setDrivePower(Math.abs(speed), Math.abs(speed));
            }

            // Gets the position and target values from drive motors
            // and checks if the one of the drive motors has reached its target
            for (int i = 0; i < getMotorDrive().length; i++){
                currentPosition[i] = getMotorDrive()[i].getCurrentPosition();
                currentTarget[i]   = getMotorDrive()[i].getTargetPosition();

                // If one of the drive motor reaches its target position,
                // running would be false, ending the loop
                if (!getMotorDrive()[i].isBusy()){
                    running = false;
                    break;
                }
            }

            telemetry.addData("Current Position", "%s", Arrays.toString(currentPosition));
            telemetry.addData("Current Target", "%s", Arrays.toString(currentTarget));
            telemetry.update();
        } while ((timer.seconds() < timeoutS) && running);

        for (int i = 0; i < getMotorDrive().length; i++) {
            // Stops the drive motors
            getMotorDrive()[i].setPower(0);

            // Disable RUN_TO_POSITION mode on each driver motor
            getMotorDrive()[i].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    public void moveByTime(double speed, double seconds) {
        timer.reset();
        while (timer.seconds() < seconds) {
            setDrivePower(speed, speed);
        }
        setDrivePower(0, 0);
    }

    public void turnByEncoders(double speed, double turnDist, double timeoutS) {
        boolean running = true;
        int[] currentTarget = new int[4];
        int[] currentPosition = new int[4];

        timer.reset();

        if (turnDist != 0) {
            running = false;
            if (turnDist > 0) {
                setTargetPosition(turnDist, -turnDist);
            } else if (turnDist < 0) {
                setTargetPosition(-turnDist, turnDist);
            }

            telemetry.log().add("Turn is " + turnDist);
        } else {
            telemetry.log().add("Turn cannot be 0.");
        }

        while (timer.seconds() < timeoutS && running) {
            for (int i = 0; i < getMotorDrive().length; i++) {
                currentPosition[i] = getMotorDrive()[i].getCurrentPosition();
                currentTarget[i] = getMotorDrive()[i].getTargetPosition();

                if (!getMotorDrive()[i].isBusy()) {
                    running = false;
                }
            }

            telemetry.addData("Target", Arrays.toString(currentTarget));
            telemetry.addData("Position", Arrays.toString(currentPosition));
            telemetry.update();
        }
    }

    /**
     * Set the power of the paddle arm and stops after certain amount of milliseconds has passed
     * @param speed Arm speed
     */
    /*public void movePaddleArm(double speed, double seconds) {
        timer.reset();
        if (timer.seconds() < seconds) {
            getMotorPaddleArm().setPower(speed);
        }
        getMotorPaddleArm().setPower(0);
    }*/
    public void movePaddleArm(double speed, long ms) {
        try {
            getMotorPaddleArm().setPower(speed);
            Thread.sleep(ms); // Waits for the motor
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // To stop the arm from moving
            getMotorPaddleArm().setPower(0);
        }
    }

    /**
     * Opens the paddles out
     */
    public void openPaddles() {
        telemetry.log().add("Opening paddles");
        setPaddlePosition(-0.4);
    }

    public void closePaddles(double position) {
        setPaddlePosition(position);
    }

    /**
     * Closes the paddles in
     */
    public void closePaddles() {
        telemetry.log().add("Closing paddles");
        closePaddles(0.4);
    }

    /**
     * Moves the jewel slider in or out for certain amount of milliseconds
     * @param speed slider motor speed
     * @param ms    milliseconds in timeout
     */
    public void moveJewelSlider(double speed, double ms) {
        speed = Range.clip(speed, -0.2, 0.2);
        timer.reset();
        while (timer.milliseconds() < ms) {
            getMotorSlider().setPower(speed);
        }

        getMotorPaddleArm().setPower(0.0); // Stops the motor
    }

    /**
     * Swings the jewel arm for certain amount of milliseconds
     * @param speed Arm Speed
     * @param ms    Milliseconds in timeout
     */
    public void moveJewelArm(double speed, double ms) {
        timer.reset();
        while (timer.milliseconds() < ms) {
            getServoJewelArm().setPower(speed);

            telemetry.addData(">", "jewel arm swinging at %.2f", speed);
            telemetry.update();
        }

        getServoJewelArm().setPower(0.0); // Stops the cr servo
    }

    public boolean isBlue(double timeoutS) {
        boolean found = false;
        timer.reset();

        while (timer.seconds() < timeoutS) {
            if (getSensor().getColorSensor().red() > getSensor().getColorSensor().blue()) {
                found = false;
            } else if (getSensor().getColorSensor().blue() > getSensor().getColorSensor().red()) {
                found = true;
            }
        }

        return found;
    }

    public boolean isRed(double timeoutS) {
        boolean found = false;
        timer.reset();

        while (timer.seconds() < timeoutS) {
            if (getSensor().getColorSensor().red() > getSensor().getColorSensor().blue()) {
                found = true;
            } else if (getSensor().getColorSensor().blue() > getSensor().getColorSensor().red()) {
                found = false;
            }
        }

        return found;
    }

    /**
     * Gets boolean if the color sensor detects a red/blue jewel
     * @param color Jewel of the Jewel (ball)
     * @param timeoutS How long the loop ends to prevent a stuck loop
     * @return boolean if the color sensor sees the color of the jewel
     */
    public boolean isJewelColor(BotSensor.Jewel color, double timeoutS) {
        boolean foundColor = false; // Find any color
        timer.reset();              // Reset the timer for timeout

        do {
            // If the certain color is detected, the foundColor boolean will be true
            if (sensor.isColor(color)) {
                foundColor = true;
            }
        } while (timer.seconds() < timeoutS && !foundColor);

        return foundColor;
    }

    // Find the specific color
    public BotSensor.Jewel isJewel(BotSensor.Jewel color, double timeoutS) {
        // Current color that the color sensor detects
        BotSensor.Jewel currentColor = BotSensor.Jewel.UNKNOWN;

        // Finding the any color boolean
        boolean foundColor = false;

        // Reset timer
        timer.reset();

        while (timer.seconds() < timeoutS && !foundColor) {
            // Color sensor tries to look for certain color from the jewel
            switch (color) {
                case BLUE:
                    if (sensor.isColor(BotSensor.Jewel.BLUE)) {
                        foundColor = true;
                        currentColor = BotSensor.Jewel.BLUE;
                    } else if (sensor.isColor(BotSensor.Jewel.RED)) {
                        foundColor = true;
                        currentColor = BotSensor.Jewel.RED;
                    }
                    break;
                case RED:
                    if (sensor.isColor(BotSensor.Jewel.RED)) {
                        foundColor = true;
                        currentColor = BotSensor.Jewel.RED;
                    } else if (sensor.isColor(BotSensor.Jewel.BLUE)) {
                        foundColor = true;
                        currentColor = BotSensor.Jewel.BLUE;
                    }
                    break;
                default:
                    foundColor = true;
            }
        }

        return currentColor;
    }

    /**
     * Turn the robot in angle using gyro sensor
     * @param speed Turn speed
     * @param turn  Turn distance
     * @param timeoutS Time before exiting the loop
     */
    public void turn(double speed, int turn, double timeoutS) {
        turnAbsolute(speed, sensor.getGyroSensor().getIntegratedZValue() + turn, timeoutS);
    }

    /**
     * Turns the robot in absolute angle and only right unless
     * reverse is true, which makes the robot to rurn left
     *
     * @param speed    Turn speed
     * @param turn     Angle of turn
     * @param timeoutS Timeout seconds
     */
    public void turnAbsolute(double speed, int turn, double timeoutS) {
        int zValue; // Get z value of the gyro sensor

        telemetry.log().add(String.format(Locale.ENGLISH, "Turning at %03d", turn));
        timer.reset();

        do {
            zValue = sensor.getGyroSensor().getIntegratedZValue();

            if (zValue < turn){
                setDrivePower(speed, -speed);
            } else if (zValue > turn){
                setDrivePower(-speed, speed);
            }

            telemetry.addData("Z Value", zValue);
            telemetry.addData("Target", turn);
            telemetry.update();
        } while ((zValue > (turn + 3) || zValue < (turn - 3)) && timer.seconds() < timeoutS);

        setDrivePower(0, 0);
    }

    /**
     * Reads the vuMark from the camera
     * @param timeoutS Time of scanning before ending the loop
     * @return vuMark
     */
    public RelicRecoveryVuMark read(double timeoutS){
        boolean found = false;
        timer.reset(); // Resets the timer

        vuforia.activate();
        while (timer.seconds() < timeoutS && !found){
            vuMark = RelicRecoveryVuMark.from(vuforia.getTrackable());

            telemetry.addData("Timeout", timer.seconds());
            if (vuMark != RelicRecoveryVuMark.UNKNOWN){
                found = true;
                telemetry.addData("VuMark", "%s visable", vuMark);
            } else {
                telemetry.addData("VuMark", "not visable");
            }

            telemetry.update();
        }
        return vuMark;
    }

    public BotSensor getSensor() {
        return sensor;
    }
}
