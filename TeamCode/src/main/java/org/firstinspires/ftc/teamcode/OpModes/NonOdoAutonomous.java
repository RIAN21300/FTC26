package org.firstinspires.ftc.teamcode.OpModes;

/*
* ntm + ghelopax
* */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.RobotConfig;
import org.firstinspires.ftc.teamcode.Subsystems.Flicker;
import org.firstinspires.ftc.teamcode.Subsystems.HoodedShooter;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.impl.MotorEx;

@Autonomous(name = "NonOdo Autonomous OpMode")
public class NonOdoAutonomous extends NextFTCOpMode {
    /* REGISTER COMPONENTS */
    {
        addComponents(
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE,
                new PedroComponent(Constants::createFollower),
                new SubsystemComponent(
                        HoodedShooter.INSTANCE,
                        Flicker.INSTANCE
                )
        );
    }

    /* VARIABLES */
    // Drivetrain
    private final MotorEx frontLeftMotor = new MotorEx("left_front").brakeMode();
    private final MotorEx frontRightMotor = new MotorEx("right_front").brakeMode();
    private final MotorEx backLeftMotor = new MotorEx("left_back").brakeMode();
    private final MotorEx backRightMotor = new MotorEx("right_back").brakeMode();

    public void turn(String state,double power,long time){
        if(state.equals("LEFT")){
            frontLeftMotor.setPower(-power);
            frontRightMotor.setPower(power);
            backLeftMotor.setPower(-power);
            backRightMotor.setPower(power);
            sleep(time);
            frontLeftMotor.setPower(0);
            frontRightMotor.setPower(0);
            backLeftMotor.setPower(0);
            backRightMotor.setPower(0);
        }
        else if(state.equals("RIGHT")){
            frontLeftMotor.setPower(power);
            frontRightMotor.setPower(-power);
            backLeftMotor.setPower(power);
            backRightMotor.setPower(-power);
            sleep(time);
            frontLeftMotor.setPower(0);
            frontRightMotor.setPower(0);
            backLeftMotor.setPower(0);
            backRightMotor.setPower(0);
        }
    }

    public void strafe(String Direction,double power,long time){
        if(Direction.equals("LEFT")){
            frontLeftMotor.setPower(-power);
            frontRightMotor.setPower(power);
            backLeftMotor.setPower(power);
            backRightMotor.setPower(-power);
            sleep(time);
            frontLeftMotor.setPower(0);
            frontRightMotor.setPower(0);
            backLeftMotor.setPower(0);
            backRightMotor.setPower(0);
        }else if(Direction.equals("RIGHT")){
            frontLeftMotor.setPower(power);
            frontRightMotor.setPower(-power);
            backLeftMotor.setPower(-power);
            backRightMotor.setPower(power);
            sleep(time);
            frontLeftMotor.setPower(0);
            frontRightMotor.setPower(0);
            backLeftMotor.setPower(0);
            backRightMotor.setPower(0);
        }
    }

    public void forward_or_backward(double power,long time){
        frontLeftMotor.setPower(power);
        frontRightMotor.setPower(power);
        backLeftMotor.setPower(power);
        backRightMotor.setPower(power);
        sleep(time);
        frontLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backLeftMotor.setPower(0);
        backRightMotor.setPower(0);
    }

    // Auto Intake
    public void collect_artifacts_by_row(){
        Intake.INSTANCE.run();
        forward_or_backward(1.0,200); //TUNE TIME AND POWER
        sleep(200);
        Intake.INSTANCE.rest();

        forward_or_backward(-1.0,200); //TUNE TIME AND POWER
        sleep(200);
    }

    // AutoINFO
    AutoInfoManager_woutPath autoInfoManager;

    /* OPMODE FUNCTIONS */
    @Override
    public void onInit() {

    }

    @Override
    public void onStartButtonPressed() {
        HoodedShooter.INSTANCE.shooter.setState(true);

        if (autoInfoManager.startNearGoal) {

        } else {

        }

        Flicker.INSTANCE.runArm(RobotConfig.BallSlotName.Left);
        sleep((long)Flicker.Arm.LIFT_DURATION);
        Flicker.INSTANCE.runArm(RobotConfig.BallSlotName.UpRight);
        sleep((long)Flicker.Arm.LIFT_DURATION);
        Flicker.INSTANCE.runArm(RobotConfig.BallSlotName.DownRight);
        sleep((long)Flicker.Arm.LIFT_DURATION);

        HoodedShooter.INSTANCE.shooter.setState(false);
    }


    @Override
    public void onStop() {

    }
}

class AutoInfoManager_woutPath {
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
    public void setStartPosition(boolean StartNearGoal) {
        startNearGoal = StartNearGoal;
    }

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
}