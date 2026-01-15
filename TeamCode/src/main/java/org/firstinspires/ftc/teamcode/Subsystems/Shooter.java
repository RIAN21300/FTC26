package org.firstinspires.ftc.teamcode.Subsystems;

import org.firstinspires.ftc.teamcode.RobotConfig;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;

/* Hooded Shooter */
public class Shooter implements Subsystem {
    public static final Shooter INSTANCE = new Shooter();

    /* VARIABLES */
    public boolean state;
    private final MotorEx motor = new MotorEx(RobotConfig.MOTOR_SHOOTER);
    public final double percentage = 0.9;
    public double maxVelocity = 1800.0;
    // PID Controller
    private final ControlSystem controller = ControlSystem.builder()
            .velPid(0.04,0,0.001)
            .build();

    /* SUBSYSTEM FUNCTIONS */
    @Override
    public void initialize() {
        state = false;
        controller.setGoal(new KineticState(0.0,0.0));
    }

    @Override
    public void periodic() {
        if (state) {
            // outVelocity = maxVelocity * percentage
            controller.setGoal(new KineticState(0.0, maxVelocity * percentage));
            motor.setPower(controller.calculate(new KineticState(motor.getCurrentPosition(), motor.getVelocity())));
        }
        else {
            controller.setGoal(new KineticState(0.0, 0.0));
            motor.setPower(0.0);
        }
    }

    /* API */
    public void setState(boolean newState) {
        state = newState;
    }

    /* DEBUG */
    public double get_vel() {
        return motor.getVelocity();
    }
    public double get_goal() {
        return controller.getGoal().getVelocity();
    }
    public double get_power() {
        return motor.getPower();
    }
}
