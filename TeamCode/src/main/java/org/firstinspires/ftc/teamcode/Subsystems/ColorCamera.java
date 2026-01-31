package org.firstinspires.ftc.teamcode.Subsystems;

import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.RobotConfig;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;
import org.firstinspires.ftc.vision.opencv.ColorRange;
import org.firstinspires.ftc.vision.opencv.ImageRegion;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;

import java.util.List;

import dev.nextftc.core.subsystems.Subsystem;

public class ColorCamera implements Subsystem {
    public static final ColorCamera INSTANCE = new ColorCamera();

    public void initColorCamera(HardwareMap hardwareMap) {
        for (int i = 0; i < colorCount; ++i) {
            colorLocator[i] = new ColorBlobLocatorProcessor.Builder()
                    .setTargetColorRange(colorRanges[i])   // use a predefined color match
                    .setContourMode(ColorBlobLocatorProcessor.ContourMode.EXTERNAL_ONLY)
                    .setRoi(ImageRegion.asUnityCenterCoordinates(-0.75, 0.75, 0.75, -0.75))
                    .setDrawContours(true)   // Show contours on the Stream Preview
                    .setBlurSize(5)          // Smooth the transitions between different colors in image
                    .build();

            visionPortal = new VisionPortal.Builder()
                    .addProcessor(colorLocator[i])
                    .setCameraResolution(new Size(640, 480))
                    .setCamera(hardwareMap.get(WebcamName.class, RobotConfig.COLOR_WEBCAM))
                    .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                    .build();
        }
    }

    /* VARIABLES */
    private final Point[] ballPosition = {
            new Point(0,0),                    //Average estimate position
            new Point(0,0),
            new Point(0,0)
    };                                             //TODO: tune this later to align with ball position on floor

    private final double THRESHOLD = 10;           //TODO: tune this later

    public ColorBlobLocatorProcessor[] colorLocator = new ColorBlobLocatorProcessor[2];
    private final ColorRange[] colorRanges = {
            ColorRange.ARTIFACT_GREEN,
            ColorRange.ARTIFACT_PURPLE
    };
    private final int colorCount = 2;
    public VisionPortal visionPortal;
    public List<ColorBlobLocatorProcessor.Blob> greenBlobList, purpleBlobList;

    /* SUBSYSTEM FUNCTIONS */


    /* API */
    public void updateBlobList() {
        greenBlobList = colorLocator[RobotConfig.BallColor.Green.ordinal()].getBlobs();
        purpleBlobList = colorLocator[RobotConfig.BallColor.Purple.ordinal()].getBlobs();

        ColorBlobLocatorProcessor.Util.filterByCriteria(
                ColorBlobLocatorProcessor.BlobCriteria.BY_CONTOUR_AREA,
                50, 20000,
                greenBlobList
        );

        ColorBlobLocatorProcessor.Util.filterByCriteria(
                ColorBlobLocatorProcessor.BlobCriteria.BY_CONTOUR_AREA,
                50, 20000,
                purpleBlobList
        );
    }

    public boolean checkSlotForPurple(RobotConfig.BallSlotName slotName) {
        updateBlobList();
        for(ColorBlobLocatorProcessor.Blob blob : purpleBlobList)
        {
            RotatedRect boxFit = blob.getBoxFit();

            if (Math.abs(boxFit.center.x - ballPosition[slotName.ordinal()].x) <= THRESHOLD)
                if (Math.abs(boxFit.center.y - ballPosition[slotName.ordinal()].y) <= THRESHOLD)
                    return true;
        }

        return false;
    }

    public boolean checkSlotForGreen(RobotConfig.BallSlotName slotName) {
        updateBlobList();
        for(ColorBlobLocatorProcessor.Blob blob : greenBlobList)
        {
            RotatedRect boxFit = blob.getBoxFit();

            if (Math.abs(boxFit.center.x - ballPosition[slotName.ordinal()].x) <= THRESHOLD)
                if (Math.abs(boxFit.center.y - ballPosition[slotName.ordinal()].y) <= THRESHOLD)
                    return true;
        }

        return false;
    }
}
