package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * This interface provides the essential function(s) for the robot to be able do things
 * in the competition and practice field.
 * @author Henry
 * @version 1.1
 */
public interface Bot {

    double LEFT_FRONT_POWER = 1;
    double LEFT_REAR_POWER = 1;
    double RIGHT_FRONT_POWER = 1;
    double RIGHT_REAR_POWER = 1;

    // Constant speed values for autonomous and driver control
    double DRIVE_SPEED = 1.0;
    double TURN_SPEED = 1.0;
    double VERTICAL_LIFT_SPEED = 1.0;
    double SLIDER_SPEED = 0.5;
    double SWEEPER_SPEED = 0.5;
    double SWEEPER_LIFT_SPEED = 0.9;
    double DUMPER_MOTOR_SPEED = 0.5;

    // Constant vertical lift speed values for autonomous
    double VERTICAL_RAISE_SPEED = -VERTICAL_LIFT_SPEED;
    double VERTICAL_LOWER_SPEED = VERTICAL_LIFT_SPEED;

    /**
     * Initializes the robot
     * @param hwMap Hardware Map
     * @param telemetry Telemetry
     */
    void init(HardwareMap hwMap, Telemetry telemetry);
}
