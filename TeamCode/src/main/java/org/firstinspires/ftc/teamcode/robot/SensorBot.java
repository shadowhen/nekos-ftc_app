package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * This class implements the functionality of the sensors on the robot.
 *
 * @author Henry
 * @version 1.1
 */
public class SensorBot {

    private Orientation lastAngles = new Orientation();
    private int globalAngle = 0;

    private BNO055IMU imu;

    /**
     * Initializes the sensors on the robot
     * @param hwMap Hardware Map
     */
    public void init(HardwareMap hwMap) {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;

        imu = hwMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
    }

    /**
     * Checks if the gyro sensor on the Rev Hub IMU is being calibrated
     * @return gyro calibrated
     */
    public boolean isGyroCalibrated() {
        return imu.isGyroCalibrated();
    }

    /**
     * Returns the gyro sensor's current angle (or heading)
     * @return angle
     */
    public double getAngle() {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;
        if (deltaAngle < -180) {
            deltaAngle += 360;
        } else if (deltaAngle > 180) {
            deltaAngle -= 360;
        }

        globalAngle += deltaAngle;
        lastAngles = angles;

        return globalAngle;
    }

    /**
     * Returns the angle error
     * @param angle Angle
     * @return Angle error
     */
    public double getError(double angle) {
        double error = angle - getAngle();
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
     * @return steer
     */
    public double getSteer(double error, double pCoeff) {
        return Range.clip(error * pCoeff, 0.0, 1.0);
    }

    /**
     * Reset the heading of the imu's gyro sensor
     */
    public void resetAngle() {
        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        globalAngle = 0;
    }

    /**
     * Returns the correction based on the zero degrees
     * @return correction
     */
    public double getCorrection(double gain, double offset) {
        double angle = getAngle();

        if (angle >= -offset && angle <= offset) {
            return 0;
        } else {
            return -angle * gain;
        }
    }
}
