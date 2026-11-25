
package org.firstinspires.ftc.teamcode.robot;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.robot.subsystem.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystem.Shooter;
import org.firstinspires.ftc.teamcode.robot.subsystem.Storage;

public class Commands {
    private final Shooter shooter;
    private final Storage storage;
    private final Intake intake;
    private double feedStart;
    private double intakeStart;
    private final double target = 1000;
    private final double velocityTolerance = 100;

    public Commands(Shooter shooter, Storage storage, Intake intake) {
        this.shooter = shooter;
        this.storage = storage;
        this.intake = intake;
    }

    public Command shoot() {
        return Command.build()
                .setStart(() -> shooter.setVelocity(target))
                .setDone(this::shooterReady)
                .requiring(shooter);
    }

    public Command intake() {
        return Command.build()
                .setStart(() -> intake.intake())
                .setExecute(() -> {})
                .setEnd(e -> intake.stop())
                .requiring(intake);
    }

    public Command stopIntake() {
        return Command.build()
                .setStart(() -> intake.stop())
                .setDone(() -> true)
                .requiring(intake);
    }

    public Command intakeFor(double seconds) {
        return Command.build()
                .setStart(() -> {
                    intakeStart = System.nanoTime();
                    intake.intake();
                })
                .setDone(() -> (System.nanoTime() - intakeStart) / 1e9 >= seconds)
                .setEnd(e -> intake.stop())
                .requiring(intake);
    }

    public Command feed() {
        return Command.build()
                .setExecute(() -> {
                    if (shooterReady()) {
                        storage.storageIn();
                        intake.intake();
                    } else {
                        storage.stop();
                        intake.stop();
                    }
                })
                .setEnd(e -> {
                    storage.stop();
                    intake.stop();
                })
                .requiring(storage, intake);
    }


    public Command feedFor(double seconds) {
        return Command.build()
                .setStart(() -> feedStart = System.nanoTime())
                .setExecute(() -> {
                    if (shooterReady()) {
                        storage.storageIn();
                        intake.intake();
                    } else {
                        storage.stop();
                        intake.stop();
                    }
                })
                .setDone(() ->
                        (System.nanoTime() - feedStart) / 1e9 >= seconds
                )
                .setEnd(e -> {
                    storage.stop();
                    intake.stop();
                })
                .requiring(storage, intake);
    }


    public Command reverseShooter() {
        return Command.build()
                .setStart(() -> shooter.setVelocity(-target))
                .requiring(shooter);
    }

    public Command stopAll() {
        return Command.build()
                .setStart(() -> {
                    shooter.stop();
                    storage.stop();
                    intake.stop();
                })
                .setDone(() -> true)
                .requiring(shooter, storage, intake);
    }

    public boolean shooterReady() {
        return Math.abs(shooter.getVelocity() - target) < velocityTolerance;
    }
}
