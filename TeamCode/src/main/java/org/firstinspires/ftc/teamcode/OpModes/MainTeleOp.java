package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.extensions.pedro.PedroDriverControlled;

import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.driving.DriverControlledCommand;

@TeleOp(name = "Main TeleOp")
public class MainTeleOp extends NextFTCOpMode {
    {
        addComponents(
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE,
                new PedroComponent(Constants::createFollower),
                new SubsystemComponent(Shooter.INSTANCE),
                new SubsystemComponent(Intake.INSTANCE)
        );
    }
    @Override
    public void onStartButtonPressed() {
        DriverControlledCommand driverControlled = new PedroDriverControlled(
                Gamepads.gamepad1().leftStickY(),
                Gamepads.gamepad1().leftStickX(),
                Gamepads.gamepad1().rightStickX()
        );
        driverControlled.schedule();
    }

    @Override
    public void onUpdate() {
        // PUSH: Shooter state (ON/OFF) equals to gamepad2 left_bumper state
        Shooter.INSTANCE.state = gamepad2.left_bumper;

        // TOGGLE: Press gamepad2 right_bumper to change Intake state (ON/OFF)
        if (gamepad2.right_bumper)
            Intake.INSTANCE.state ^= true;

        // Shooter debug log
        telemetry.addData("MotorShooter velocity: ",Shooter.INSTANCE.get_vel());
        telemetry.addData("Shooter goal: ",Shooter.INSTANCE.get_goal());
        telemetry.addData("Shooter state: ",Shooter.INSTANCE.state);
        telemetry.addData("left_bumper state: ",gamepad1.left_bumper);
        telemetry.addData("MotorShooter power: ", Shooter.INSTANCE.get_power());
        telemetry.update();
    }
}