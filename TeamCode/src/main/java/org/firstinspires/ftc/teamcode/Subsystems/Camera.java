package org.firstinspires.ftc.teamcode.Subsystems;

import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.RobotConfig;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

import dev.nextftc.core.subsystems.Subsystem;

public class Camera implements Subsystem {
    public static final Camera INSTANCE = new Camera();

    public void initCamera(HardwareMap hardwareMap) {
        aprilTag = new AprilTagProcessor.Builder()
                .build();
        aprilTag.setDecimation(2);

        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, RobotConfig.WEBCAM))
                .addProcessor(aprilTag)
                .setCameraResolution(new Size(640, 480))
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .build();
    }

    /* VARIABLES */
    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;
    public List<AprilTagDetection> detectionList;

    /* SUBSYSTEM FUNCTIONS */
    @Override
    public void periodic() {
        detectionList = aprilTag.getDetections();
    }

    /* API */
    public List<AprilTagDetection> getDetectionList() {
        return aprilTag.getDetections();
    }
}
