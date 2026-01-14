package org.firstinspires.ftc.teamcode.Subsystems;

import org.firstinspires.ftc.teamcode.RobotConfig;

import dev.nextftc.bindings.Button;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;

public class Flicker implements Subsystem {
    public static final Flicker INSTANCE = new Flicker();

    /* VARIABLES */
    private static final int armCount = 3;
    // ARM
    public enum ArmName {
        UpRight,
        Left,
        DownRight

    }

    private static class Arm {
        // Variables
        private final ServoEx servo;
        private final double LIFT_POS = 1.0;
        private final double REST_POS = 0.0;

        // Init
        public Arm(String __NAME__) {
            servo = new ServoEx(__NAME__);

            servo.setPosition(REST_POS);
        }

        // API
        public void lift() {
            servo.setPosition(LIFT_POS);
        }

        public void rest() {
            servo.setPosition(REST_POS);
        }
    }

    private static final Arm[] arms = new Arm[armCount];

    /* SUBSYSTEM FUNCTIONS */
    @Override
    public void initialize() {
        for (int i = 0; i < armCount; ++i) {
            arms[i] = new Arm(RobotConfig.SERVO_FLICKER[i]);
        }
    }

    /* API */
    public Flicker bindArmTo(ArmName armName, Button __button__) {
        __button__
                .whenBecomesTrue (() -> arms[armName.ordinal()].lift())
                .whenBecomesFalse(() -> arms[armName.ordinal()].rest());
        return this;
    }
}
