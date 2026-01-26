package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.RobotConfig;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import dev.nextftc.core.subsystems.Subsystem;

public class Camera implements Subsystem {
    public static final Camera INSTANCE = new Camera();

    /* VARIABLES */
    HardwareMap hardwareMap;
    AprilTagProcessor aprilTag;
    VisionPortal visionPortal;

    /* SUBSYSTEM FUNCTIONS */
    @Override
    public void initialize() {
        aprilTag = new AprilTagProcessor.Builder()
                .build();
        aprilTag.setDecimation(3);

        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, RobotConfig.WEBCAM))
                .addProcessor(aprilTag)
                .build();
    }

    @Override
    public void periodic() {

    }

    /* API */

}
