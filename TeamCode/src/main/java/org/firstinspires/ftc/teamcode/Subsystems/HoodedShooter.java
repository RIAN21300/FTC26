package org.firstinspires.ftc.teamcode.Subsystems;

/*
 /\_/\  /\_/\  /\_/\  /\_/\
( o.o )( o.o )( o.o )( o.o )
 > ^ <  > ^ <  > ^ <  > ^ <
 /\_/\                /\_/\
( o.o )   ghelopax   ( o.o )
 > ^ <                > ^ <
 /\_/\  /\_/\  /\_/\  /\_/\
( o.o )( o.o )( o.o )( o.o )
 > ^ <  > ^ <  > ^ <  > ^ <
*/

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.RobotConfig;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.List;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;

/* Hooded Shooter */
public class HoodedShooter implements Subsystem {
    public static final HoodedShooter INSTANCE = new HoodedShooter();

    /* VARIABLES */
    public static class Shooter {
        // Init
        public Shooter() {
            state = false;
            controller.setGoal(new KineticState(0.0, 0.0));
        }

        // Variables
        public boolean state;
        public final MotorEx motor = new MotorEx(RobotConfig.MOTOR_SHOOTER);
        public static final double percentage = 0.9;
        public static final double maxVelocity = 1800.0;

        // PID Controller
        private static final ControlSystem controller = ControlSystem.builder()
                .velPid(0.04, 0.0, 0.001)
                .build();

        // API
        public void update() {
            if (state) {
                // outVelocity = maxVelocity * percentage
                controller.setGoal(new KineticState(0.0, maxVelocity * percentage));
                motor.setPower(controller.calculate(new KineticState(motor.getCurrentPosition(), motor.getVelocity())));
            }
            else {
                controller.setGoal(new KineticState(0.0, 0.0));
                motor.setPower(0.0);
            }
        }

        public void setState(boolean newState) {
            state = newState;
        }

        public double getCurrentPercentage() {
            return motor.getVelocity() / maxVelocity;
        }
    }

    public static class Turret {
        // Init
        public Turret() {
            // init AprilTag
            aprilTag = new AprilTagProcessor.Builder()
                    .build();
            aprilTag.setDecimation(3);

            visionPortal = new VisionPortal.Builder()
                    .setCamera(hardwareMap.get(WebcamName.class, RobotConfig.WEBCAM))
                    .addProcessor(aprilTag)
                    .build();
        }

        // Variables
        private HardwareMap hardwareMap;
        private final VisionPortal visionPortal;
        private final AprilTagProcessor aprilTag;
        private int DESIRED_TAG_ID = 20;

        // API
        public void updateDesiredTagID(boolean RED, boolean BLUE) {
            if (RED) DESIRED_TAG_ID = 24;
            if (BLUE) DESIRED_TAG_ID = 20;
        }

        public void update() {
            List<AprilTagDetection> detectionList = aprilTag.getDetections();

            for (AprilTagDetection detection : detectionList) {
                if (detection.id == DESIRED_TAG_ID) {

                }
            }
        }
    }

    public Shooter shooter = new Shooter();
//    public Turret turret = new Turret();

    /* SUBSYSTEM FUNCTIONS */
    @Override
    public void initialize() {

    }

    @Override
    public void periodic() {
        shooter.update();
//        turret.update();
    }

    /* API */
    public void setState(boolean newState) {
        shooter.setState(newState);
    }
}
