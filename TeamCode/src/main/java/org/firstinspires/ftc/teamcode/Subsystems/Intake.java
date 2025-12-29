package org.firstinspires.ftc.teamcode.Subsystems;

import org.firstinspires.ftc.teamcode.RobotConfig;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;

public class Intake implements Subsystem {
    public static final Intake INSTANCE = new Intake();

    private Intake() {}

    public boolean state;
    private final MotorEx motor = new MotorEx(RobotConfig.MOTOR_INTAKE);

    @Override
    public void periodic() {
        motor.setPower((state ? 1 : 0));
    }
}
