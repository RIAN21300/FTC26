package org.firstinspires.ftc.teamcode.Subsystems;

import org.firstinspires.ftc.teamcode.RobotConfig;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;

public class Intake implements Subsystem {
    public static final Intake INSTANCE = new Intake();

    /* VARIABLES */
    public boolean state = false;
    private final MotorEx motor = new MotorEx(RobotConfig.MOTOR_INTAKE);

    /* SUBSYSTEM FUNCTIONS */
    @Override
    public void periodic() {
        if (state) {
            motor.setPower(1.0);
        }
        else {
            motor.setPower(0.0);
        }
    }
}
