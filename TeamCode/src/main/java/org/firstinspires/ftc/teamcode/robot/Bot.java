package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * This provides an abstract class for the core functions of the robot
 */
public abstract class Bot {

    // Allows to access the hardware reference from the config file
    protected HardwareMap hwMap;

    // This sends information to the driver station remotely
    protected Telemetry telemetry;

    protected ElapsedTime runtime;

    protected Bot() {
        runtime = new ElapsedTime();
    }

    public abstract void init(HardwareMap ahwMap, Telemetry atelemetry);

    /**
     * Waits for seconds to pass
     * @param seconds time to pass
     */
    public void waitTime(double seconds) {
        seconds += runtime.seconds();

        while (runtime.seconds() < seconds) {
            telemetry.addData("Status", "Waiting for %.4f", runtime.seconds());
            telemetry.addData("Time", "%.4f / %.4f", runtime.seconds(), seconds);
            telemetry.update();
        }
    }
}
