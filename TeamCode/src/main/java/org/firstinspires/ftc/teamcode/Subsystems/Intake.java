package org.firstinspires.ftc.teamcode.Subsystems;

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
    public boolean OUTTAKE_MODE;

    /* SUBSYSTEM FUNCTIONS */
    @Override
    public void initialize() {
        OUTTAKE_MODE = false;
        rest();
    }

    /* API */
    public void run() {
        if (OUTTAKE_MODE)
            motor.setPower(-INTAKE_POWER);
        else motor.setPower(INTAKE_POWER);
    }

    public void rest() {
        motor.setPower(REST_POWER);
    }

    /* DEBUG */
    public double get_power() {
        return motor.getPower();
    }
}