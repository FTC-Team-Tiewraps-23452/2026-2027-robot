
package org.firstinspires.ftc.teamcode.robot.opmode.teleop;

import static com.pedropathing.ivy.Scheduler.schedule;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.robot.Commands;
import org.firstinspires.ftc.teamcode.robot.subsystem.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystem.Shooter;
import org.firstinspires.ftc.teamcode.robot.subsystem.Storage;
import org.firstinspires.ftc.teamcode.robot.opmode.OpModeStorage;

@TeleOp(name = "TeleOP", group = "TeleOp")
public class TeleOP extends OpMode {

    private Shooter shooter;
    private Intake intake;
    private Storage storage;
    private Follower follower;
    private Commands commands;

    private boolean lastLeftBumper = false;
    private boolean lastRightBumper = false;
    private boolean lastRightTrigger = false;
    private boolean prevY = false;

    @Override
    public void init() {
        Scheduler.reset();

        shooter = new Shooter(hardwareMap);
        intake = new Intake(hardwareMap);
        storage = new Storage(hardwareMap);

        commands = new Commands(shooter, storage, intake);

        follower = Constants.create(hardwareMap);

        if (OpModeStorage.autoPoseValid) {
            follower.setPose(OpModeStorage.autonomousEndPose);
            OpModeStorage.autoPoseValid = false;
        }

        follower.update();

        telemetry.addData("Init", "Completed");
        telemetry.update();
    }

    @Override
    public void loop() {
        Scheduler.execute();

        DrivePowers powers = ManualDrive.fieldCentric(
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x,
                follower.pose().heading()
        );

        ManualDrive.driveOrHold(follower, powers);
        follower.update();

        boolean leftBumper = gamepad1.left_bumper;
        boolean rightBumper = gamepad1.right_bumper;
        boolean rightTrigger = gamepad1.right_trigger > 0.1;

        if (leftBumper && !lastLeftBumper) {
            schedule(commands.intake());
        }

        if (!leftBumper && lastLeftBumper && !rightBumper) {
            schedule(commands.stopIntake());
        }

        if (rightBumper && !lastRightBumper) {
            schedule(commands.shoot().then(commands.feed()));
        }

        if (!rightBumper && lastRightBumper) {
            schedule(commands.stopAll());
        }

        if (rightTrigger && !lastRightTrigger) {
            schedule(commands.reverseShooter());
        }

        if (!rightTrigger && lastRightTrigger && !rightBumper) {
            schedule(commands.stopAll());
        }

        lastRightBumper = rightBumper;
        lastRightTrigger = rightTrigger;
        lastLeftBumper = leftBumper;

        if (gamepad1.y && !prevY && Math.abs(gamepad1.left_stick_x) < 0.05
                && Math.abs(gamepad1.left_stick_y) < 0.05) {
            Pose p = follower.pose();
            follower.setPose(new Pose(p.x(), p.y(), 0));
        }
        prevY = gamepad1.y;

        Pose robotPose = follower.pose();

        telemetry.addData("Robot X", robotPose.x());
        telemetry.addData("Robot Y", robotPose.y());
        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));
        telemetry.addData("Shooter Target (TPS)", 1000);
        telemetry.addData("Shooter Actual (TPS)", shooter.getVelocity());
        telemetry.addData("Shooter Ready", commands.shooterReady());

        telemetry.update();
    }
}
