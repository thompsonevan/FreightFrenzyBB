package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class ExtenderCommon {

    public ExtenderHardware robot = new ExtenderHardware();

    public LinearOpMode curOpMode;

    public float s1Val = 0;
    public float s2Val = 0;
    public float s3Val = 0;
    public float s4Val = 0;
    public float s5Val = 0;

    public int state = 0;

    public ExtenderCommon(LinearOpMode owningOpMode){
        curOpMode = owningOpMode;
        robot.init(curOpMode.hardwareMap);
    }

    public void excuteTeleop(){
        if(curOpMode.gamepad1.start){
            state = 0;
        } else if (curOpMode.gamepad1.b){
            state = 1;
        } else if (curOpMode.gamepad1.x){
            state = 2;
        } else if (curOpMode.gamepad1.y){
            state = 3;
        } else if (curOpMode.gamepad1.a){
            state = 4;
        }

        if(state == 0){
            while(curOpMode.gamepad1.left_trigger > .1){
                s1Val = setVal(s1Val, .01f);
            }
            while(curOpMode.gamepad1.right_trigger > .1){
                s1Val -= .01;
            }
            robot.s1.setPosition(s1Val);
        }

    }

    public float setVal(float curVal, float changeVal){
        return .01f;
    }

}