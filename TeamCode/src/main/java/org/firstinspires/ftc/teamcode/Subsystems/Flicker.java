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
    public static class Arm {
        // Init
        public Arm(String servoName) {
            servo = new ServoEx(servoName);
        }

        // Variables
        private static final double LIFT_POS = 0.5;
        private static final double REST_POS = 0.0;
        private final ServoEx servo;

        // API
        public void setLiftPos() {
            servo.setPosition(LIFT_POS);
        }

        public void setRestPos() {
            servo.setPosition(REST_POS);
        }

        public double getPos() {
            return servo.getPosition();
        }
    }

    public static final Arm[] arms = new Arm[armCount];

    /* SUBSYSTEM FUNCTIONS */
    @Override
    public void initialize() {
        for (int i = 0; i < armCount; ++i) {
            arms[i] = new Arm(RobotConfig.SERVO_FLICKER[i]);
        }

        for (int i = 0; i < armCount; ++i) {
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

    public double get_pos(ArmName armName) {
        return arms[armName.ordinal()].getPos();
    }
}