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

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.CRServoEx;
import dev.nextftc.hardware.impl.MotorEx;

/* Hooded Shooter */
public class HoodedShooter implements Subsystem {
    public static final HoodedShooter INSTANCE = new HoodedShooter();

    /* VARIABLES */
    public static class Shooter {
        // Init
        public Shooter() {
            state = false;
            controller.setGoal(new KineticState(0.0, 0.0));
        }

        // Variables
        public boolean state;
        public final MotorEx motor = new MotorEx(RobotConfig.MOTOR_SHOOTER);
        public static final double percentage = 0.9;
        public static final double maxVelocity = 1800.0;

        // PID Controller
        private static final ControlSystem controller = ControlSystem.builder()
                .velPid(0.04, 0.0, 0.001)
                .build();

        // API
        public void update() {
            if (state) {
                // outVelocity = maxVelocity * percentage
                controller.setGoal(new KineticState(0.0, maxVelocity * percentage));
                motor.setPower(controller.calculate(new KineticState(motor.getCurrentPosition(), motor.getVelocity())));
            }
            else {
                controller.setGoal(new KineticState(0.0, 0.0));
                motor.setPower(0.0);
            }
        }

        public void setState(boolean newState) {
            state = newState;
        }

        public double getCurrentPercentage() {
            return motor.getVelocity() / maxVelocity;
        }
    }

    public static class Turret {
        // Init
        public Turret() {
            DESIRED_TAG_ID = 20;
            rotateSpeed = 0.0;
        }

        // Variables
        private int DESIRED_TAG_ID;
        private final CRServoEx rotate = new CRServoEx(RobotConfig.SERVO_TURRET_ROTATE); // Rotating servo
        public double rotateSpeed;

        // API
        public void setRotateSpeed(double newSpeed) {
            rotateSpeed = newSpeed;
        }

        public double getRotateSpeed() {
            return rotate.getPower();
        }

        public void updateDesiredTagID(boolean RED, boolean BLUE) {
            if (RED) DESIRED_TAG_ID = 24;
            if (BLUE) DESIRED_TAG_ID = 20;
        }

        public void update() {
            rotate.setPower(rotateSpeed);
        }
    }

    public Shooter shooter = new Shooter();
    public Turret turret = new Turret();

    /* SUBSYSTEM FUNCTIONS */
    @Override
    public void initialize() {

    }

    @Override
    public void periodic() {
        shooter.update();
        turret.update();
    }

    /* API */
    public void setShooterState(boolean newState) {
        shooter.setState(newState);
    }

    public void setTurretRotateSpeed(double speed) {
        turret.setRotateSpeed(speed);
    }
}
