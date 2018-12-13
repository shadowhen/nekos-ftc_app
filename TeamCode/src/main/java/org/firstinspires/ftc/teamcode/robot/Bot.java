package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * This interface provides the essential function(s) for the robot to be able do things
 * in the competition and practice field.
 * @author Henry
 * @version 1.0
 */
public interface Bot {

    /**
     * Intializes the robot
     * @param hwMap Hardware Map
     * @param telemetry Telemetry
     */
    void init(HardwareMap hwMap, Telemetry telemetry);
}
