package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * This class extends Bot to allow testing experimental methods that are
 * not available on normal programs of the robot.
 */
public class TestBot extends Bot {

    public TestBot() {

    }

    @Override
    public void init(HardwareMap ahwMap, Telemetry _telemetry) {
        super.init(ahwMap, _telemetry);

        jewelSlider = hwMap.dcMotor.get("motorslider");
        jewelArm    = hwMap.crservo.get("servolowerarm");
        jewelElbow  = hwMap.servo.get("servoelbowarm");
    }
}
