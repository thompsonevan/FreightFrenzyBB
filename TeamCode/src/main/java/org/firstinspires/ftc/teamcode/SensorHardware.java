/*

This is the Hardware class, it is used to declare all the components and create objects
of them so they can be used in the code to run them

 */

// This just tells the code where this file is located in the file structure
package org.firstinspires.ftc.teamcode;

// This imports all the different dependencies and libraries and other classes we use in this file
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class SensorHardware {

    public DistanceSensor ds1;
    public Servo s1;

    HardwareMap hwMap =  null;
    private ElapsedTime period = new ElapsedTime();

    public void init(HardwareMap ahwMap) {
        hwMap = ahwMap;

        s1 = hwMap.get(Servo.class, "s1");

        s1.setPosition(0);

        ds1 = hwMap.get(DistanceSensor.class, "ds1");
    }
}

