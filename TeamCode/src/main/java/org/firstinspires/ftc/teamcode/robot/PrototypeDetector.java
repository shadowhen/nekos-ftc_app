package org.firstinspires.ftc.teamcode.robot;

import com.disnodeteam.dogecv.OpenCVPipeline;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

/**
 * This class implements the OpenCVPipeline using examples provided by the OpenCV repository, and
 * the detector is custom and designed to detect gold minerals and find the closest gold mineral
 * from the camera perspective.
 *
 * @author Henry
 * @version 1.0
 */
public class PrototypeDetector extends OpenCVPipeline {

    private static final double SCALE_X = 700;
    private static final double SCALE_Y = 700;
    private static final Size GAUSSIAN_BLUR_FILTER_SIZE = new Size(7, 7);

    // Gold Range
    private static final Scalar LOW_GOLD = new Scalar(17, 100, 80);
    private static final Scalar UPPER_GOLD = new Scalar(30, 255, 255);

    // Brightness Range
    private static final Scalar LOW_BRIGHT = new Scalar(170, 255, 255);
    private static final Scalar HIGH_BRIGHT = new Scalar(180, 255, 255);

    // Color for draw rectangles around contours
    private static final Scalar BOUNDING_RECT_COLOR = new Scalar(0, 255, 0);

    private Mat mRgbaC = new Mat(); // New Copy
    private Mat mRgbaB = new Mat(); // Gaussian Blur
    private Mat mRgbaBHsv = new Mat();

    private Mat maskOne = new Mat();
    private Mat maskTwo = new Mat();
    private Mat mask = new Mat();

    private Mat mFinal = new Mat();

    private Mat kernel = new Mat();
    private Mat maskClosed = new Mat();
    private Mat maskClean = new Mat();

    private Mat imageContour = new Mat();
    private Mat hierarchy = new Mat();

    private boolean detectedGold;

    @Override
    public Mat processFrame(Mat rgba, Mat gray) {
        double scaleX = SCALE_X / rgba.width();
        double scaleY = SCALE_Y / rgba.height();
        ArrayList<MatOfPoint> contours = new ArrayList<>();

        // Make a copy to work Mat
        rgba.copyTo(mRgbaC);

        // Resize the image to  aspecific size
        Imgproc.resize(rgba, mRgbaC, mRgbaC.size(), scaleX, scaleY);

        // Smooth the entire image
        Imgproc.GaussianBlur(mRgbaC, mRgbaB, GAUSSIAN_BLUR_FILTER_SIZE, 0);

        // Convert from rgb Mat to hsv Mat
        Imgproc.cvtColor(mRgbaB, mRgbaBHsv, Imgproc.COLOR_RGB2HSV);

        // Filter for the gold and brightness out of the image
        Core.inRange(mRgbaBHsv, LOW_GOLD, UPPER_GOLD, maskOne);
        Core.inRange(mRgbaBHsv, LOW_BRIGHT, HIGH_BRIGHT, maskTwo);
        Core.add(maskOne, maskTwo, mask);

        // Erode and dilate the image
        kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5));
        Imgproc.morphologyEx(mask, maskClosed, Imgproc.MORPH_CLOSE, kernel);
        Imgproc.morphologyEx(maskClosed, maskClean, Imgproc.MORPH_OPEN, kernel);

        // Copy the clean mask to an image for contours
        maskClean.copyTo(imageContour);
        
        // Find the contour and find the biggest contour of all contours
        Imgproc.findContours(imageContour, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        if (contours.size() > 0) {
            CvUtil.drawRectangleContour(mRgbaC, CvUtil.findBiggestContour(contours), BOUNDING_RECT_COLOR);
            detectedGold = true;
        } else {
            detectedGold = false;
        }

        mRgbaC.copyTo(mFinal);

        // Returns the final image
        return mFinal;
    }

    /**
     * Returns the detected gold
     * @return detected gold
     */
    public boolean getDetectedGold() {
        return detectedGold;
    }
}
