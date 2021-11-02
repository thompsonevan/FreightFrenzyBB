package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@TeleOp(name="Sensor Teleop", group="Pushbot")
//@Disabled
public class SensorTeleop extends LinearOpMode {

    @Override
    public void runOpMode() {
        SensorCommon sensor = new SensorCommon(this);

        waitForStart();

        while (opModeIsActive()) {

            sensor.excuteTeleop();

            telemetry.update();
        }
    }
}
