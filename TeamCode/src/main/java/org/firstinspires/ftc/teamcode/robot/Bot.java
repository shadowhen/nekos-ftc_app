package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public interface Bot {

    /**
     * Intializes the robot
     * @param hwMap Hardware Map
     * @param telemetry Telemetry
     */
    void init(HardwareMap hwMap, Telemetry telemetry);
}
