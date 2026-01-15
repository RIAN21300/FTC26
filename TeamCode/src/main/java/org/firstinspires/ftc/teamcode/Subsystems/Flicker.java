package org.firstinspires.ftc.teamcode.Subsystems;

import org.firstinspires.ftc.teamcode.RobotConfig;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;

public class Flicker implements Subsystem {
    public static final Flicker INSTANCE = new Flicker();

    /* VARIABLES */
    public enum ArmName {
        UpRight,
        Left,
        DownRight
    }

    private static final int armCount = 3;
    private static final double LIFT_POS = 1.0;
    private static final double REST_POS = 0.0;
    private static final ServoEx[] servos = new ServoEx[3];

    /* SUBSYSTEM FUNCTIONS */
    @Override
    public void initialize() {
        for (int i = 0; i < armCount; ++i) {
            servos[i] = new ServoEx(RobotConfig.SERVO_FLICKER[i]);
        }
    }

    /* API */
    public void lift(ArmName armName) {
        servos[armName.ordinal()].setPosition(LIFT_POS);
    }

    public void rest(ArmName armName) {
        servos[armName.ordinal()].setPosition(REST_POS);
    }
}