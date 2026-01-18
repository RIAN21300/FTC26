package org.firstinspires.ftc.teamcode.OpModes;

import static dev.nextftc.bindings.Bindings.*;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import dev.nextftc.bindings.BindingManager;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.PedroComponent;

import org.firstinspires.ftc.teamcode.Subsystems.Flicker;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.driving.FieldCentric;
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
                new SubsystemComponent(Shooter.INSTANCE, Intake.INSTANCE, Flicker.INSTANCE)
        );
    }

    /* VARIABLES */
    private final MotorEx frontLeftMotor = new MotorEx("left_front").brakeMode();
    private final MotorEx frontRightMotor = new MotorEx("right_front").reversed().brakeMode();
    private final MotorEx backLeftMotor = new MotorEx("left_back").brakeMode();
    private final MotorEx backRightMotor = new MotorEx("right_back").brakeMode();
    private IMUEx imu = new IMUEx("imu", Direction.UP, Direction.FORWARD).zeroed();

    /* TELEOP FUNCTIONS */
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
                Gamepads.gamepad1().rightStickX(),
                new FieldCentric(imu)
        );
        driverControlled.schedule();

        // INTAKE
        // TOGGLE: Press gamepad2 right_bumper to change Intake state (ON/OFF)
        button(() -> gamepad2.right_bumper)
                .toggleOnBecomesTrue()
                .whenTrue (Intake.INSTANCE::run)
                .whenFalse(Intake.INSTANCE::rest);

        // FLICKER
        // PUSH: UpRight = triangle, Left = square, DownRight = cross
        button(() -> gamepad2.triangle)
                .whenBecomesTrue (() -> Flicker.INSTANCE.lift(Flicker.ArmName.UpRight))
                .whenBecomesFalse(() -> Flicker.INSTANCE.rest(Flicker.ArmName.UpRight));

        button(() -> gamepad2.square)
                .whenBecomesTrue (() -> Flicker.INSTANCE.lift(Flicker.ArmName.Left))
                .whenBecomesFalse(() -> Flicker.INSTANCE.rest(Flicker.ArmName.Left));

        button(() -> gamepad2.cross)
                .whenBecomesTrue (() -> Flicker.INSTANCE.lift(Flicker.ArmName.DownRight))
                .whenBecomesFalse(() -> Flicker.INSTANCE.rest(Flicker.ArmName.DownRight));
    }

    @Override
    public void onUpdate() {
        BindingManager.update();

        // SHOOTER
        // PUSH: Shooter state (ON/OFF) equals to gamepad2 left_bumper state
        Shooter.INSTANCE.setState(gamepad2.left_bumper);

        // DEBUG
        telemetry.addLine("====# SHOOTER #====")
                .addData("\ngamepad2.left_bumper: " , gamepad2.left_bumper)
                .addData("\nMotorShooter power: "   , Shooter.INSTANCE.get_power())
                .addData("\nMotorShooter velocity: ", Shooter.INSTANCE.get_vel())
//                .addData("Shooter goal: "         , Shooter.INSTANCE.get_goal())
//                .addData("Shooter state: "        , Shooter.INSTANCE.state)
                .addData("\npercentage reached: "   , Shooter.INSTANCE.get_vel() / Shooter.INSTANCE.maxVelocity);

        telemetry.addLine("\n====# INTAKE #====")
                .addData("\ngamepad2.right_bumper: ", gamepad2.right_bumper)
                .addData("\nMotorIntake power: "    , Intake.INSTANCE.get_power());

        telemetry.addLine("\n====# GAMEPAD1 JOYSTICK #====")
                .addData("\ngamepad1.left_stick_y [negated]: ", -gamepad1.left_stick_y)
                .addData("\ngamepad1.left_stick_x: "          , gamepad1.left_stick_x)
                .addData("\ngamepad1.right_stick_x: "         , gamepad1.right_stick_x);

        telemetry.update();
    }

    @Override
    public void onStop() {
        BindingManager.reset();
    }
}