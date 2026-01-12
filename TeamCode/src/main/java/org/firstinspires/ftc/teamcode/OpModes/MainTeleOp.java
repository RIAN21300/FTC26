package org.firstinspires.ftc.teamcode.OpModes;

import static dev.nextftc.bindings.Bindings.button;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

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
import dev.nextftc.hardware.driving.MecanumDriverControlled;
import dev.nextftc.hardware.impl.MotorEx;

@TeleOp(name = "Main TeleOp")
public class MainTeleOp extends NextFTCOpMode {
    /* REGISTER COMPONENTS */
    {
        addComponents(
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE,
                new PedroComponent(Constants::createFollower),
                new SubsystemComponent(Shooter.INSTANCE, Intake.INSTANCE)
        );
    }

    /* VARIABLES */
    private final MotorEx frontLeftMotor = new MotorEx("left_front").reversed();
    private final MotorEx frontRightMotor = new MotorEx("right_front");
    private final MotorEx backLeftMotor = new MotorEx("left_back").reversed();
    private final MotorEx backRightMotor = new MotorEx("right_back");

    /*TELEOP FUNCTIONS */
    @Override
    public void onStartButtonPressed() {
        Command driverControlled = new MecanumDriverControlled(
                frontLeftMotor,
                frontRightMotor,
                backLeftMotor,
                backRightMotor,
                Gamepads.gamepad1().leftStickY().negate(),
                Gamepads.gamepad1().leftStickX(),
                Gamepads.gamepad1().rightStickX()
        );
        driverControlled.schedule();

        // INTAKE
        // TOGGLE: Press gamepad2 right_bumper to change Intake state (ON/OFF)
        button(() -> gamepad2.right_bumper)
                .toggleOnBecomesTrue()
                .whenTrue(Intake.INSTANCE::run)
                .whenFalse(Intake.INSTANCE::rest);
    }

    @Override
    public void onUpdate() {
        // SHOOTER
        // PUSH: Shooter state (ON/OFF) equals to gamepad2 left_bumper state
        Shooter.INSTANCE.setState(gamepad2.left_bumper);

        // FLICKER
        // PUSH: UpRight = triangle, Left = square, DownRight = cross
        Flicker.INSTANCE
                .setArmState(Flicker.ArmName.UpRight, gamepad2.triangle)
                .setArmState(Flicker.ArmName.Left, gamepad2.square)
                .setArmState(Flicker.ArmName.DownRight, gamepad2.cross);

        // DEBUG
        // Shooter debug log
        telemetry.addData("MotorShooter velocity: ",Shooter.INSTANCE.get_vel())
                 .addData("Shooter goal: ",Shooter.INSTANCE.get_goal())
                 .addData("Shooter state: ",Shooter.INSTANCE.state)
                 .addData("left_bumper state: ",gamepad1.left_bumper)
                 .addData("MotorShooter power: ", Shooter.INSTANCE.get_power());
        telemetry.update();
    }
}