package org.firstinspires.ftc.teamcode.robot.subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Shooter {
    private DcMotor motor;

    public Shooter(HardwareMap hardwareMap) {
        motor = hardwareMap.get(DcMotor.class, "shooterMotor");
        motor.setDirection(DcMotorSimple.Direction.FORWARD);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public void shoot() {
        motor.setPower(.75);
    }

    public void stop() {
        motor.setPower(0);
    }
}
