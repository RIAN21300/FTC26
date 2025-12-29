package org.firstinspires.ftc.teamcode.Subsystems;

import org.firstinspires.ftc.teamcode.RobotConfig;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;

public class Shooter implements Subsystem {
    public static final Shooter INSTANCE = new Shooter();

    private Shooter() {}

    public boolean state;
    private final MotorEx motor = new MotorEx(RobotConfig.MOTOR_SHOOTER);

    @Override
    public void periodic() {
        motor.setPower((state ? 1 : 0));
    }
}
