package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Subsystems.Flicker;
import org.firstinspires.ftc.teamcode.Subsystems.HoodedShooter;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

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
    public void onStartButtonPressed() {

    }

    @Override
    public void onUpdate() {

    }
}
