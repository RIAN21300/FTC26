package org.firstinspires.ftc.teamcode.Subsystems;

import org.firstinspires.ftc.teamcode.RobotConfig;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;

/* Sushi-roller Intake */
public class Intake implements Subsystem {
    public static final Intake INSTANCE = new Intake();

    /* VARIABLES */
    private final MotorEx motor = new MotorEx(RobotConfig.MOTOR_INTAKE);
    private final double INTAKE_POWER = 1.0;
    private final double REST_POWER = 0.0;

    /* API */
    public void run() {
        motor.setPower(INTAKE_POWER);
    }

    public void rest() {
        motor.setPower(REST_POWER);
    }

    /* DEBUG */
    public double get_power() {
        return motor.getPower();
    }
}
