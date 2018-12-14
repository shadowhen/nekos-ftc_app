package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * This class implements the functionality of the sensors on the robot.
 *
 * @author Henry
 * @version 1.0
 */
public class SensorBot {

    private static double minDistanceMm = 0;
    private static double maxDistanceMm = 1;

    private ModernRoboticsI2cGyro gyro;
    private DistanceSensor distanceSensor;

    public void init(HardwareMap hwMap) {
        //gyro = (ModernRoboticsI2cGyro) hwMap.get(GyroSensor.class, "gyro");
        //distanceSensor = hwMap.get(DistanceSensor.class, "distance_sensor");
    }

    /**
     * Returns boolean if the distance sensor between these two distance ranges
     * @param unit Distance Unit
     * @param min  Min distance
     * @param max  Max distance
     * @return boolean
     */
    public boolean seeObject(DistanceUnit unit, double min, double max) {
        double distance = distanceSensor.getDistance(unit);
        return (distance >= min && distance <= max);
    }

    /**
     * Returns the angle error
     * @param angle Angle
     * @return Angle error
     */
    public double getError(double angle) {
        double error = angle - gyro.getIntegratedZValue();
        while (error > 180) {
            error -= 360;
        }
        while (error <= -180) {
            error += 360;
        }
        return error;
    }

    /**
     * Returns the steer
     * @param error   Error
     * @param pCoeff  Proportional Coefficient
     * @return
     */
    public double getSteer(double error, double pCoeff) {
        return Range.clip(error * pCoeff, 0.0, 1.0);
    }

    public ModernRoboticsI2cGyro getGyro() {
        return gyro;
    }

    public DistanceSensor getDistanceSensor() {
        return distanceSensor;
    }
}
