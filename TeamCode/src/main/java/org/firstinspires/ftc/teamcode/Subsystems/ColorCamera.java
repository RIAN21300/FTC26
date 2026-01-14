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

    private final Point ballPosition[] = {new Point(0,0),       //Average estimate position
                                          new Point(0,0),
                                          new Point(0,0)};      //TODO: tune this later to align with ball position in floor

    private final double THRESHOLD = 10;                              //TODO: tune this later

    public ColorBlobLocatorProcessor colorLocator;
    public VisionPortal portal;
    public List<ColorBlobLocatorProcessor.Blob> blobs;

    public ColorCamera(HardwareMap hardwareMap) {
        colorLocator = new ColorBlobLocatorProcessor.Builder()
                .setTargetColorRange(ColorRange.ARTIFACT_PURPLE)   // use a predefined color match
                .setContourMode(ColorBlobLocatorProcessor.ContourMode.EXTERNAL_ONLY)
                .setRoi(ImageRegion.asUnityCenterCoordinates(-0.75, 0.75, 0.75, -0.75))
                .setDrawContours(true)   // Show contours on the Stream Preview
                .setBlurSize(5)          // Smooth the transitions between different colors in image
                .build();

        portal = new VisionPortal.Builder()
                .addProcessor(colorLocator)
                .setCameraResolution(new Size(320, 240))
                .setCamera(hardwareMap.get(WebcamName.class, RobotConfig.COLOR_WEBCAM))
                .build();
    }

    public void updatePosition() {
        blobs = colorLocator.getBlobs();

        ColorBlobLocatorProcessor.Util.filterByCriteria(
                ColorBlobLocatorProcessor.BlobCriteria.BY_CONTOUR_AREA,
                50, 20000, blobs);
    }

    public boolean check(int ball) {
        for(ColorBlobLocatorProcessor.Blob b : blobs)
        {
            RotatedRect boxFit = b.getBoxFit();
            if (Math.abs(boxFit.center.x - ballPosition[ball].x) <= THRESHOLD && Math.abs(boxFit.center.y - ballPosition[ball].y) <= THRESHOLD) return true;
        }
        return false;
    }
}
