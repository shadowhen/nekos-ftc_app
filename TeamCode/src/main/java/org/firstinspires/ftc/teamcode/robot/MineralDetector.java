package org.firstinspires.ftc.teamcode.robot;

import com.disnodeteam.dogecv.OpenCVPipeline;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

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

    private static final Scalar GOLD_RECT_COLOR = new Scalar(0, 255, 0);
    private static final Scalar SILVER_RECT_COLOR = new Scalar(0, 0, 255);

    private Rect goldRect = new Rect();
    private Rect silverRect = new Rect();

    @Override
    public Mat processFrame(Mat rgba, Mat gray) {
        Mat finalMat = new Mat();
        Mat workingMat = new Mat();

        Mat goldMat = new Mat();
        Mat silverMat = new Mat();

        ArrayList<MatOfPoint> goldContours = new ArrayList<>();
        ArrayList<MatOfPoint> silverContours = new ArrayList<>();
        Mat goldHierarchy = new Mat();
        Mat silverHierarchy = new Mat();

        MatOfPoint biggestContour;

        rgba.copyTo(workingMat);

        // Resize the image
        Imgproc.resize(workingMat, workingMat, IMAGE_DIMENSIONS);

        workingMat.copyTo(finalMat);

        // Smooth the image using Gaussian Blur
        Imgproc.GaussianBlur(workingMat, workingMat, new Size
                (IMAGE_DIMENSIONS.width/100, IMAGE_DIMENSIONS.height/100), 0);

        // Convert the color format to hsv
        Imgproc.cvtColor(workingMat, workingMat, Imgproc.COLOR_RGB2HSV);

        // GOLD
        workingMat.copyTo(goldMat);
        filterGold(goldMat, goldMat);
        cleanMask(goldMat, goldMat, Imgproc.CV_SHAPE_ELLIPSE, new Size(5, 5));

        // SILVER
        workingMat.copyTo(silverMat);
        filterSilver(silverMat, silverMat);
        cleanMask(silverMat, silverMat, Imgproc.CV_SHAPE_ELLIPSE, new Size(5, 5));

        // Find the contours for gold and silver minerals
        Imgproc.findContours(goldMat, goldContours, goldHierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.findContours(silverMat, silverContours, silverHierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        // Find the closest gold mineral
        if (goldContours.size() > 0) {
            biggestContour = CvUtil.findBiggestContour(goldContours);
            goldRect = Imgproc.boundingRect(biggestContour);
            CvUtil.drawRectangleContour(finalMat, goldRect, GOLD_RECT_COLOR);
        } else {
            goldRect.x = 0;
            goldRect.y = 0;
            goldRect.height = 0;
            goldRect.width = 0;
        }

        // Find the closest silver mineral
        if (silverContours.size() > 0) {
            biggestContour = CvUtil.findBiggestContour(silverContours);
            silverRect = Imgproc.boundingRect(biggestContour);
            CvUtil.drawRectangleContour(finalMat, silverRect, SILVER_RECT_COLOR);
        } else {
            silverRect.x = 0;
            silverRect.y = 0;
            silverRect.height = 0;
            silverRect.width = 0;
        }

        return finalMat;
    }

    /**
     * Filters image for a gold mineral
     * @param src Source Mat
     * @param dst Destination Mat
     */
    private static void filterGold(Mat src, Mat dst) {
        Mat maskOne = new Mat();
        Mat maskTwo = new Mat();

        Core.inRange(src, LOW_GOLD, HIGH_GOLD, maskOne);
        Core.inRange(src, LOW_BRIGHT, HIGH_BRIGHT, maskTwo);

        Core.add(maskOne, maskTwo, dst);
    }

    /**
     * Filters image for a silver mineral
     * @param src Source Mat
     * @param dst Destination Mat
     */
    private static void filterSilver(Mat src, Mat dst) {
        Mat maskOne = new Mat();
        Mat maskTwo = new Mat();
        Core.inRange(src, LOW_SILVER, HIGH_SILVER, maskOne);
        Core.inRange(src, LOW_BRIGHT, HIGH_BRIGHT, maskTwo);
        Core.add(maskOne, maskTwo, dst);
    }

    /**
     * Cleans the mask from a Mat
     * @param src  Source Mat
     * @param dst  Destination Mat
     * @param op   Kernel Operation
     * @param size Kernel Size
     */
    private static void cleanMask(Mat src, Mat dst, int op, Size size) {
        Mat kernel = Imgproc.getStructuringElement(op, size);
        Imgproc.morphologyEx(src, src, Imgproc.MORPH_CLOSE, kernel);
        Imgproc.morphologyEx(src, dst, Imgproc.MORPH_OPEN, kernel);
    }

    /**
     * Returns the gold rect
     * @return gold rect
     */
    public Rect getGoldRect() {
        return goldRect;
    }

    /**
     * Returns the silver rect
     * @return silver rect
     */
    public Rect getSilverRect() {
        return silverRect;
    }
}
