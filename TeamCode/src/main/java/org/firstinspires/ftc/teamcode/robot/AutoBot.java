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

public class AutoBot extends Bot {

    private ElapsedTime timer;

    // Vuforia
    private RelicRecoveryVuMark vuMark;
    private BotVuforia vuforia;

    // Sensors
    private ModernRoboticsUsbDeviceInterfaceModule cdim;
    private ModernRoboticsI2cGyro gyro;
    private ColorSensor colorSensor;

    // Math for target position system
    private static final double COUNTS_PER_REV = 1120; // Counts Per Revolution from Tetrix Motor
    private static final double WHEELS_DIAMETER_MM = 83.5;
    private static final double COUNTS_PER_MM = COUNTS_PER_REV / (WHEELS_DIAMETER_MM * Math.PI);

    // Speed for encoders
    private static final double TURN_SPEED = 0.3;

    public AutoBot() {
        super();
        timer = new ElapsedTime();
        vuforia = new BotVuforia();
    }

    private enum Jewel {
        RED_JEWEL, BLUE_JEWEL, UNKNOWN_JEWEL
    }

    @Override
    public void init(HardwareMap ahwMap, Telemetry _telemetry) {
        super.init(ahwMap, _telemetry);

        addLog("Adding new hardware");
        // Device Interface Module hardware mao
        cdim = (ModernRoboticsUsbDeviceInterfaceModule)
                hwMap.get(DeviceInterfaceModule.class,"Device Interface Module 1");

        // Sensors hardware map
        gyro = (ModernRoboticsI2cGyro)hwMap.get(GyroSensor.class, "gyro");
        colorSensor = hwMap.colorSensor.get("colorsensor");

        // Resets all encoders on the drive motors
        addLog("Resetting drive motors' encoders");
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Activates the encoders on the drive motors
        addLog("Enabling drive motors' encoders");
        setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Prevent the drive motors from making any movement
        driveMotors[0].setPower(Math.abs(0));
        driveMotors[1].setPower(Math.abs(0));
        driveMotors[2].setPower(Math.abs(0));
        driveMotors[3].setPower(Math.abs(0));

        // Allows robot to use vuforia
        addLog("Setting up vuforia");
        vuforia.setupVuforia(hwMap);

        // Calibrates and resets the gyro sensor
        addLog("Calibrating Gyro Sensor");
        gyro.calibrate();
        while (gyro.isCalibrating()){
            telemetry.addLine(String.format(Locale.ENGLISH, "Calibrating for about %.4f", timer.time()));
            telemetry.update();
        }

        telemetry.addData(">", "Press START to activate the robot.");
        telemetry.update();
    }

    //-----------------------------------------------------------------------
    //---------------------------Auto Movement-------------------------------
    //-----------------------------------------------------------------------

    /**
     * Set the target position of the drive motors using distance of millimeters.
     * <p>
     * Note: The drive motors may have target position, BUT the drive motors must be runmode
     * RUN_TO_POSITION in order to use posiiton values as moving and stop points.
     * @param leftDist  Left MM Distance
     * @param rightDist Right MM Distance
     */
    private void setTargetPosition(double leftDist, double rightDist){
        int[] newTargets = new int[4];

        newTargets[0] = driveMotors[0].getCurrentPosition() + (int)(leftDist * COUNTS_PER_MM);
        newTargets[1] = driveMotors[1].getCurrentPosition() + (int)(rightDist * COUNTS_PER_MM);
        newTargets[2] = driveMotors[2].getCurrentPosition() + (int)(leftDist * COUNTS_PER_MM);
        newTargets[3] = driveMotors[3].getCurrentPosition() + (int)(rightDist * COUNTS_PER_MM);

        driveMotors[0].setTargetPosition(newTargets[0]);
        driveMotors[1].setTargetPosition(newTargets[1]);
        driveMotors[2].setTargetPosition(newTargets[2]);
        driveMotors[3].setTargetPosition(newTargets[3]);
    }

