package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

interface Robot {

    void init(HardwareMap hwMap, Telemetry telemetry);

    void close();

    void setDrivePower(double leftPower, double rightPower);

    void setPaddlePosition(double position);
}
