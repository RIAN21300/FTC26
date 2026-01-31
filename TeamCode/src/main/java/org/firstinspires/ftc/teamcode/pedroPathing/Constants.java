package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.Encoder;
import com.pedropathing.ftc.localization.constants.DriveEncoderConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
        public static final double ODOMETRY_POD_DIAMETER = 35; //MM
        public static double encoderResolution = 8192 / (Math.PI * ODOMETRY_POD_DIAMETER);
        public static FollowerConstants followerConstants = new FollowerConstants()
                .mass(5)    //KG                                    //TODO: Calculate this later
                .useSecondaryTranslationalPIDF(false)
                .useSecondaryHeadingPIDF(false)
                .useSecondaryDrivePIDF(false);

        public static MecanumConstants driveConstants = new MecanumConstants()
                .maxPower(0.8)
                .rightFrontMotorName("right_front")
                .rightRearMotorName("right_back")
                .leftRearMotorName("left_back")
                .leftFrontMotorName("left_front")
                .leftFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
                .leftRearMotorDirection(DcMotorSimple.Direction.REVERSE)
                .rightFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
                .rightRearMotorDirection(DcMotorSimple.Direction.FORWARD);
        public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

        public static DriveEncoderConstants localizerConstants = new DriveEncoderConstants() //TODO: check this later
                .rightFrontMotorName("right_front")
                .rightRearMotorName("right_back")
                .leftRearMotorName("left_back")
                .leftFrontMotorName("left_front")
                .leftFrontEncoderDirection(Encoder.REVERSE)
                .leftRearEncoderDirection(Encoder.REVERSE)
                .rightFrontEncoderDirection(Encoder.FORWARD)
                .rightRearEncoderDirection(Encoder.FORWARD);
        public static Follower createFollower(HardwareMap hardwareMap) {
            return new FollowerBuilder(followerConstants, hardwareMap)
                    .pathConstraints(pathConstraints)
                    .driveEncoderLocalizer(localizerConstants)
                    .mecanumDrivetrain(driveConstants)
                    .build();
        }
}
