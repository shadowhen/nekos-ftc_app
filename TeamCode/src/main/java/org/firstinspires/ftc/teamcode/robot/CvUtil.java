package org.firstinspires.ftc.teamcode.robot;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.List;

/**
 * This class provide methods which are used for various purposes in OpenCV.
 * @author Henry
 * @version 1.0
 */
public class CvUtil {

    /**
     * Find the biggest contour from given list of contours.
     * @param contours List of contours
     * @return Biggest contour
     */
    public static MatOfPoint findBiggestContour(List<MatOfPoint> contours) {
        MatOfPoint biggestContour = contours.get(0);
        double area = Imgproc.contourArea(biggestContour);

        for (int i = 1; i < contours.size(); i++) {
            double contourArea = Imgproc.contourArea(contours.get(i));
            if (contourArea > area) {
                area = contourArea;
                biggestContour = contours.get(i);
            }
        }

        return biggestContour;
    }

    /**
     * Draws the rectangle over a selected contour on a given image.
     * @param image Image to be drawn
     * @param contour Contour where the rectangle is drawn
     */
    public static void drawRectangleContour(Mat image, MatOfPoint contour, Scalar color) {
        drawRectangleContour(image, Imgproc.boundingRect(contour), color);
    }

    /**
     * Draws the rectangle over a selected rect on a given image.
     * @param image Image to be drawn
     * @param rect  Rect to draw
     * @param color Rect Color
     */
    public static void drawRectangleContour(Mat image, Rect rect, Scalar color) {
        Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), color, 5);
    }
}
