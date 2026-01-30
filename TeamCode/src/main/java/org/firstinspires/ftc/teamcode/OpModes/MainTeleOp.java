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

import static dev.nextftc.bindings.Bindings.*;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import dev.nextftc.bindings.BindingManager;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.PedroComponent;

import org.firstinspires.ftc.teamcode.RobotConfig;
import org.firstinspires.ftc.teamcode.Subsystems.Camera;
import org.firstinspires.ftc.teamcode.Subsystems.Flicker;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.HoodedShooter;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.driving.MecanumDriverControlled;
import dev.nextftc.hardware.impl.Direction;
import dev.nextftc.hardware.impl.IMUEx;
import dev.nextftc.hardware.impl.MotorEx;

@TeleOp(name = "Main TeleOp")
public class MainTeleOp extends NextFTCOpMode {
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
                        Camera.INSTANCE
                )
        );
    }

    /* VARIABLES */
    private RobotConfig.AllianceName currentAlliance;
    // Drivetrain
    private final MotorEx frontLeftMotor = new MotorEx("left_front").brakeMode().reversed();
    private final MotorEx frontRightMotor = new MotorEx("right_front").brakeMode();
    private final MotorEx backLeftMotor = new MotorEx("left_back").brakeMode().reversed();
    private final MotorEx backRightMotor = new MotorEx("right_back").brakeMode();
    private final IMUEx imu = new IMUEx("imu", Direction.UP, Direction.FORWARD).zeroed();

    /* OPMODE FUNCTIONS */
    @Override
    public void onInit() {
        currentAlliance = RobotConfig.AllianceName.Blue; // Default Alliance

        Camera.INSTANCE.initCamera(hardwareMap);
    }

    @Override
    public void onWaitForStart() {
        // Choosing Alliance: gamepad1: dpad_right = RED, dpad_left = BLUE
        if (gamepad1.dpad_right) {
            currentAlliance = RobotConfig.AllianceName.Red;
        }
        if (gamepad1.dpad_left) {
            currentAlliance = RobotConfig.AllianceName.Blue;
        }

        HoodedShooter.INSTANCE.turret.updateDesiredTagID(currentAlliance);

        telemetry.addLine("\nChoose Your Alliance (GAMEPAD 1)");
        telemetry.addLine("\nDPAD RIGHT = RED, DPAD LEFT = BLUE");
        telemetry.addData("\nCurrent Alliance", currentAlliance);
        telemetry.update();
    }

    @Override
    public void onStartButtonPressed() {
        // DRIVETRAIN
        Command driverControlled = new MecanumDriverControlled(
                frontLeftMotor,
                frontRightMotor,
                backLeftMotor,
                backRightMotor,
                Gamepads.gamepad1().leftStickY().negate(),
                Gamepads.gamepad1().leftStickX(),
                Gamepads.gamepad1().rightStickX()
//                new FieldCentric(imu)
        );
        driverControlled.schedule();

        // INTAKE
        // TOGGLE: Press gamepad2 right_bumper to change Intake state (ON/OFF)
        button(() -> gamepad2.right_bumper)
                .toggleOnBecomesTrue()
                .whenTrue (Intake.INSTANCE::run)
                .whenFalse(Intake.INSTANCE::rest);

        // FLICKER
        // TOGGLE CYCLE: UpRight = triangle, Left = square, DownRight = cross

        button(() -> gamepad2.triangle)
                .whenBecomesTrue(() -> Flicker.INSTANCE.runArm(RobotConfig.BallSlotName.UpRight));
        button(() -> gamepad2.square)
                .whenBecomesTrue(() -> Flicker.INSTANCE.runArm(RobotConfig.BallSlotName.Left));
        button(() -> gamepad2.cross)
                .whenBecomesTrue(() -> Flicker.INSTANCE.runArm(RobotConfig.BallSlotName.DownRight));

        // TURRET: Rotate
        // TOGGLE AutoAim
        button(() -> gamepad2.circle)
                .whenBecomesTrue(() -> HoodedShooter.INSTANCE.turret.aimToTag());
    }

    @Override
    public void onUpdate() {
        BindingManager.update();

        // SHOOTER
        // PUSH: Shooter state (ON/OFF) equals to gamepad2 left_bumper state
        HoodedShooter.INSTANCE.setShooterState(gamepad2.left_bumper);

        // TURRET
        // Rotate: ANALOG: gamepad2 left stick x
        HoodedShooter.INSTANCE.setTurretRotateSpeed(-gamepad2.left_stick_x);

        // DEBUG
        telemetry.addLine("\n====# GAMEPAD1 JOYSTICK #====")
                .addData("\ngamepad1.left_stick_y [negated]", -gamepad1.left_stick_y)
                .addData("\ngamepad1.left_stick_x"          , gamepad1.left_stick_x)
                .addData("\ngamepad1.right_stick_x"         , gamepad1.right_stick_x);

        telemetry.addLine("\n====# SHOOTER #====")
                .addData("\ngamepad2.left_bumper"           , gamepad2.left_bumper)
//                .addData("\nMotorShooter power: "   , Shooter.INSTANCE.get_power())
                .addData("\nMotorShooter velocity: "        , HoodedShooter.INSTANCE.shooter.getVelocity())
//                .addData("Shooter goal: "         , Shooter.INSTANCE.get_goal())
//                .addData("Shooter state: "        , Shooter.INSTANCE.state)
                .addData("\npercentage reached"             , HoodedShooter.INSTANCE.shooter.getCurrentPercentage());

        telemetry.addLine("\n====# Turret #====")
                .addData("\ngamepad2.left_stick_x"          , gamepad2.left_stick_x)
                .addData("\nTurret rotateSpeed"             , HoodedShooter.INSTANCE.turret.rotateSpeed)
                .addData("\nServoTurretRotate speed"        , HoodedShooter.INSTANCE.turret.getRotateSpeed());

        telemetry.addLine("\n====# INTAKE #====")
                .addData("\ngamepad2.right_bumper"          , gamepad2.right_bumper)
                .addData("\nMotorIntake power"              , Intake.INSTANCE.get_power());

        telemetry.addLine("\n====# FLICKER #====")
                .addData("\ngamepad2.triangle"              , gamepad2.triangle)
                .addData("\nServoArmUpRight position"       , Flicker.INSTANCE.getArmPos(RobotConfig.BallSlotName.UpRight))
                .addData("\ngamepad2.square"                , gamepad2.square)
                .addData("\nServoArmLeft position"          , Flicker.INSTANCE.getArmPos(RobotConfig.BallSlotName.Left))
                .addData("\ngamepad2.cross"                 , gamepad2.cross)
                .addData("\nServoArmDownRight position"     , Flicker.INSTANCE.getArmPos(RobotConfig.BallSlotName.DownRight));

        telemetry.update();
    }

    @Override
    public void onStop() {
        BindingManager.reset();

        HoodedShooter.INSTANCE.setShooterState(false);

        Intake.INSTANCE.rest();
    }
}