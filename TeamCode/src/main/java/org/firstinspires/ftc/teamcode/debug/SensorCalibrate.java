package org.firstinspires.ftc.teamcode.debug;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Sensor: Color Sensor Calibration")
public class SensorCalibrate extends OpMode {

    // Color Sensor to calibrate. Require that it is a Modern Robotics sensor.
    private ModernRoboticsI2cColorSensor color;

    // Track whether a calibration was recently requested.
    private int command;

    /**
     * Initialize color sensor hardware.
     */
    @Override
    public void init() {
        // Change the value "color" to the name of your color sensor.
        color = hardwareMap.get(ModernRoboticsI2cColorSensor.class, "color");
    }

    /**
     * Respond to gamepad input to perform calibrations.
     *
     * As explained in the prompt, X will begin a black-level calibration, and Y will begin a
     * white-level calibration. We prevent calibrations from being triggered within one second
     * of one another using a timer.
     */
    @Override
    public void loop() {

        /* Read the current value of the command register. If a calibration is not in progress, this
         * register should read either 0x00 or 0x01 depending on the mode of the sensor. Otherwise,
         * the register will contain the command we recently wrote.
         */
        command = color.readUnsignedByte(ModernRoboticsI2cColorSensor.Register.COMMAND);

        if (!(command == 0 || command == 1)) {
            // Something is in progress. Do nothing.
        }
        else if (gamepad1.x || gamepad2.x) {
            color.writeCommand(ModernRoboticsI2cColorSensor.Command.CALIBRATE_BLACK);
        }
        else if (gamepad1.y || gamepad2.y) {
            color.writeCommand(ModernRoboticsI2cColorSensor.Command.CALIBRATE_WHITE);
        }
        else if (gamepad1.a || gamepad2.a) {
            color.enableLed(true);
        }
        else if (gamepad1.b || gamepad2.b) {
            color.enableLed(false);
        }

        /* The following simply deals with the telemetry printed to the Driver's Station phone.
         * Status information is printed along with some instructions.
         */
        switch (command) {
            case 0x00:
            case 0x01:
                telemetry.addData("Status", "Ready");
                break;

            case 0x42:
                telemetry.addData("Status", "Calibrating black...");
                break;

            case 0x43:
                telemetry.addData("Status", "Calibrating white...");
                break;

            default:
                telemetry.addData("Status", "Something is in progress... ");
        }

        telemetry.addData("Instructions", "Hold the sensor away from any source of light and at "
                + "least 1.5m from the nearest object. Press X to begin the dark-level calibration."
                + " Once complete, hold the sensor 5cm from a white non-reflective object. "
                + "Press Y to begin a white-level calibration.");
    }
}
