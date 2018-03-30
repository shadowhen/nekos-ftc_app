package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.teamcode.robot.sensor.BotVuforia;

@TeleOp(name = "BotVuforia Test", group = "Test")
@Disabled
public class VuforiaTest extends LinearOpMode {

    private BotVuforia vuforia = new BotVuforia();

    @Override
    public void runOpMode() throws InterruptedException {
        vuforia.setup(hardwareMap); // Setup the vuforia for the robot

        telemetry.addData(">", "Press Play to start");
        telemetry.update();

        waitForStart();

        vuforia.activate();

        while(opModeIsActive()){
            // Get the vuMark based on the possible marks that the camera sees
            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(vuforia.getTrackable());

            // Informs the user about what the camera sees
            if (vuMark != RelicRecoveryVuMark.UNKNOWN){
                telemetry.addData("VuMark", "%s visable", vuMark);
                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) vuforia.getTrackable().getListener()).getPose();
                telemetry.addData("Pose", vuforia.format(pose));

                if (pose != null) {
                    VectorF trans = pose.getTranslation();
                    Orientation rot = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

                    // Extract the X, Y, and Z components of the offset of the target relative to the robot
                    double tX = trans.get(0);
                    double tY = trans.get(1);
                    double tZ = trans.get(2);

                    // Extract the rotational components of the target relative to the robot
                    double rX = rot.firstAngle;
                    double rY = rot.secondAngle;
                    double rZ = rot.thirdAngle;
                }
            } else {
                telemetry.addData("VuMark", "not visible");
            }

            telemetry.update();
        }
    }
}
