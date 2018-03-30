package org.firstinspires.ftc.teamcode.robot.sensor;

import android.graphics.Color;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbDeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * This class provides sensor functionality, which includes the color sensor, gyro sensor, and so
 * on.
 */
public class BotSensor {

    private Telemetry telemetry = null;

    private ModernRoboticsI2cColorSensor colorSensor;
    private ModernRoboticsI2cGyro gyroSensor;
    private DeviceInterfaceModule cdim;

    public BotSensor() {

    }

    public enum Jewel {
        BLUE,
        RED,
        UNKNOWN;
    }

    public void setup(HardwareMap hwMap, Telemetry _telemetry, boolean light) {
        telemetry = _telemetry;

        colorSensor = (ModernRoboticsI2cColorSensor) hwMap.colorSensor.get("color");
        gyroSensor  = (ModernRoboticsI2cGyro) hwMap.gyroSensor.get("gyro");
        cdim        = hwMap.deviceInterfaceModule.get("Device Interface Module 1");

        colorSensor.enableLed(light);
        //colorSensor.enableLed(false);

        gyroSensor.calibrate();
        while (gyroSensor.isCalibrating()) {
            telemetry.addData(">", "calibrating the gyro sensor");
            telemetry.update();
        }
    }

    public void close() {
        colorSensor.close();
        gyroSensor.close();
        cdim.close();
    }

    /**
     *
     * @param color
     * @return
     */
    public boolean isColor(Jewel color) {
        boolean found = false; // found boolean

        // Looks for the color and if the certain color is detected, the found boolean would be true
        switch (color) {
            case RED:
                if (colorSensor.red() > colorSensor.blue() && colorSensor.red() > colorSensor.green()) {
                    found = true;
                }
                break;
            case BLUE:
                if (colorSensor.blue() > colorSensor.red() && colorSensor.blue() > colorSensor.green()) {
                    found = true;
                }
                break;
        }

        return found;
    }

    /**
     * Gets the RGB array from the color sensor
     * @return RGB array
     */
    public int[] getRGB() {
        return new int[]{colorSensor.red(), colorSensor.green(), colorSensor.blue()};
    }

    /**
     * Gets the HSV array from RGB array that comes from the color sensor
     * @return HSV array
     */
    public float[] getHSV() {
        int[] rgb = getRGB();
        float[] hsv = new float[3];

        Color.RGBToHSV(rgb[0] * 8, rgb[1] * 8, rgb[2] * 8, hsv);
        return hsv;
    }

    public ColorSensor getColorSensor() {
        return colorSensor;
    }

    public ModernRoboticsI2cGyro getGyroSensor() {
        return gyroSensor;
    }

    public DeviceInterfaceModule getCdim() {
        return cdim;
    }
}
