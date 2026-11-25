package org.firstinspires.ftc.teamcode.robot.opmode.autonomous;

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
import org.firstinspires.ftc.teamcode.robot.opmode.OpModeStorage;

@Autonomous(name = "blue simple push", group = "Autonomous")
public class autoBlueSimplePush extends LinearOpMode {
    private Shooter shooter;
    private Intake intake;
    private Storage storage;
    private Follower follower;
    private Commands commands;

    private final PoseFactory poseFactory = PoseFactory.degrees().mirrorAroundPoint(70.75, 70.75);

    private final Pose start = poseFactory.of(56, 8, -90);
    private final Pose path1 = poseFactory.of(56, 24, -90);
    private final Pose point2Start = poseFactory.of(56, 24, -90);
    private final Pose point2 = poseFactory.of(8.5, 71.5468, 180);
    private final Pose point2Control1 = poseFactory.of(31.091, 30.775, 0);
    private final Pose point3 = poseFactory.of(8.5, 90.3947, 180);

    // Autonomous routine
    public Command autoRoutine() {
        return sequential(
                parallel(follow(follower, path1()), commands.shoot()),
                commands.feedFor(5),
                commands.stopAll(),

                follow(follower, path2()),

                follow(follower, path3())
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

        OpModeStorage.autonomousEndPose = follower.pose();
        OpModeStorage.autoPoseValid = true;
    }

    public Path path1() {
        return Paths
                .line(start, path1)
                .constant(path1)
                .with(Constants.foresightConfig.maxPathSpeed.at(0.5));
    }

    public Path path2() {
        return Paths
                .curve(point2Start, point2Control1, point2)
                .linear(point2Start, point2)
                .with(Constants.foresightConfig.maxPathSpeed.at(0.5));
    }

    public Path path3() {
        return Paths
                .line(point2, point3)
                .constant(point3)
                .with(Constants.foresightConfig.maxPathSpeed.at(0.5));
    }
}
