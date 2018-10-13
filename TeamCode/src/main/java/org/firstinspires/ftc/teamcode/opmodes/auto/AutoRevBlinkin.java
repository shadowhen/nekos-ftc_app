package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;

public class AutoRevBlinkin extends AutoOpMode {

    private RevBlinkinLedDriver revBlinkinLedDriver;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        revBlinkinLedDriver = hardwareMap.get(RevBlinkinLedDriver.class, "rev_blinkin");

        waitForStart();

        revBlinkinLedDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLUE);
        sleep(1000);
        revBlinkinLedDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.RED);
        sleep(1000);
        revBlinkinLedDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.WHITE);
        sleep(1000);
        revBlinkinLedDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.GOLD);
        sleep(1000);

    }
}
