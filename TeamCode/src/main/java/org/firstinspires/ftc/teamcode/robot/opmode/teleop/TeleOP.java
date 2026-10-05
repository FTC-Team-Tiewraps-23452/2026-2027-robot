package org.firstinspires.ftc.teamcode.robot.opmode.teleop;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.robot.subsystem.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystem.Shooter;

@TeleOp(name = "TeleOP", group = "TeleOp")
public class TeleOP extends OpMode {
    private Shooter shooter;
    private Intake intake;
    private Follower follower;

    @Override
    public void init() {
        shooter = new Shooter(hardwareMap);
        intake = new Intake(hardwareMap);
        follower = Constants.create(hardwareMap);

        telemetry.addData("init completed", "");
        telemetry.update();
    }

    @Override
    public void loop() {
        //TODO change to field-centric drive
        ManualDrive.driveOrHold(
                follower,
                -gamepad1.left_stick_y,
                gamepad1.left_stick_x,
                gamepad1.right_stick_x
        );

        follower.update();

        if (gamepad1.left_bumper) {
            intake.intake();
        } else {
            intake.stop();
        }

        if (gamepad1.right_bumper) {
            shooter.shoot();
        } else {
            shooter.stop();
        }
    }
}