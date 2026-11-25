package org.firstinspires.ftc.teamcode.robot.opmode.autonomous;

import static com.pedropathing.api.Paths.*;
import com.pedropathing.api.Paths;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.robot.Commands;
import org.firstinspires.ftc.teamcode.robot.subsystem.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystem.Shooter;
import org.firstinspires.ftc.teamcode.robot.subsystem.Storage;

@Autonomous(name = "red advanced", group = "Autonomous")
public class autoRedAdvanced extends LinearOpMode {

    private Follower follower;

    private Shooter shooter;
    private Intake intake;
    private Storage storage;
    private Commands commands;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(56, 8, -90);
    private final Pose path1 = poseFactory.of(56, 24, -90);
    private final Pose point2 = poseFactory.of(8, 24, -90);
    private final Pose point3 = poseFactory.of(8, 8.1945, -90);
    private final Pose point4Start = poseFactory.of(8, 8.1945, -90);
    private final Pose point4 = poseFactory.of(56, 117, 90);
    private final Pose point4Control1 = poseFactory.of(34.9468, 24.4013, 0);
    private final Pose point4Control2 = poseFactory.of(0, 114.3884, 0);
    private final Pose point5 = poseFactory.of(8.704, 123.0223, 90);
    private final Pose point5Control1 = poseFactory.of(32.0939, 127.8014, 0);

    // Autonomous routine
    public Command autoRoutine() {
        return sequential(
                parallel(follow(follower, path1()), commands.shoot()),
                commands.feedFor(5),
                commands.stopAll(),

                follow(follower, path2()),

                parallel(follow(follower, path3()), commands.intakeFor(3)),

                parallel(follow(follower, path4()), commands.shoot()),
                commands.feedFor(5),
                commands.stopAll(),

                follow(follower, path5())
        );
    }

    @Override
    public void runOpMode() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        follower.update();

        shooter = new Shooter(hardwareMap);
        intake = new Intake(hardwareMap);
        storage = new Storage(hardwareMap);
        commands = new Commands(shooter, storage, intake);

        waitForStart();
        schedule(autoRoutine());

        while (opModeIsActive()) {
            follower.update();
            Scheduler.execute();

            telemetry.addData("x", follower.pose().x());
            telemetry.addData("y", follower.pose().y());
            telemetry.addData("heading", follower.pose().heading());

            if (follower.currentPath() != null) {
                telemetry.addData("Current path distance remaining", follower.distanceToEndpoint());
                telemetry.addData("Path number", follower.pathIndex());
            }

            telemetry.update();
        }
    }

    public Path path1() {
        return Paths.line(start, path1).constant(path1).with(Constants.foresightConfig.maxPathSpeed.at(0.5));

    }

    public Path path2() {
        return Paths.line(path1, point2).constant(point2).with(Constants.foresightConfig.maxPathSpeed.at(0.5));
    }

    public Path path3() {
        return Paths.line(point2, point3).constant(point3).with(Constants.foresightConfig.maxPathSpeed.at(0.5));
    }

    public Path path4() {
        return Paths.curve(point4Start, point4Control1, point4Control2, point4).linear(point4Start, point4).with(Constants.foresightConfig.maxPathSpeed.at(0.5));
    }

    public Path path5() {
        return Paths.curve(point4, point5Control1, point5).constant(point5).with(Constants.foresightConfig.maxPathSpeed.at(0.5));
    }
}
