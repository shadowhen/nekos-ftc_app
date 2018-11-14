package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class SensorBot {

    private ModernRoboticsI2cGyro gyro;
    private DistanceSensor distanceSensor;

    public void init(HardwareMap hwMap) {
        gyro = (ModernRoboticsI2cGyro) hwMap.get(GyroSensor.class, "gyro");
        distanceSensor = hwMap.get(DistanceSensor.class, "distance_sensor");
    }

    public ModernRoboticsI2cGyro getGyro() {
        return gyro;
    }

    public DistanceSensor getDistanceSensor() {
        return distanceSensor;
    }
}
