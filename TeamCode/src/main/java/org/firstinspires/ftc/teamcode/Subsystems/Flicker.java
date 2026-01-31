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


import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RobotConfig;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;

public class Flicker implements Subsystem {
    public static final Flicker INSTANCE = new Flicker();

    /* VARIABLES */
    private static final int armCount = 3;

    // Arm class, using 180-degree servo
    public static class Arm {
        // Init
        public Arm(String servoName) {
            servo = new ServoEx(servoName);
        }

        // Variables
        private double LIFT_POS = 7.9/9.0;
        private double REST_POS = 1.1/9.0;
        private final ServoEx servo;
        private boolean armState = false;
        private final ElapsedTime timer = new ElapsedTime();
        // static
        private static final double LIFT_DURATION = 600; // ms
        public static boolean busy = false;

        // API
        public double getPos() {
            return servo.getPosition();
        }

        public void run() {
            busy = true;
            timer.reset();
            armState = true;
        }

        public void lift() {
            servo.setPosition(LIFT_POS);
        }

        public void rest() {
            servo.setPosition(REST_POS);
        }

        public void reverse() {
            LIFT_POS = 1.0/9.0;        // Reversed Arm position
            REST_POS = 8.0/9.0;
        }

        public void update() {
            if (armState && timer.milliseconds() <= LIFT_DURATION) {
                lift();
            } else {
                armState = false;
                rest();
                busy = false;
            }
        }

        public void autoFlickMotif(String pattern) {
            for (int i = 0; i < pattern.length(); ++i) {
                if (pattern.charAt(i) == 'G') {
                    for (RobotConfig.BallSlotName slotName : RobotConfig.BallSlotName.values()) {
                        if (ColorCamera.INSTANCE.checkSlotForGreen(slotName)) {
                            run();
                            break;
                        }
                    }
                }

                if (pattern.charAt(i) == 'P') {
                    for (RobotConfig.BallSlotName slotName : RobotConfig.BallSlotName.values()) {
                        if (ColorCamera.INSTANCE.checkSlotForPurple(slotName)) {
                            run();
                            break;
                        }
                    }
                }
            }
        }
    }

    public static final Arm[] arms = new Arm[armCount];

    /* SUBSYSTEM FUNCTIONS */
    @Override
    public void initialize() {
        for (int i = 0; i < armCount; ++i) {
            arms[i] = new Arm(RobotConfig.SERVO_FLICKER[i]);
        }

        arms[RobotConfig.BallSlotName.DownRight.ordinal()].reverse();

        for (int i = 0; i < armCount; ++i) {
            arms[i].rest();
        }
    }

    @Override
    public void periodic() {
        for (int i = 0; i < armCount; ++i) {
            arms[i].update();
        }
    }

    /* API */
    public void liftArm(RobotConfig.BallSlotName armName) {
        arms[armName.ordinal()].lift();
    }

    public void restArm(RobotConfig.BallSlotName armName) {
        arms[armName.ordinal()].rest();
    }

    public void runArm(RobotConfig.BallSlotName armName) {
        if (!Arm.busy) {
            arms[armName.ordinal()].run();
        }
    }

    public double getArmPos(RobotConfig.BallSlotName armName) {
        return arms[armName.ordinal()].getPos();
    }

    public void autoFlick(String pattern) {
        int greenPos = 0;
        boolean[] flicked = {false, false, false};

        for (RobotConfig.BallSlotName slotName : RobotConfig.BallSlotName.values()) {
            if (ColorCamera.INSTANCE.checkSlotForGreen(slotName)) {
                greenPos = slotName.ordinal();
                break;
            }
        }

        for (int i = 0; i < armCount; i++) {
            if (pattern.charAt(i) == 'G') {
                arms[greenPos].run();
                flicked[greenPos] = true;
            }
            else {
                for (int j = 0; j < armCount; j++) {
                    if (!flicked[j] && j != greenPos) {
                        arms[j].run();
                        flicked[j] = true;
                        break;
                    }
                }
            }
        }
    }
}