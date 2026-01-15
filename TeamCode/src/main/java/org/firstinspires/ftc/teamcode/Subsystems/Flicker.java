package org.firstinspires.ftc.teamcode.Subsystems;

import org.firstinspires.ftc.teamcode.RobotConfig;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;

public class Flicker implements Subsystem {
    public static final Flicker INSTANCE = new Flicker();

    private ServoEx[] servo;
    public boolean[] state;
    private static final double upPos = 1.0;
    private static final double downPos = 0;
    private static final int armCount = 3;

    public Flicker() {
        for (int i = 0; i < armCount; i++) {
            servo[i] = new ServoEx(RobotConfig.SERVO_FLICKER[i]);
        }
    }

    /* SUBSYSTEM FUNCTIONS */
    @Override
    public void periodic() {
        for (int i = 0; i < armCount; i++) {
            if (state[i]) servo[i].setPosition(upPos);
            else servo[i].setPosition(downPos);
        }
    }

    /* API */
    public void setArmState(int armNum, boolean armState) {
        state[armNum] = armState;
    }
}