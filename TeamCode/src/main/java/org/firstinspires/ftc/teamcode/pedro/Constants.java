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

        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);

        c.manualBrakeMode.set(true);
    });

    public static OTOSConfig localizerConfig = new OTOSConfig(c -> {
        c.name.set("otos");
        c.linearScalar.set(1.1480099310344827);
        c.angularScalar.set(0.9874772289281946);
        c.offset.set(new Pose(0, 0, Math.PI/2));
        c.linearUnit.set(DistanceUnit.INCH);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.22759325865191732);
                Controller secondaryTranslationalForward = Controller.proportional(0.08408962766800945);
                Controller primaryTranslationalLateral = Controller.proportional(0.4275736412552012);
                Controller secondaryTranslationalLateral = Controller.proportional(0.15797703546568564);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.019261168867170976));
                c.brake.set(Controller.proportionalFeedforward(0.016371993537095328));

                c.headingFeedback.set(Controller.proportional(2.3613049240057395));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.08139350704230204, 0.003315342398207586));

                c.linearBrakeCoefficients.set(Matrix.diag(0.09683768706367524, 0.07723241930270128));
                c.quadraticBrakeCoefficients.set(Matrix.diag(9.625913282560673E-4, 0.0015923740343263342));

                c.maxAchievableForwardVelocity.set(59.69926558110018);
                c.maxAchievableStrafeVelocity.set(45.79836606268377);
                c.naturalForwardDeceleration.set(27.70312328150595);
                c.naturalStrafeDeceleration.set(66.3575952522091);
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