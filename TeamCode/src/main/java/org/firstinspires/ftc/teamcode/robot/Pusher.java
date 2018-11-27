package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Pusher {

    private Servo pusherServo;

    public void init(HardwareMap hwMap) {
        pusherServo = hwMap.get(Servo.class, "servo_pusher");
    }

    public Servo getServoPusher() {
        return pusherServo;
    }
}