    public void moveByEncoders(double speed, double leftDist, double rightDist){
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
    public void moveByEncoders(double speed, double leftDist, double rightDist, double timeoutS){
        int[] currentTarget = new int[4];
        int[] currentPosition = new int[4];

        // Resets the target and position values in the drive motors
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Enable RUN_TO_POSIITON mode on the drive motors
        setRunMode(DcMotor.RunMode.RUN_TO_POSITION);

        setTargetPosition(leftDist, rightDist);

        // Set the absolute speed of the drive motors
        driveMotors[0].setPower(Math.abs(speed));
        driveMotors[1].setPower(Math.abs(speed));
        driveMotors[2].setPower(Math.abs(speed));
        driveMotors[3].setPower(Math.abs(speed));

        // Resets the timer for timeout
        timer.reset();

        // The robot drives to its target position until one of
        // the drive motors' position reaches its target.
        while ((timer.seconds() < timeoutS) &&
                (driveMotors[0].isBusy() && driveMotors[1].isBusy()
                && driveMotors[2].isBusy() && driveMotors[3].isBusy())){
            // Gets the current position values from the drive motors
            for (int i = 0; i < 4; i++){
                currentPosition[i] = driveMotors[i].getCurrentPosition();
                currentTarget[i] = driveMotors[i].getTargetPosition();
            }

            // Information on the drive station
            telemetry.addData("Current Position", "%d %d %d %d",
                    currentPosition[0], currentPosition[1],
                    currentPosition[2], currentPosition[3]);
            telemetry.addData("Current Target", "%d %d %d %d",
                    currentTarget[0], currentTarget[1],
                    currentTarget[2], currentTarget[3]);
            telemetry.update();
        }

        // Stops the drive motors
        driveMotors[0].setPower(Math.abs(0));
        driveMotors[1].setPower(Math.abs(0));
        driveMotors[2].setPower(Math.abs(0));
        driveMotors[3].setPower(Math.abs(0));

        // Disable RUN_TO_POSITION mode on the driver motors
        driveMotors[0].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveMotors[1].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveMotors[2].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveMotors[3].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /**
     * The robot uses the encoder target position system to turn using millimeters to determine
     * the turn distance
     * @param speed     Turn Speed
     * @param distance  MM Distance
     * @param timeoutMS Timeout milliseconds for turn loop
     */
    public void turnByEncoder(double speed, double distance, long timeoutMS){
        int[] newTarget = new int[4];
        int[] currentTarget = new int[4];

        for (int i = 0; i < driveMotors.length; i++){
            newTarget[i] = driveMotors[i].getCurrentPosition() + (int)(distance * COUNTS_PER_MM);

            if (i == 0 || i % 2 == 0){
                driveMotors[i].setTargetPosition(newTarget[i]);
            } else {
                driveMotors[i].setTargetPosition(-newTarget[i]);
            }

            driveMotors[i].setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        timer.reset();
        while ((timer.milliseconds() < timeoutMS) && (!isReachedTarget())){
            for (int i = 0; i < driveMotors.length; i++){
                driveMotors[i].setPower(speed);
                currentTarget[i] = driveMotors[i].getCurrentPosition();
            }
            telemetry.addData("Speed", speed);
            telemetry.addData("Distance", distance);
            telemetry.addData("Target Position", "%d %d %d %d",
                    newTarget[0], newTarget[1], newTarget[2], newTarget[3]);
            telemetry.addData("Current Position", "%d %d %d %d",
                    currentTarget[0], currentTarget[1], currentTarget[2], currentTarget[3]);
            telemetry.update();
        }

        for (DcMotor motor : driveMotors){
            motor.setPower(0);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    /**
     * Returns a boolean if the one of the drive motors reach its target position
     * @return one of the drive motor reached its target
     */
    private boolean isReachedTarget(){
       return driveMotors[0].getCurrentPosition() > driveMotors[0].getTargetPosition() ||
               driveMotors[1].getCurrentPosition() > driveMotors[1].getTargetPosition() ||
               driveMotors[2].getCurrentPosition() > driveMotors[2].getTargetPosition() ||
               driveMotors[3].getCurrentPosition() > driveMotors[3].getTargetPosition();
    }

    /**
     * Turn the robot using milliseconds time
     * @param speed Turn Speed
     * @param sleepMS Wait time
     */
    public void turnByTime(double speed, long sleepMS){
        timer.reset();

        while (timer.milliseconds() < sleepMS){
            telemetry.addData("Speed", "%.2f", speed);
            telemetry.update();

            setDrivePower(speed, -speed);
        }
    }

    //-----------------------------------------------------------------------
    //---------------------------Glyph Control-------------------------------
    //-----------------------------------------------------------------------

    /**
     * Pulls in the flaps to grab the glyph
     */
    public void grabGlyph(){
        addLog("Grabbing the glyph");
        setFlapPosition(.4);
    }

    /**
     * Pulls back the flaps to release the glyph
     */
    public void releaseGlyph(){
        addLog("Releasing the glyph");
        setFlapPosition(-.4); // Lets go the glyph
    }

    /**
     * The arm lifts the glyph at certain speed and time
     * @param speed   Arm Speed
     * @param seconds Time before the arm stops lifting
     */
    public void liftGlyph(double speed, double seconds){
        timer.reset();
        addLog("Raising the glyph");
        while (timer.seconds() < seconds){
            setArmPower(Math.abs(speed));
        }
        setArmPower(0);
    }

    /**
     * The arm lowers the glyph at certain speed and time
     * @param speed   Arm Speed
     * @param seconds Time before the arm stops lowering
     */
    public void lowerGlyph(double speed, double seconds){
        timer.reset();
        addLog("Lowering the glyph");
        while (timer.seconds() < seconds){
            setArmPower(-Math.abs(speed));
        }
        setArmPower(0);
    }

    //-----------------------------------------------------------------------
    //----------------------------Color Sensor-------------------------------
    //-----------------------------------------------------------------------

    public boolean detectBlue(){
        boolean detected = false;
        if (colorSensor.blue() > colorSensor.red() && colorSensor.blue() > colorSensor.green()){
            detected = true;
            cdim.setLED(0, false);
            cdim.setLED(1, true);
        } else {
            cdim.setLED(0, false);
            cdim.setLED(1, false);
        }
        return detected;
    }

    public boolean detectRed(){
        boolean detected = false;
        if (colorSensor.red() > colorSensor.blue() && colorSensor.red() > colorSensor.green()){
            detected = true;
            cdim.setLED(0, true);
            cdim.setLED(1, false);
        } else {
            cdim.setLED(0, false);
            cdim.setLED(1, false);
        }
        return detected;
    }

    public void enableLED(boolean enable){
        colorSensor.enableLed(enable);
    }

    public void locateJewel(Jewel jewel, double scanS, double turnS, double timeoutS){
        boolean finish = false;
        boolean reverseTurn = false;
        Jewel foundJewel = Jewel.UNKNOWN_JEWEL;

        timer.reset();
        // Scans the jewel for certain amount of time
        while (timer.seconds() < scanS){
            if (detectBlue()){
                foundJewel = Jewel.BLUE_JEWEL;
            } else if (detectRed()){
                foundJewel = Jewel.RED_JEWEL;
            }
        }

        // Compares the found jewel and the current alliance jewel
        // and decides the robot should either reverse or not
        if (foundJewel != jewel && foundJewel != Jewel.UNKNOWN_JEWEL){
            reverseTurn = true;
        }

        timer.reset();
        // Robot turns with the jewel arm to knock off the opposite alliance jewel
        while (timer.seconds() < turnS){
            if (reverseTurn){
                setDrivePower(-TURN_SPEED, TURN_SPEED);
            } else {
                setDrivePower(TURN_SPEED, -TURN_SPEED);
            }
        }
        setDrivePower(0, 0);
        addLog("");
    }

    //-----------------------------------------------------------------------
    //----------------------------Gyro Sensor--------------------------------
    //-----------------------------------------------------------------------

    public void turnByGyro(double speed, int angle){
        turnByGyro(speed, angle, 5);
    }

    /**
     * Turn the robot in angle using gyro sensor
     * @param angle Angle of the turn
     */
    public void turnByGyro(double speed, int angle, double timeoutS){
        boolean reverse = (angle < 0);
        //gyro.resetZAxisIntegrator(); // Resets the z axis of the gyro
        turnAbsolute(speed, angle + gyro.getIntegratedZValue(), reverse, timeoutS);
    }

    /**
     * Turns the robot in absolute angle and only right unless
     * reverse is true, which makes the robot to rurn left
     *
     * @param angle   Angle of turn
     * @param reverse Reverse turn
     * @param timeoutS Timeout seconds
     */
    public void turnAbsolute(double speed, int angle, boolean reverse, double timeoutS){
        int zValue = gyro.getIntegratedZValue(); // Assign the z value of the gyro sensor

        telemetry.log().add(String.format(Locale.ENGLISH, "Turning at %03d", angle));
        timer.reset();

        while ((zValue > (angle + 3) || zValue < (angle - 3)) && timer.seconds() < timeoutS){
            if (reverse){
                if (zValue < angle){
                    setDrivePower(speed, -speed);
                } else if (zValue > angle){
                    setDrivePower(-speed, speed);
                }
            } else {
                if (zValue > angle){
                    setDrivePower(-speed, speed);
                } else if (zValue < angle){
                    setDrivePower(speed, -speed);
                }
            }

            telemetry.addData("Z Value", "%02d", zValue);
            telemetry.addData("Z Turn ", "%02d", angle);
            telemetry.addData("Reverse", reverse ? "On" : "Off");
            telemetry.addData("Timeout", timer.seconds());
            telemetry.update();

            zValue = gyro.getIntegratedZValue();
        }
    }

    //-----------------------------------------------------------------------
    //----------------------------Vuforia------------------------------------
    //-----------------------------------------------------------------------

    public void scanCryptoKey(double timeoutS){
        boolean foundVuMark = false;

        vuforia.activateRelicTrackables(); // Activate the trackables
        timer.reset(); // Resets the timer

        while (timer.seconds() < timeoutS){
            // Gets the vuMark from vuforia
            vuMark = RelicRecoveryVuMark.from(vuforia.getRelicTemplate());

            // Timeout
            telemetry.addData("Timeout", timer.seconds());

            if (vuMark != RelicRecoveryVuMark.UNKNOWN){
                foundVuMark = true;
                telemetry.addData("VuMark", "%s visable", vuMark);
            } else {
                telemetry.addData("VuMark", "not visable");
            }

            telemetry.update();
            if (foundVuMark){
                break;
            }
        }
    }

    public RelicRecoveryVuMark getCryptoKey(){
        return vuMark;
    }
}
