//package org.firstinspires.ftc.teamcode.robot.opmode.teleop;
//
//import com.pedropathing.drivetrain.DrivePowers;
//import com.pedropathing.follower.Follower;
//import com.pedropathing.follower.ManualDrive;
//import com.pedropathing.math.Pose;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//
//import org.firstinspires.ftc.teamcode.pedro.Constants;
//import org.firstinspires.ftc.teamcode.robot.opmode.OpModeStorage;
//import org.firstinspires.ftc.teamcode.robot.subsystem.Intake;
//import org.firstinspires.ftc.teamcode.robot.subsystem.Shooter;
//import org.firstinspires.ftc.teamcode.robot.subsystem.Storage;
//
//@TeleOp(name = "TeleOP", group = "TeleOp")
//public class TeleOP extends OpMode {
//    private Shooter shooter;
//    private Intake intake;
//    private Follower follower;
//    private Storage storage;
//
//    @Override
//    public void init() {
//        shooter = new Shooter(hardwareMap);
//        intake = new Intake(hardwareMap);
//        storage = new Storage(hardwareMap);
////        follower = Constants.create(hardwareMap);
//
//        follower.setPose(OpModeStorage.autonomousEndPose);
//        follower.update();
//
//        telemetry.addData("init completed", "");
//        telemetry.update();
//    }
//
//    @Override
//    public void loop() {
//        DrivePowers powers = ManualDrive.fieldCentric(
//                -gamepad1.left_stick_y,
//                -gamepad1.left_stick_x,
//                gamepad1.right_stick_x,
//                follower.pose().heading()
//        );
//
//        ManualDrive.driveOrHold(follower, powers);
//        follower.update();
//
//        if (gamepad1.left_bumper) {
//            intake.intake();
//        } else {
//            intake.stop();
//        }
//
//        if (gamepad1.right_bumper) {
//            shooter.shoot();
//        } else if (gamepad1.right_trigger_pressed) {
//            shooter.reverse();
//        } else {
//            shooter.stop();
//        }
//
//        if (shooter.getVelocity() > 800) {
//            storage.storageIn();
//            intake.intake();
//        } else {
//            storage.stop();
//        }
//
//        if (gamepad1.y) {
//            follower.setHeading(0);
//        }
//
//        Pose robotPose = follower.pose();
//
//        telemetry.addData("Robot X", robotPose.x());
//        telemetry.addData("Robot Y", robotPose.y());
//        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));
//        telemetry.addData("", "");
//        telemetry.addData("Shooter vel", shooter.getVelocity());
//    }
//}