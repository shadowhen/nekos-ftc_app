package org.firstinspires.ftc.teamcode.robot;

import com.disnodeteam.dogecv.OpenCVPipeline;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * This class extends the OpenCVPipeline for detecting gold and silver minerals.
 * @author Henry
 * @version 1.0
 */
public class MineralDetector extends OpenCVPipeline {

    private static final Size IMAGE_DIMENSIONS = new Size(700, 700);

    private static final Scalar LOW_GOLD = new Scalar(15, 100, 80);
    private static final Scalar HIGH_GOLD = new Scalar(30, 255, 255);

    private static final Scalar LOW_BRIGHT = new Scalar(170, 255, 255);
    private static final Scalar HIGH_BRIGHT = new Scalar(180, 255, 255);

    private static final Scalar LOW_SILVER = new Scalar(10, 5, 200);
    private static final Scalar HIGH_SILVER = new Scalar(40, 40, 255);

    @Override
    public Mat processFrame(Mat rgba, Mat gray) {
        Mat workingMat = new Mat();
        Mat goldMat = new Mat();
        Mat silverMat = new Mat();
        rgba.copyTo(workingMat);

        // Resize the image
        Imgproc.resize(workingMat, workingMat, IMAGE_DIMENSIONS);

        // Smooth the image using Gaussian Blur
        Imgproc.GaussianBlur(workingMat, workingMat, new Size
                (IMAGE_DIMENSIONS.width/100, IMAGE_DIMENSIONS.height/100), 0);

        // Convert the color format to hsv
        Imgproc.cvtColor(workingMat, workingMat, Imgproc.COLOR_RGB2HSV);

        //TODO: Finish the gold
        // GOLD
        workingMat.copyTo(goldMat);

        //TODO: Finish the silver
        // SILVER
        workingMat.copyTo(silverMat);

        //TODO: Work on the contours for the final image

        return null;
    }

    private void filterGold(Mat src, Mat dst) {
        Mat maskOne = new Mat();
        Mat maskTwo = new Mat();

        Core.inRange(src, LOW_GOLD, HIGH_GOLD, maskOne);
        Core.inRange(src, LOW_BRIGHT, HIGH_BRIGHT, maskTwo);

        //TODO: Finish the filterGold method
    }
}
