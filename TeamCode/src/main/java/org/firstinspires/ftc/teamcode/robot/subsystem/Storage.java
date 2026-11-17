package org.firstinspires.ftc.teamcode.robot.subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Storage {
    private DcMotor storageMotor;

    public Storage(HardwareMap hardwareMap) {
        storageMotor = hardwareMap.get(DcMotor.class, "storageMotor");
        storageMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        storageMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void storageIn() {
        storageMotor.setPower(.7);
    }

    public void stop() {
        storageMotor.setPower(0.0);
    }
}
