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
        private static final double LIFT_POS = 2/9.0;
        private static final double REST_POS = 7/9.0;
        private final ServoEx servo;
        private boolean state;

        // API
        public void setLiftPos() {
            state = true;
        }

        public void setRestPos() {
            state = false;
        }

        public double getPos() {
            return servo.getPosition();
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
            arms[i].setRestPos();
        }
    }

    /* API */
    public void lift(ArmName armName) {
        arms[armName.ordinal()].setLiftPos();
    }

    public void rest(ArmName armName) {
        arms[armName.ordinal()].setRestPos();
    }

    @Override
    public void periodic() {
        for (int i = 0; i < armCount; ++i) {
            arms[i].update();
        }
    }

    public double get_pos(ArmName armName) {
        return arms[armName.ordinal()].getPos();
    }
}