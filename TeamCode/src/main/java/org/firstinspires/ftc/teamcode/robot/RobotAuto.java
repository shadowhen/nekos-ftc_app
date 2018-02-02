package org.firstinspires.ftc.teamcode.robot;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

public interface RobotAuto {

    void moveByEncoders(double speed, double leftDist, double rightDist, double timeoutS);

    void turn(double speed, int turn, double timeoutS);

    void turnAbsolute(double speed, int turn, double timeoutS);

    RelicRecoveryVuMark getVuMark();
}
