package org.firstinspires.ftc.teamcode.vuforia;

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
import org.firstinspires.ftc.teamcode.vuforia.BotVuforia;

@TeleOp(name = "Vuforia Test", group = "test")
public class VuforiaTest extends LinearOpMode {

    private BotVuforia botVuforia = new BotVuforia();

    @Override
    public void runOpMode() throws InterruptedException {

        botVuforia.setupVuforia(hardwareMap); // Setup the vuforia for the robot
        telemetry.addData(">", "Press Play to start");
        telemetry.update();
        waitForStart();

        botVuforia.activateRelicTrackables();

        while(opModeIsActive()){
            if (gamepad1.a && !gamepad1.b){
                botVuforia.activateRelicTrackables();
            }
            if (!gamepad1.a && gamepad1.b){
                botVuforia.deactivateRelicTrackables();
            }

            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(botVuforia.getRelicTemplate());
            if (vuMark != RelicRecoveryVuMark.UNKNOWN){
                telemetry.addData("VuMark", "%s visable", vuMark);
                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener)botVuforia.getRelicTemplate().getListener()).getPose();
                telemetry.addData("Pose", botVuforia.format(pose));

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
