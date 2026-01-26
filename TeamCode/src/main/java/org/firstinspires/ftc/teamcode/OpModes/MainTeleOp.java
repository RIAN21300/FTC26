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
                new SubsystemComponent(HoodedShooter.INSTANCE, Intake.INSTANCE, Flicker.INSTANCE)
        );
    }

    /* VARIABLES */
    private final MotorEx frontLeftMotor = new MotorEx("left_front").brakeMode();
    private final MotorEx frontRightMotor = new MotorEx("right_front").reversed().brakeMode();
    private final MotorEx backLeftMotor = new MotorEx("left_back").brakeMode();
    private final MotorEx backRightMotor = new MotorEx("right_back").brakeMode();
    private final IMUEx imu = new IMUEx("imu", Direction.UP, Direction.FORWARD).zeroed();

    /* OPMODE FUNCTIONS */
    @Override
    public void onWaitForStart() {
        HoodedShooter.INSTANCE.turret.updateDesiredTagID(gamepad1.circle, gamepad2.cross);

        telemetry.addLine("Connect Instruction: adb connect 192.168.43.1\nStatus: adb devices\nDisconnect: adb disconnect");
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
    }

    @Override
    public void onUpdate() {
        BindingManager.update();

        // SHOOTER
        // PUSH: Shooter state (ON/OFF) equals to gamepad2 left_bumper state
        HoodedShooter.INSTANCE.setState(gamepad2.left_bumper);

        // FLICKER
        // PUSH: UpRight = triangle, Left = square, DownRight = cross
        Flicker.INSTANCE.setArmState(Flicker.ArmName.UpRight, gamepad2.triangle);
        Flicker.INSTANCE.setArmState(Flicker.ArmName.Left, gamepad2.square);
        Flicker.INSTANCE.setArmState(Flicker.ArmName.DownRight, gamepad2.cross);

        // DEBUG
        telemetry.addLine("\n====# GAMEPAD1 JOYSTICK #====")
                .addData("\ngamepad1.left_stick_y [negated]: ", -gamepad1.left_stick_y)
                .addData("\ngamepad1.left_stick_x: "          , gamepad1.left_stick_x)
                .addData("\ngamepad1.right_stick_x: "         , gamepad1.right_stick_x);

        telemetry.addLine("\n====# HOODED SHOOTER #====")
                .addData("\ngamepad2.left_bumper: " , gamepad2.left_bumper)
//                .addData("\nMotorShooter power: "   , Shooter.INSTANCE.get_power())
//                .addData("\nMotorShooter velocity: ", Shooter.INSTANCE.get_vel())
//                .addData("Shooter goal: "         , Shooter.INSTANCE.get_goal())
//                .addData("Shooter state: "        , Shooter.INSTANCE.state)
                .addData("\npercentage reached: "   , HoodedShooter.INSTANCE.shooter.getCurrentPercentage());

        telemetry.addLine("\n====# INTAKE #====")
                .addData("\ngamepad2.right_bumper: ", gamepad2.right_bumper)
                .addData("\nMotorIntake power: "    , Intake.INSTANCE.get_power());

        telemetry.addLine("\n====# FLICKER #====")
                .addData("\ngamepad2.triangle: "         , gamepad2.triangle)
                .addData("\nServoArmUpRight position: "  , Flicker.INSTANCE.getArmPos(Flicker.ArmName.UpRight))
                .addData("\ngamepad2.square: "           , gamepad2.square)
                .addData("\nServoArmLeft position: "     , Flicker.INSTANCE.getArmPos(Flicker.ArmName.Left))
                .addData("\ngamepad2.cross: "            , gamepad2.cross)
                .addData("\nArm_DownRight state: "       , Flicker.INSTANCE.getArmState(Flicker.ArmName.DownRight))
                .addData("\nServoArmDownRight position: ", Flicker.INSTANCE.getArmPos(Flicker.ArmName.DownRight));

        telemetry.update();
    }

    @Override
    public void onStop() {
        BindingManager.reset();

        HoodedShooter.INSTANCE.setState(false);

        Intake.INSTANCE.rest();

        Flicker.INSTANCE.setArmState(Flicker.ArmName.UpRight, false);
        Flicker.INSTANCE.setArmState(Flicker.ArmName.Left, false);
        Flicker.INSTANCE.setArmState(Flicker.ArmName.DownRight, false);
    }
}