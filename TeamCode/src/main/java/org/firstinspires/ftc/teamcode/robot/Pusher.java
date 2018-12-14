package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * This class implements the pusher or gold mineral pusher that comes out the robot and comes
 * back in.
 * @author Henry
 * @version 1.0
 */
public class Pusher {

    private Servo pusherServo;

    /**
     * Initializes the hardware
     * @param hwMap Hardware Map
     */
    public void init(HardwareMap hwMap) {
        pusherServo = hwMap.get(Servo.class, "servo_pusher");
    }

    /**
     * Returns the servo pusher
     * @return Servo Pusher
     */
    public Servo getServoPusher() {
        return pusherServo;
    }
}
