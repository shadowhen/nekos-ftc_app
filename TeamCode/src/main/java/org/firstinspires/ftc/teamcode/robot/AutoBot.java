package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbDeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.vuforia.BotVuforia;

import java.util.Locale;

public class AutoBot extends Bot implements RobotAuto {

    private ElapsedTime timer;

    // Sensors
    private ModernRoboticsUsbDeviceInterfaceModule cdim;
    private ModernRoboticsI2cGyro gyro;
    private ColorSensor colorSensor;

    // Vuforia
    private RelicRecoveryVuMark vuMark;
    private BotVuforia vuforia;

    // Math for target position system
    private static final double COUNTS_PER_REV = 1120; // Counts Per Revolution from Tetrix Motor
    private static final double WHEELS_DIAMETER_MM = 83.5;
    private static final double COUNTS_PER_MM = COUNTS_PER_REV / (WHEELS_DIAMETER_MM * Math.PI);

    public AutoBot() {
        super();
    }

    public enum Jewel {
        RED, BLUE, UNKNOWN
    }

    public enum GlyphControl {
        GRAB, RELEASE, LIFT, LOWER;
    }

    @Override
    public void init(HardwareMap ahwMap, Telemetry _telemetry) {
        super.init(ahwMap, _telemetry);

        telemetry.addData("Status", "Initializing");
        telemetry.update();

        // Initializing and creating new object of ElapsedTime
        timer = new ElapsedTime();

        // Get hardware references for the robot's hardware and electronic components
        cdim = (ModernRoboticsUsbDeviceInterfaceModule)
                hwMap.get(DeviceInterfaceModule.class,"Device Interface Module 1");
        gyro = (ModernRoboticsI2cGyro)hwMap.get(GyroSensor.class, "gyro");
        colorSensor = hwMap.colorSensor.get("colorsensor");

        for (int i = 0; i < getDriveMotors().length; i++){
            getDriveMotors()[i].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            getDriveMotors()[i].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            getDriveMotors()[i].setPower(0.00);
        }

        while (getGyro().isCalibrating()) {
            telemetry.addData(">", "calibrating gyro sensor");
            telemetry.update();
        }

        // Allows robot to use vuforia as its pictograph scanner
        vuforia = new BotVuforia(hwMap);
        vuforia.activateRelicTrackables(); // Allows vuforia to locate, scan, and track pictographs

        telemetry.addData(">", "Press START to activate the robot.");
        telemetry.update();
    }

    @Override
    public void close() {
        super.close();

        // Close all hardware devices
        colorSensor.close();
        gyro.close();
        cdim.close();

        // Disables tracking relic vuMarks
        vuforia.deactivatRelicTrackables();
    }

    ////////////////////////////////////////////////////////////////////////
    //---------------------------Auto Movement----------------------------//
    ////////////////////////////////////////////////////////////////////////

