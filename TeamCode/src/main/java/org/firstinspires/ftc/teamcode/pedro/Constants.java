package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Pose;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.OTOSConfig;
import com.pedropathing.revhub.localizers.OTOSLocalizer;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("leftFront");
        c.frontRightName.set("rightFront");
        c.backLeftName.set("leftBack");
        c.backRightName.set("rightBack");

        c.frontLeftDirection.set(DcMotorSimple.Direction.FORWARD);
        c.frontRightDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backLeftDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backRightDirection.set(DcMotorSimple.Direction.REVERSE);

        c.manualBrakeMode.set(true);
    });

    public static OTOSConfig localizerConfig = new OTOSConfig(c -> {
        c.name.set("otos");
        c.linearScalar.set(1.066666666666667);
        c.angularScalar.set(0.9872734284612351);
        c.offset.set(new Pose(-0.0, -0.0, -Math.PI/2));
        c.linearUnit.set(DistanceUnit.INCH);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.05);
                Controller secondaryTranslationalForward = Controller.proportional(0.02);
                Controller primaryTranslationalLateral = Controller.proportional(0.05);
                Controller secondaryTranslationalLateral = Controller.proportional(0.02);
                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(0.0, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(0.0, primaryTranslationalLateral));
                c.coast.set(Controller.proportionalFeedforward(0.022));
                c.brake.set(Controller.proportionalFeedforward(0.022));
                c.headingFeedback.set(Controller.proportional(0.02));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.1, 0.05));
                c.linearBrakeCoefficients.set(Matrix.diag(0.2, 0.2));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.008, 0.008));
                c.maxAchievableForwardVelocity.set(45.0);
                c.maxAchievableStrafeVelocity.set(35.0);
                c.naturalForwardDeceleration.set(40.0);
                c.naturalStrafeDeceleration.set(30.0);
            }
    );
    public static Follower create(HardwareMap h) {
        return new Follower(
                new OTOSLocalizer(h, localizerConfig),
                new Mecanum(h, drivetrainConfig),
                //TODO change to existing ForesightConfig
                new Foresight(foresightConfig)
        );
    }
}