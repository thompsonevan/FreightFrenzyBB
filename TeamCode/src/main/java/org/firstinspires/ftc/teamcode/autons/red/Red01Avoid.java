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

package org.firstinspires.ftc.teamcode.autons.red;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.autons.AutoCommon;

/**
 * This file illustrates the concept of driving a path based on encoder counts.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByTime;
 *
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forwards, and causes the encoders to count UP.
 *
 *   The desired path in this example is:
 *   - Drive forward for 48 inches
 *   - Spin right for 12 Inches
 *   - Drive Backwards for 24 inches
 *   - Stop and close the claw.
 *
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 *  This methods assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="RED 01 (Avoid)", group="OnBot")

//@Disabled
public class Red01Avoid extends LinearOpMode {
    private ElapsedTime     runtime = new ElapsedTime();

    AutoCommon auto=null;

    boolean red=true;

    double driveSpeed = .5;
    double strafeSpeed = .5;
    double conveyorSpeed = .8;
    double spinnerSpeed = .1;

    @Override
    public void runOpMode() {
        auto = new AutoCommon(this, red);

        auto.resetEncoders();

        while(!isStarted()){
            auto.checkDistances();
        }

        int pos = auto.getPos(!red);

        auto.conveyor.liftConveyor(pos, .8, 4);

        auto.encoderStrafe(strafeSpeed, 30, 250, false, false, false);

        auto.encoderDrive(driveSpeed,850,10, false);

        auto.moveSpinner(spinnerSpeed, 2);

        auto.encoderDrive(driveSpeed,-900,10, false);

        auto.encoderStrafe(strafeSpeed, 4, 350, true, false, false);

        int encoderPos = auto.getDistFromHub(pos, red);

        auto.encoderStrafe(strafeSpeed,10,encoderPos+100,false,false,false);

        auto.encoderDrive(driveSpeed, -1000, 10,false);

        auto.conveyor.pushConveyor(conveyorSpeed, 30);

        auto.encoderDrive(driveSpeed, -1000, 10,false);

        auto.encoderStrafe(strafeSpeed,10,encoderPos + 2000,true,false,false);

        auto.conveyor.liftConveyor(3, .8, 30);

        auto.encoderDrive(driveSpeed, -1400, 10,false);
    }
}