    public void moveByEncoders(double speed, double leftDist, double rightDist) {
        moveByEncoders(speed, leftDist, rightDist, 5);
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
    public void moveByEncoders(double speed, double leftDist, double rightDist, double timeoutS) {
        int[] currentTarget   = new int[4];
        int[] currentPosition = new int[4];

        boolean running = true;

        // Set the target position of the drive motors
        for (int i = 0; i < getDriveMotors().length; i++){
            getDriveMotors()[i].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            getDriveMotors()[i].setMode(DcMotor.RunMode.RUN_TO_POSITION);

            if (i % 2 == 0){
                getDriveMotors()[i].setTargetPosition(getDriveMotors()[i].getCurrentPosition()
                        + (int)(leftDist * COUNTS_PER_MM));
            } else if (i % 2 != 0){
                getDriveMotors()[i].setTargetPosition(getDriveMotors()[i].getCurrentPosition()
                        + (int)(rightDist * COUNTS_PER_MM));
            }
        }

        // Set the absolute (non-negative) speed of the drive motors
        setDrivePower(Math.abs(speed), Math.abs(speed));

        timer.reset();

        // The robot drives to its target position until one of
        // the drive motors' position reaches its target.
        while ((timer.seconds() < timeoutS) && running){
            // Gets the current position values from the drive motors
            for (int i = 0; i < getDriveMotors().length; i++){
                // Set the absolute speed of the drive motors since they move
                // according to the target position
                getDriveMotors()[i].setPower(Math.abs(speed));

                // Get position and target values from the drive motors
                currentPosition[i] = getDriveMotors()[i].getCurrentPosition();
                currentTarget[i]   = getDriveMotors()[i].getTargetPosition();

                if (!getDriveMotors()[i].isBusy()){
                    running = false;
                }
            }

            telemetry.addData("Current Position", "%d %d %d %d",
                    currentPosition[0], currentPosition[1],
                    currentPosition[2], currentPosition[3]);
            telemetry.addData("Current Target", "%d %d %d %d",
                    currentTarget[0], currentTarget[1],
                    currentTarget[2], currentTarget[3]);
            telemetry.update();
        }

        for (int i = 0; i < getDriveMotors().length; i++) {
            // Stops the drive motors
            getDriveMotors()[i].setPower(0);

            // Disable RUN_TO_POSITION mode on the driver motors
            getDriveMotors()[i].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //---------------------------Glyph Control-----------------------------//
    /////////////////////////////////////////////////////////////////////////

    /**
     * Pulls in the paddleServos to grab the glyph
     */
    public void grabGlyph(){
        telemetry.log().add("Grabbing glyph");
        setPaddlePosition(0.4);
    }

    /**
     * Pulls back the paddleServos to release the glyph
     */
    public void releaseGlyph(){
        telemetry.log().add("Releasing glyph");
        setPaddlePosition(-0.4); // Lets go the glyph
    }

    /**
     * The arm lifts the glyph at certain speed and time
     * @param speed   Arm Speed
     * @param seconds Time before the arm stops lifting
     */
    public void liftGlyph(double speed, double seconds){
        timer.reset();
        telemetry.log().add("Raising glyph");
        while (timer.seconds() < seconds){
            getPaddleArmMotor().setPower(Math.abs(speed));
        }
        getPaddleArmMotor().setPower(0);
    }

    /**
     * The arm lowers the glyph at certain speed and time
     * @param speed   Arm Speed
     * @param seconds Time before the arm stops lowering
     */
    public void lowerGlyph(double speed, double seconds){
        timer.reset();
        telemetry.log().add("Lowering glyph");
        while (timer.seconds() < seconds){
            getPaddleArmMotor().setPower(-Math.abs(speed));
        }
        getPaddleArmMotor().setPower(0);
    }

    /////////////////////////////////////////////////////////////////////////
    //----------------------------Color Sensor-----------------------------//
    /////////////////////////////////////////////////////////////////////////

    /**
     * Returns the color of the jewel that color sensor detects
     * @return Color of the Jewel
     */
    public Jewel getJewelColor() {
        Jewel color;

        if (colorSensor.red() > colorSensor.blue() && colorSensor.red() > colorSensor.green()) {
            color = Jewel.RED;
        } else if (colorSensor.blue() > colorSensor.red() && colorSensor.blue() > colorSensor.green()) {
            color = Jewel.BLUE;
        } else {
            color = Jewel.UNKNOWN;
        }

        return color;
    }

    /////////////////////////////////////////////////////////////////////////
    //----------------------------Gyro Sensor------------------------------//
    /////////////////////////////////////////////////////////////////////////

    public void turn(double speed, int turn){
        turn(speed, turn, 5);
    }

    /**
     * Turn the robot in angle using gyro sensor
     * @param speed Turn speed
     * @param turn Angle of the turn
     * @param timeoutS Time before exiting the loop
     */
    public void turn(double speed, int turn, double timeoutS){
        turnAbsolute(speed, turn + gyro.getIntegratedZValue(), timeoutS);
    }

    /**
     * Turns the robot in absolute angle and only right unless
     * reverse is true, which makes the robot to rurn left
     *
     * @param speed     Turn speed
     * @param turn     Angle of turn
     * @param timeoutS  Timeout seconds
     */
    public void turnAbsolute(double speed, int turn, double timeoutS){
        int zValue; // Get z value of the gyro sensor

        telemetry.log().add(String.format(Locale.ENGLISH, "Turning at %03d", turn));
        timer.reset();

        do {
            zValue = gyro.getIntegratedZValue();

            if (zValue < turn){
                setDrivePower(speed, -speed);
            } else if (zValue > turn){
                setDrivePower(-speed, speed);
            }

            telemetry.addData("Z Value", zValue);
            telemetry.addData("Target", turn);
            telemetry.update();
        } while ((zValue > (turn + 3) || zValue < (turn - 3)) && timer.seconds() < timeoutS);
    }

    /////////////////////////////////////////////////////////////////////////
    //----------------------------Vuforia----------------------------------//
    /////////////////////////////////////////////////////////////////////////

    /**
     * Search and scans the pictograph through the phone camera
     * @param timeoutS Time of scanning before ending the loop
     */
    public void scanPictograph(double timeoutS){
        timer.reset();                     // Resets the timer

        while (timer.seconds() < timeoutS){
            // Gets the vuMark from vuforia through the camera
            getVuMark();

            telemetry.addData("Timeout", timer.seconds());
            if (vuMark != RelicRecoveryVuMark.UNKNOWN){
                telemetry.addData("VuMark", "%s visable", vuMark);
            } else {
                telemetry.addData("VuMark", "not visable");
            }

            telemetry.update();
        }
    }

    /**
     * Returns Relic Recovery VuMark from the last template that vuforia detects
     * @return VuMark
     */
    public RelicRecoveryVuMark getVuMark(){
        vuforia.activateRelicTrackables();
        vuMark = RelicRecoveryVuMark.from(vuforia.getRelicTemplate());

        return vuMark;
    }

    /////////////////////////////////////////////////////////////////////////
    //------------------------Getters Hardware-----------------------------//
    /////////////////////////////////////////////////////////////////////////

    public ModernRoboticsUsbDeviceInterfaceModule getCdim() {
        return cdim;
    }

    public ModernRoboticsI2cGyro getGyro() {
        return gyro;
    }

    public ColorSensor getColorSensor() {
        return colorSensor;
    }
}
