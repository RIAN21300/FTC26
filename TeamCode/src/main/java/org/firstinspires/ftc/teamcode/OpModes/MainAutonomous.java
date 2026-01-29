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

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.RobotConfig;
import org.firstinspires.ftc.teamcode.Subsystems.Camera;
import org.firstinspires.ftc.teamcode.Subsystems.ColorCamera;
import org.firstinspires.ftc.teamcode.Subsystems.Flicker;
import org.firstinspires.ftc.teamcode.Subsystems.HoodedShooter;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.impl.Direction;
import dev.nextftc.hardware.impl.IMUEx;
import dev.nextftc.hardware.impl.MotorEx;

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
    AutoDrivetrain autoDrivetrain = new AutoDrivetrain(hardwareMap);
    private final MotorEx frontLeftMotor = new MotorEx("left_front").brakeMode();
    private final MotorEx frontRightMotor = new MotorEx("right_front").brakeMode();
    private final MotorEx backLeftMotor = new MotorEx("left_back").brakeMode();
    private final MotorEx backRightMotor = new MotorEx("right_back").brakeMode();
    private final IMUEx imu = new IMUEx("imu", Direction.UP, Direction.FORWARD).zeroed();
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


    }

    @Override
    public void onWaitForStart() {

    }

    @Override
    public void onStartButtonPressed() {
    }

    @Override
    public void onUpdate() {

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
}

class AutoDrivetrain { // Using PedroPathing
    public AutoDrivetrain(HardwareMap hardwareMap) {
        follower = Constants.createFollower(hardwareMap);
    }

    /* Follower */
    private final Follower follower;

    /* Pose on Zone */
    private Pose start;
    private Pose score;
    private Pose spikeMark_Goal;
    private Pose spikeMark_Middle;
    private Pose spikeMark_LoadingZone;
    private double MirrorX(boolean mirrored, double x) { // Mirror Pose for Red Alliance
        return (mirrored ? 144.0 - x : x);
    }
    private double MirrorAngle(boolean mirrored, double angle) {
        return (mirrored ? 180.0 - angle : angle);
    }

    /* API */
    public void updatePose(RobotConfig.AllianceName Alliance, boolean startNearGoal) {
        boolean mirrored = Alliance.equals(RobotConfig.AllianceName.Red);

        start = (
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
        score = (
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

        spikeMark_Goal        = new Pose(
                MirrorX(mirrored, 48),
                84,
                Math.toRadians(MirrorAngle(mirrored, 180))
        );
        spikeMark_Middle      = new Pose(
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