package org.firstinspires.ftc.teamcode.robot.opmode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.subsystem.Shooter;

@TeleOp(name = "main TeleOP", group = "TeleOp")
public class TeleOP extends OpMode {
    Shooter shooter;

    @Override
    public void init() {
        shooter = new Shooter(hardwareMap);
        telemetry.addData("init completed", "");
        telemetry.update();
    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            shooter.shoot();
            telemetry.addData("shooter enabled", "");
        } if (gamepad1.b) {
            shooter.stop();
            telemetry.addData("shooter disabled", "");
        }
        telemetry.update();
    }
}