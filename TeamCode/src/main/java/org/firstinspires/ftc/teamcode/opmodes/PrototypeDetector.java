package org.firstinspires.ftc.teamcode.opmodes;

import com.disnodeteam.dogecv.OpenCVPipeline;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.LinkedList;
import java.util.List;

public class PrototypeDetector extends OpenCVPipeline {

    public double scale = 1.0;

    private Size initSize;

    private Mat mRgbaC; // New Copy
    private Mat mRgbaS; // Scale
    private Mat mRgbaB; // Gaussian Blur
    private Mat maskOne;
    private Mat maskTwo;
    private Mat mask;

    private Mat kernel;
    private Mat maskClosed;
    private Mat maskClean;

    private Mat overlay;

    @Override
    public Mat processFrame(Mat rgba, Mat gray) {
        initSize = rgba.size();
        Size adjustedSize = new Size();

        adjustedSize.height = initSize.height * scale;
        adjustedSize.width = initSize.width * scale;

        // Copies the original rgba mat to new copy, so the new copy can modified without modifiying
        // the original mat
        rgba.copyTo(mRgbaC);
        Imgproc.resize(mRgbaC, mRgbaS, adjustedSize);
        Imgproc.GaussianBlur(mRgbaS, mRgbaB, new Size(new double[]{7, 7}), 0);
        Core.inRange(mRgbaS, new Scalar(30, 100, 80), new Scalar(50, 255, 255), maskOne);
        Core.inRange(mRgbaS, new Scalar(170, 100, 80), new Scalar(180, 255, 255), maskTwo);

        // Mask One + Mask Two = Mask
        Core.add(maskOne, maskTwo, mask);

        // Erode and dilate
        kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(15, 15));
        Imgproc.morphologyEx(mask, maskClosed, Imgproc.MORPH_CLOSE, kernel);
        Imgproc.morphologyEx(maskClosed, maskClean, Imgproc.MORPH_OPEN, kernel);

        // FIND THE BIGGEST CONTOUR AKA FIND THE BIGGEST AREA
        Mat image = new Mat();
        Mat hiereachy = new Mat();
        List<MatOfPoint> contours = new LinkedList<>();
        MatOfPoint biggestContour = new MatOfPoint();
        Mat maskGold = new Mat(rgba.rows(), rgba.cols(), CvType.CV_8U);

        double maxVal = 0;
        int maxValIdx = 0;

        maskClean.copyTo(image);
        Imgproc.findContours(image, contours, hiereachy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        for (int contourIdx = 0; contourIdx < contours.size(); contourIdx++) {
            double contourArea = Imgproc.contourArea(contours.get(contourIdx));
            if (maxVal < contourArea) {
                maxVal = contourArea;
                maxValIdx = contourIdx;
            }
        }

        biggestContour = contours.get(maxValIdx);
        Imgproc.drawContours(maskGold, contours, -1, new Scalar(0, 255, 0), -1);

        // OVERLAY:
        Mat rgbMask = new Mat();
        Imgproc.cvtColor(maskGold, rgbMask, Imgproc.COLOR_GRAY2RGB);
        Core.addWeighted(rgbMask, 0.5, rgba, 0.5, 0, overlay);

        // CIRCLE THE BIGGEST GOLD
        Mat circled = new Mat();
        overlay.copyTo(circled);
        Imgproc.ellipse(circled, Imgproc.fitEllipse(MatOfPoint2f.fromNativeAddr(biggestContour.getNativeObjAddr())), new Scalar(0, 255, 0), Imgproc.LINE_AA);

        Mat mBgr = new Mat();
        Imgproc.cvtColor(circled, mBgr, Imgproc.COLOR_RGB2BGR);
        return mBgr;
    }
}
