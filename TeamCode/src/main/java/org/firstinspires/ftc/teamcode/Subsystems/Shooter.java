package org.firstinspires.ftc.teamcode.Subsystems;

import com.pedropathing.control.Controller;

import org.firstinspires.ftc.teamcode.RobotConfig;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;

public class Shooter implements Subsystem {
    public static final Shooter INSTANCE = new Shooter();

    public boolean state;
    private ControlSystem controller;
    private final MotorEx motor;
    private Shooter() {
        motor = new MotorEx(RobotConfig.MOTOR_SHOOTER);

        controller = ControlSystem.builder()
                .velPid(0.1,0,0)
                .build();

        controller.setGoal(new KineticState(0.0,0.0));
    }

    @Override
    public void periodic() {
        if (state) controller.setGoal(new KineticState(0.0,0.9));
        else controller.setGoal(new KineticState(0.0,0.9));

        motor.setPower(controller.calculate(
                new KineticState(motor.getCurrentPosition(), motor.getVelocity())
        ));
    }

    public double get_vel() {
        return motor.getVelocity();
    }
}
