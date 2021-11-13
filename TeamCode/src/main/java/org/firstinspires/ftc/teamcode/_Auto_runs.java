/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.vuforia.CameraDevice;

/**
 *
 * This code is for _Red_Carousel
 *
 * Autonomous programs:  These are the main programs and are named for the starting
 *  position.
 *
 *  _Red_Carousel is used when starting from the block nearest the Carousel when
 *      part of the Red Alliance.
 *  _Red_Freight is used when starting from the block nearest the Freight when
 *      part of the Red Alliance.
 *  _Blue_Carousel is used when starting from the block nearest the Carousel when
 *      part of the Vlue Alliance.
 *  _Blue_Freight is used when starting from the block nearest the Freight when
 *      part of the Blue Alliance.
 **/

/**
 * The code REQUIRES that you DO have encoders on the wheels,
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forwards, and causes the encoders to count UP.
 *
 *  This methods assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run
 *  profile
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 **/

@Autonomous(name="AUTONOMOUS RUNS", group="OnBot")

//@Disabled
public class _Auto_runs extends LinearOpMode {

    /* Declare OpMode members. */

    private ElapsedTime     runtime = new ElapsedTime();
//    public ConveyorCommon robot = new ConveyorCommon();
//    public SpinnerCommon spinner = new SpinnerCommon();

    static boolean alliance = true;  //  true for RED, false for BLUE

    static double widthOfRobot = 17.0;
    location loc = new location(widthOfRobot,96);  //Start Using Center of Robot, 2nd tile from front
    location car_loc = new location (widthOfRobot,25);
    location hub_loc = new location (93, 150);
    location freight_loc = new location (widthOfRobot,400);
    location freight_park_loc = new location (90,400);
    location storage_loc = new location (90,30);

    int myLevel = 0;

    AutoCommon auto=null;

    Boolean blue=false;
    int encoderDesired=0;
   @Override
    public void runOpMode() {

       /*
        * Initialize the drive system variables.
        * The init() method of the hardware class does all the work here
        */
       auto = new AutoCommon(this);

       auto.resetEncoders();
/*
       auto.liftClaw.robot.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
       auto.liftClaw.disengageGrabbers();

       auto.liftClaw.robot.claw.setPosition(.8);

*/
       // Wait for the game to start (driver presses PLAY)
       waitForStart();
       tasks();
   }


    /**
     *   tasks() -- steps to achieve points
     *
     *   1.  Move away from wall
     *   2.  Identify level to place block
     *   3.  Adjust level of conveyer
     *   4.  Lateral to center on shipping hub
     *   5.  Move in to shipping hub for placement of block
     *   6.  Load block onto shipping hub       26 points (assuming Team Shipping Element)
     *   7.  Move back from shipping hub
     *   8.  Lateral over to Carousel
     *   9.  Turn Carousel to drop duck         10 points
     *  10.  Move to Storage Unit
     *  11.  Center in Storage Unit.             6 points
     *
     *
     **/
    public void tasks() {
        myLevel = getLevel();
        loc = carousel(loc);
        loc = shippingHub(loc);
        loc = freight(loc);
        loc = freight_park(loc);
    }
    public int getLevel(){
        return 0; //Detail to be Defined Later
    }
    public location storage (location loc) {
        auto.encoderLateral(-0.3, 5, storage_loc.x - loc.x,
                alliance, false, false);
        auto.encoderDrive(0.5, storage_loc.y - loc.y, 10, false);
        return storage_loc;
    }
    public location freight (location loc) {
        auto.encoderLateral(-0.3, 5, freight_loc.x - loc.x,
                alliance, false, false);
        auto.encoderDrive(0.5, freight_loc.y - loc.y, 10, false);
        return freight_loc;
    }
    public location freight_park (location loc) {
        auto.encoderLateral(-0.3, 5, freight_park_loc.x - loc.x,
                alliance, false, false);
        auto.encoderDrive(0.5, freight_park_loc.y - loc.y, 10, false);
        return freight_loc;
    }
    public location shippingHub(location loc) {
        auto.encoderLateral(-0.3, 5, hub_loc.x - loc.x,
                alliance, false, false);
        auto.encoderDrive(0.5, hub_loc.y - loc.y, 10, false);
        //  add code to load block on shipping hub using level
        return hub_loc;
    }
    public location carousel(location loc) {
        auto.encoderLateral(0.3, 5, car_loc.x - loc.x,
                alliance, false, false);

        auto.encoderDrive(0.5, - (car_loc.y - loc.y), 10, true);
        //  add code to rotate carousel
        return car_loc;
    }
    public class location{
        public double x;
        public double y;
        public location(double ix, double iy){
            x = ix;
            y = iy;
        }
    }
}