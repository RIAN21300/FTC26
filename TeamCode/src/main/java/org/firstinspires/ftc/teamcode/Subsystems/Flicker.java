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

    // Arm class, using 180-degree servo
    public static class Arm {
        // Init
        public Arm(String servoName) {
            servo = new ServoEx(servoName);
        }

        // Variables
        private static final double LIFT_POS = 8.0/9.0;
        private static final double REST_POS = 1.0/9.0;
        private final ServoEx servo;
        private boolean state;

        // API
        public double getPos() {
            return servo.getPosition();
        }

        public void setState(boolean newState) {
            state = newState;
        }

        public void update() {
            if (state) servo.setPosition(LIFT_POS);
            else servo.setPosition(REST_POS);
        }
    }

    public static final Arm[] arms = new Arm[armCount];

    /* SUBSYSTEM FUNCTIONS */
    @Override
    public void initialize() {
        for (int i = 0; i < armCount; ++i) {
            arms[i] = new Arm(RobotConfig.SERVO_FLICKER[i]);
            arms[i].setState(false);
        }
    }

    @Override
    public void periodic() {
        for (int i = 0; i < armCount; ++i) {
            arms[i].update();
        }
    }

    /* API */
    public void setArmState(ArmName armName, boolean newArmState) {
        arms[armName.ordinal()].setState(newArmState);
    }

    public boolean getArmState(ArmName armName) {
        return arms[armName.ordinal()].state;
    }

    public double getArmPos(ArmName armName) {
        return arms[armName.ordinal()].getPos();
    }
}