package org.firstinspires.ftc.teamcode.OpModes;

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

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.RobotConfig;
import org.firstinspires.ftc.teamcode.Subsystems.Camera;
import org.firstinspires.ftc.teamcode.Subsystems.ColorCamera;
import org.firstinspires.ftc.teamcode.Subsystems.Flicker;
import org.firstinspires.ftc.teamcode.Subsystems.HoodedShooter;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.List;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@Autonomous(name = "Main Autonomous OpMode")
public class MainAutonomous extends NextFTCOpMode {
    /* REGISTER COMPONENTS */
    {
        addComponents(
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE,
                new PedroComponent(Constants::createFollower),
                new SubsystemComponent(
                        HoodedShooter.INSTANCE,
                        Intake.INSTANCE,
                        Flicker.INSTANCE,
                        Camera.INSTANCE,
                        ColorCamera.INSTANCE
                )
        );
    }

    /* VARIABLES */
    // Drivetrain
    AutoDrivetrain autoDrivetrain = new AutoDrivetrain();
    // Info
    AutoInfoManager autoInfoManager;

    /* OPMODE FUNCTIONS */
    @Override
    public void onInit() {
        Camera.INSTANCE.initCamera(hardwareMap);
        ColorCamera.INSTANCE.initColorCamera(hardwareMap);

        // Choosing Alliance: gamepad1: dpad_right = RED, dpad_left = BLUE
        autoInfoManager.updateAlliance(RobotConfig.AllianceName.NotChosenYet);

        telemetry.addLine("\nChoose Your Alliance (GAMEPAD 1 DPAD)");
        telemetry.addLine("\nRIGHT = RED, LEFT = BLUE");
        telemetry.update();

        while (autoInfoManager.currentAlliance.equals(RobotConfig.AllianceName.NotChosenYet)) {
            autoInfoManager.updateAlliance(gamepad1.dpad_right, gamepad1.dpad_left);
        }

        telemetry.addData("\nCurrent Alliance", autoInfoManager.currentAlliance);
        telemetry.update();

        HoodedShooter.INSTANCE.turret.updateDesiredTagID(autoInfoManager.currentAlliance);

        autoInfoManager.patternID = Camera.INSTANCE.getDetectionList().get(0).id;

        PedroComponent.follower().setStartingPose(AutoDrivetrain.startPose);
        pathStage = 0;
    }

    @Override
    public void onWaitForStart() {

    }

    @Override
    public void onStartButtonPressed() {

    }

    @Override
    public void onUpdate() {
        PedroComponent.follower().update();
        autonomousPathUpdate();
    }

    private int pathStage;
    public void autonomousPathUpdate() {
        switch (pathStage) {
            case 0:
                if (!PedroComponent.follower().isBusy()) {
                    PedroComponent.follower().followPath(autoInfoManager.pathFirst, true);
                    pathStage = 1;
                }
                break;
            case 1:
                if (!PedroComponent.follower().isBusy()) {
                    HoodedShooter.INSTANCE.turret.trackTag();
                    HoodedShooter.INSTANCE.shooter.setState(true);
                    new Delay(100);
                    Flicker.INSTANCE.autoFlick(autoInfoManager.currentPattern);
                    HoodedShooter.INSTANCE.shooter.setState(false);
                    Intake.INSTANCE.run();
                    PedroComponent.follower().followPath(autoInfoManager.pathSecond, true);
                    pathStage = 2;
                }
                break;
            case 2:
                if (!PedroComponent.follower().isBusy()) {
                    Intake.INSTANCE.rest();
                    PedroComponent.follower().followPath(autoInfoManager.pathThird, true);
                    pathStage = 3;
                }
                break;
        }
    }
}

class AutoInfoManager {
    /* Start Position */
    public boolean startNearGoal;

    /* Alliance */
    public RobotConfig.AllianceName currentAlliance;

    /* Pattern (MOTIF) */
    public int patternID = -1;
    public String currentPattern;
    private final String[] patternList = {
            "GPP",
            "PGP",
            "PPG"
    };

    /* API */
    public void updateAlliance(boolean RED, boolean BLUE) {
        if (RED) {
            currentAlliance = RobotConfig.AllianceName.Red;
        }
        if (BLUE) {
            currentAlliance = RobotConfig.AllianceName.Blue;
        }
    }

    public void updateAlliance(RobotConfig.AllianceName newAlliance) {
        currentAlliance = newAlliance;
    }

    public void updatePattern() {
        if (patternID != -1) {
            currentPattern = patternList[patternID];
        }
    }

    public void updateStartPosition(boolean StartNearGoal) {
        startNearGoal = StartNearGoal;
    }

    public PathChain pathFirst = PedroComponent.follower().pathBuilder()
            .addPath(new BezierLine(AutoDrivetrain.startPose, AutoDrivetrain.scorePose))
            .setLinearHeadingInterpolation(AutoDrivetrain.startPose.getHeading(), AutoDrivetrain.scorePose.getHeading())
            .build();

    public PathChain pathSecond = PedroComponent.follower().pathBuilder()
            .addPath(new BezierLine(AutoDrivetrain.scorePose, AutoDrivetrain.spikeMark_LoadingZone))
            .setLinearHeadingInterpolation(AutoDrivetrain.scorePose.getHeading(), AutoDrivetrain.spikeMark_LoadingZone.getHeading())
            .build();

    public PathChain pathThird = PedroComponent.follower().pathBuilder()
            .addPath(new BezierLine(AutoDrivetrain.spikeMark_LoadingZone, AutoDrivetrain.scorePose))
            .setLinearHeadingInterpolation(AutoDrivetrain.spikeMark_LoadingZone.getHeading(), AutoDrivetrain.scorePose.getHeading())
            .build();
}

class AutoDrivetrain { // Using PedroPathing
    /* Pose on Zone */
    public static Pose startPose;
    public static Pose scorePose;
    public static Pose spikeMark_Goal;
    public static Pose spikeMark_Middle;
    public static Pose spikeMark_LoadingZone;
    private double MirrorX(boolean mirrored, double x) { // Mirror Pose for Red Alliance
        return (mirrored ? 144.0 - x : x);
    }
    private double MirrorAngle(boolean mirrored, double angle) {
        return (mirrored ? 180.0 - angle : angle);
    }

    /* API */
    public void updatePose(RobotConfig.AllianceName Alliance, boolean startNearGoal) {
        boolean mirrored = Alliance.equals(RobotConfig.AllianceName.Red);

        startPose = (
                startNearGoal
                        ? new Pose(
                        MirrorX(mirrored, 21),
                        123,
                        Math.toRadians(MirrorAngle(mirrored, -35))
                )
                        : new Pose(
                        MirrorX(mirrored, 56),
                        8,
                        Math.toRadians(MirrorAngle(mirrored, 90))
                )
        );
        scorePose = (
                startNearGoal
                        ? new Pose(
                        72,
                        72,
                        Math.toRadians(MirrorAngle(mirrored, 135))
                )
                        : new Pose(
                        MirrorX(mirrored, 56),
                        8,
                        Math.toRadians(MirrorAngle(mirrored, 121))
                )
        );

        spikeMark_Goal = new Pose(
                MirrorX(mirrored, 48),
                84,
                Math.toRadians(MirrorAngle(mirrored, 180))
        );
        spikeMark_Middle = new Pose(
                MirrorX(mirrored, 48),
                60,
                Math.toRadians(MirrorAngle(mirrored, 180))
        );
        spikeMark_LoadingZone = new Pose(
                MirrorX(mirrored, 48),
                36,
                Math.toRadians(MirrorAngle(mirrored, 180))
        );
    }
}