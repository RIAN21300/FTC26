package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
        public static final double ODOMETRY_POD_DIAMETER = 35; //MM
        public static double encoderResolution = 8192 / (Math.PI * ODOMETRY_POD_DIAMETER);
        public static FollowerConstants followerConstants = new FollowerConstants()
                .mass(5)                                        //TODO: Calculate this later
                .useSecondaryTranslationalPIDF(true)
                .useSecondaryHeadingPIDF(true)
                .useSecondaryDrivePIDF(true);

        public static MecanumConstants driveConstants = new MecanumConstants()
                .maxPower(1)
                .rightFrontMotorName("right_front")
                .rightRearMotorName("right_back")
                .leftRearMotorName("left_back")
                .leftFrontMotorName("left_front")
                .leftFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
                .leftRearMotorDirection(DcMotorSimple.Direction.FORWARD)
                .rightFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
                .rightRearMotorDirection(DcMotorSimple.Direction.FORWARD);
        public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

        public static PinpointConstants localizerConstants = new PinpointConstants() //TODO: check this later
                .forwardPodY(-5)
                .strafePodX(0.5)
                .distanceUnit(DistanceUnit.INCH)
                .hardwareMapName("pinpoint")
                .customEncoderResolution(encoderResolution)
                .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
                .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        public static Follower createFollower(HardwareMap hardwareMap) {
            return new FollowerBuilder(followerConstants, hardwareMap)
                    .pathConstraints(pathConstraints)
                    .pinpointLocalizer(localizerConstants)
                    .mecanumDrivetrain(driveConstants)
                    .build();
        }
}
