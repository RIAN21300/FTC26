package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

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
    // DRIVETRAIN
    private final MotorEx frontLeftMotor = new MotorEx("left_front").brakeMode();
    private final MotorEx frontRightMotor = new MotorEx("right_front").brakeMode();
    private final MotorEx backLeftMotor = new MotorEx("left_back").brakeMode();
    private final MotorEx backRightMotor = new MotorEx("right_back").brakeMode();
    private final IMUEx imu = new IMUEx("imu", Direction.UP, Direction.FORWARD).zeroed();

    // MOTIF
    private int patternID = -1;
    private final String[] patternList = {
            "GPP",
            "PGP",
            "PPG"
    };

    private void flickWithPattern(String pattern) {
        for (int i = 0; i < pattern.length(); ++i) {

            if (pattern.charAt(i) == 'G') {
                for (RobotConfig.BallSlotName slotName : RobotConfig.BallSlotName.values()) {
                    if (ColorCamera.INSTANCE.checkSlotForGreen(slotName)) {
                        Flicker.INSTANCE.setArmState(slotName, true);
                        new Delay(2);
                        Flicker.INSTANCE.setArmState(slotName, false);
                        break;
                    }
                }
            }

            if (pattern.charAt(i) == 'P') {
                for (RobotConfig.BallSlotName slotName : RobotConfig.BallSlotName.values()) {
                    if (ColorCamera.INSTANCE.checkSlotForPurple(slotName)) {
                        Flicker.INSTANCE.setArmState(slotName, true);
                        new Delay(2);
                        Flicker.INSTANCE.setArmState(slotName, false);
                        break;
                    }
                }
            }
        }
    }

    /* OPMODE FUNCTIONS */
    @Override
    public void onStartButtonPressed() {
//        while (patternID == -1) {
//
//        }
        patternID = 1;

        String currentPattern = patternList[patternID];

        backLeftMotor.setPower(1.0);
        backRightMotor.setPower(1.0);
        frontLeftMotor.setPower(1.0);
        frontRightMotor.setPower(1.0);
        Intake.INSTANCE.run();

        new Delay(3);

        backLeftMotor.setPower(0.0);
        backRightMotor.setPower(0.0);
        frontLeftMotor.setPower(0.0);
        frontRightMotor.setPower(0.0);
        Intake.INSTANCE.rest();

        flickWithPattern(currentPattern);
    }

    @Override
    public void onUpdate() {



    }
}
