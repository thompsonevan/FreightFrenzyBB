package org.firstinspires.ftc.teamcode.autons;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.vuforia.CameraDevice;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.conveyor.ConveyorCommon;
import org.firstinspires.ftc.teamcode.drivetrain.DrivetrainCommon;
import org.firstinspires.ftc.teamcode.intake.IntakeCommon;
import org.firstinspires.ftc.teamcode.spinner.SpinnerCommon;

public class AutoCommon {

    public AutoHardware robot = null;
    public DrivetrainCommon chassis = null;
    public ConveyorCommon conveyor = null;
    public SpinnerCommon spinner = null;
    public IntakeCommon intake = null;


    public VectorF blockLoc = null;
    public CameraDevice vufCam = null;

    private ElapsedTime runtime = new ElapsedTime();

    private LinearOpMode curOpMode = null;

    public AutoCommon(LinearOpMode owningOpMode, boolean red) {


        curOpMode = owningOpMode;

        chassis = new DrivetrainCommon(curOpMode);
        robot = new AutoHardware();
        conveyor = new ConveyorCommon(curOpMode);
        spinner = new SpinnerCommon(curOpMode, red);
        intake = new IntakeCommon(curOpMode);


        robot.init(curOpMode.hardwareMap);
    }


    /*
     *  Method to perfmorm a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void encoderDrive(double speed,
                             int encoderValue,
                             double timeoutS, boolean pid) {
        double correction = 0;


        resetEncoders();
        // Ensure that the opmode is still active
        if (curOpMode.opModeIsActive()) {


            if (pid) {
                correction = chassis.pidDrive.performPID(chassis.getAngle());
            }

            // Determine new target position, and pass to motor controller;
            chassis.robot.driveLF.setTargetPosition(encoderValue);
            chassis.robot.driveRF.setTargetPosition(encoderValue);
            chassis.robot.driveLR.setTargetPosition(encoderValue);
            chassis.robot.driveRR.setTargetPosition(encoderValue);


            // Turn On RUN_TO_POSITION
            chassis.robot.driveLF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            chassis.robot.driveRF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            chassis.robot.driveLR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            chassis.robot.driveRR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            chassis.robot.driveLF.setPower(Math.abs(speed));
            chassis.robot.driveRF.setPower(Math.abs(speed));
            chassis.robot.driveLR.setPower(Math.abs(speed));
            chassis.robot.driveRR.setPower(Math.abs(speed));

            double currentPower = speed;

            int decelStart = (int) (encoderValue * .75);
            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (curOpMode.opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (chassis.robot.driveLF.isBusy() && chassis.robot.driveRF.isBusy()
                            && chassis.robot.driveLR.isBusy() && chassis.robot.driveRR.isBusy()
                    )) {


                chassis.robot.driveLF.setPower(Math.abs(currentPower - correction));
                chassis.robot.driveRF.setPower(Math.abs(currentPower + correction));
                chassis.robot.driveLR.setPower(Math.abs(currentPower - correction));
                chassis.robot.driveRR.setPower(Math.abs(currentPower + correction));

                // currentPower=rampUpDown(speed,currentPower,.2,chassis.robot.driveLF.getCurrentPosition(),decelStart);


                if (pid) {
                    correction = chassis.pidDrive.performPID(chassis.getAngle());

                    if (correction > 0) {

                    }


                    if (encoderValue < 0) {
                        correction = correction * (-1);
                    }
                }
            }


            // Stop all motion;
            chassis.robot.driveLF.setPower(0);
            chassis.robot.driveRF.setPower(0);
            chassis.robot.driveLR.setPower(0);
            chassis.robot.driveRR.setPower(0);


            // Turn off RUN_TO_POSITION
            chassis.robot.driveLF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            chassis.robot.driveRF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            chassis.robot.driveLR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            chassis.robot.driveRR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }


    public void encoderDriveToDistance(double speed,
                                       int encoderValue,
                                       double timeoutS, double distance, boolean pid) {
        double correction = 0;


        resetEncoders();
        // Ensure that the opmode is still active
        if (curOpMode.opModeIsActive()) {


            if (pid) {
                correction = chassis.pidDrive.performPID(chassis.getAngle());
            }

            // Determine new target position, and pass to motor controller;
            chassis.robot.driveLF.setTargetPosition(encoderValue);
            chassis.robot.driveRF.setTargetPosition(encoderValue);
            chassis.robot.driveLR.setTargetPosition(encoderValue);
            chassis.robot.driveRR.setTargetPosition(encoderValue);


            // Turn On RUN_TO_POSITION
            chassis.robot.driveLF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            chassis.robot.driveRF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            chassis.robot.driveLR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            chassis.robot.driveRR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            chassis.robot.driveLF.setPower(Math.abs(speed));
            chassis.robot.driveRF.setPower(Math.abs(speed));
            chassis.robot.driveLR.setPower(Math.abs(speed));
            chassis.robot.driveRR.setPower(Math.abs(speed));

            double currentPower = speed;

            int decelStart = (int) (encoderValue * .75);
            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (curOpMode.opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (chassis.robot.driveLF.isBusy() && chassis.robot.driveRF.isBusy()
                            && chassis.robot.driveLR.isBusy() && chassis.robot.driveRR.isBusy()
                    )) {


                chassis.robot.driveLF.setPower(Math.abs(currentPower - correction));
                chassis.robot.driveRF.setPower(Math.abs(currentPower + correction));
                chassis.robot.driveLR.setPower(Math.abs(currentPower - correction));
                chassis.robot.driveRR.setPower(Math.abs(currentPower + correction));

                // currentPower=rampUpDown(speed,currentPower,.2,chassis.robot.driveLF.getCurrentPosition(),decelStart);
/*
                if(liftClaw.robot.lift_check.getDistance(DistanceUnit.CM)<distance)
                {
                    //curOpMode.sleep(500);
                    chassis.robot.leftGuide.setPosition(.6);
                    chassis.robot.rightGuide.setPosition(.4);
                    curOpMode.sleep(500);
                    break;
                }
*/
                if (pid) {
                    correction = chassis.pidDrive.performPID(chassis.getAngle());

                    if (correction > 0) {

                    }


                    if (encoderValue < 0) {
                        correction = correction * (-1);
                    }
                }
            }


            // Stop all motion;
            chassis.robot.driveLF.setPower(0);
            chassis.robot.driveRF.setPower(0);
            chassis.robot.driveLR.setPower(0);
            chassis.robot.driveRR.setPower(0);


            // Turn off RUN_TO_POSITION
            chassis.robot.driveLF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            chassis.robot.driveRF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            chassis.robot.driveLR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            chassis.robot.driveRR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }


    public void encoderDriveWithDrift(double leftSpeed, double rightSpeed,
                                      int encoderValue,
                                      double timeoutS) {


        resetEncoders();
        // Ensure that the opmode is still active
        if (curOpMode.opModeIsActive()) {

            // Determine new target position, and pass to motor controller;
            chassis.robot.driveLF.setTargetPosition(encoderValue);
            chassis.robot.driveRF.setTargetPosition(encoderValue);
            chassis.robot.driveLR.setTargetPosition(encoderValue);
            chassis.robot.driveRR.setTargetPosition(encoderValue);


            // Turn On RUN_TO_POSITION
            chassis.robot.driveLF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            chassis.robot.driveRF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            chassis.robot.driveLR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            chassis.robot.driveRR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            chassis.robot.driveLF.setPower(Math.abs(leftSpeed));
            chassis.robot.driveRF.setPower(Math.abs(rightSpeed));
            chassis.robot.driveLR.setPower(Math.abs(leftSpeed));
            chassis.robot.driveRR.setPower(Math.abs(rightSpeed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (curOpMode.opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (chassis.robot.driveLF.isBusy() && chassis.robot.driveRF.isBusy()
                            && chassis.robot.driveLR.isBusy() && chassis.robot.driveRR.isBusy()
                    )) {

                chassis.robot.driveLF.setPower(Math.abs(leftSpeed));
                chassis.robot.driveRF.setPower(Math.abs(rightSpeed));
                chassis.robot.driveLR.setPower(Math.abs(leftSpeed));
                chassis.robot.driveRR.setPower(Math.abs(rightSpeed));


            }


            // Stop all motion;
            chassis.robot.driveLF.setPower(0);
            chassis.robot.driveRF.setPower(0);
            chassis.robot.driveLR.setPower(0);
            chassis.robot.driveRR.setPower(0);


            // Turn off RUN_TO_POSITION
            chassis.robot.driveLF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            chassis.robot.driveRF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            chassis.robot.driveLR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            chassis.robot.driveRR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }

    public void encoderTurn(double speed,
                            int encoderValue,
                            double timeoutS) {

        resetEncoders();
        // Ensure that the opmode is still active
        if (curOpMode.opModeIsActive()) {


            double correction = chassis.pidDrive.performPID(chassis.getAngle());

            // Determine new target position, and pass to motor controller;
            chassis.robot.driveLF.setTargetPosition(-encoderValue);
            chassis.robot.driveRF.setTargetPosition(encoderValue);
            chassis.robot.driveLR.setTargetPosition(-encoderValue);
            chassis.robot.driveRR.setTargetPosition(encoderValue);


            // Turn On RUN_TO_POSITION
            chassis.robot.driveLF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            chassis.robot.driveRF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            chassis.robot.driveLR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            chassis.robot.driveRR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            chassis.robot.driveLF.setPower(Math.abs(speed));
            chassis.robot.driveRF.setPower(Math.abs(speed));
            chassis.robot.driveLR.setPower(Math.abs(speed));
            chassis.robot.driveRR.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (curOpMode.opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (chassis.robot.driveLF.isBusy() && chassis.robot.driveRF.isBusy()
                            && chassis.robot.driveLR.isBusy() && chassis.robot.driveRR.isBusy()
                    )) {

                chassis.robot.driveLF.setPower(Math.abs(-speed));
                chassis.robot.driveRF.setPower(Math.abs(speed));
                chassis.robot.driveLR.setPower(Math.abs(-speed));
                chassis.robot.driveRR.setPower(Math.abs(speed));


            }

            // Stop all motion;
            chassis.robot.driveLF.setPower(0);
            chassis.robot.driveRF.setPower(0);
            chassis.robot.driveLR.setPower(0);
            chassis.robot.driveRR.setPower(0);


            // Turn off RUN_TO_POSITION
            chassis.robot.driveLF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            chassis.robot.driveRF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            chassis.robot.driveLR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            chassis.robot.driveRR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


            curOpMode.sleep(500);   // optional pause after each move

            chassis.rotation = chassis.getAngle();
            // reset angle tracking on new heading.
            chassis.resetAngle();
        }
    }


    public void resetEncoders() {
        //Reset the encoders
        chassis.robot.driveLF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        chassis.robot.driveRF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        chassis.robot.driveLR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        chassis.robot.driveRR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //set all the motors to run using encoders
        chassis.robot.driveLF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        chassis.robot.driveRF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        chassis.robot.driveLR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        chassis.robot.driveRR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    public VectorF encoderStrafe(double power, double timeoutS, int encoderValue, boolean strafeLeft,
                                 boolean pid, boolean objectDetection) {

        chassis.rotation = chassis.getAngle();        // reset angle tracking on new heading.
        chassis.resetAngle();

        double powerRightRear;
        double powerLeftRear;
        double powerLeftFront;
        double powerRightFront;

        double correction = 0;

        double currentPower = .1;

        runtime.reset();

        VectorF blockLoc = null;

        resetEncoders();
        //   CameraDevice.getInstance().setFlashTorchMode(true);

        int setA = 1;
        int setB = 1;

        if (strafeLeft) {
            setA = -1;
            setB = 1;
        } else {
            setA = 1;
            setB = -1;
        }

        // Determine new target position, and pass to motor controller;
        chassis.robot.driveLF.setTargetPosition(encoderValue * setA);
        chassis.robot.driveRF.setTargetPosition(encoderValue * setB);
        chassis.robot.driveLR.setTargetPosition(encoderValue * setB);
        chassis.robot.driveRR.setTargetPosition(encoderValue * setA);


        // Turn On RUN_TO_POSITION
        chassis.robot.driveLF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        chassis.robot.driveRF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        chassis.robot.driveLR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        chassis.robot.driveRR.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        int startDecelerationAt = (int) (encoderValue * .95);


        while (curOpMode.opModeIsActive() &&
                (runtime.seconds() < timeoutS) &&
                (chassis.robot.driveLF.isBusy() && chassis.robot.driveRF.isBusy()
                        && chassis.robot.driveLR.isBusy() && chassis.robot.driveRR.isBusy())
        ) {

            currentPower = power;//rampUpDown(power,currentPower,.1,chassis.robot.driveRR.getCurrentPosition(),startDecelerationAt);

            chassis.robot.driveRR.setPower(currentPower);
            chassis.robot.driveLR.setPower(currentPower);
            chassis.robot.driveLF.setPower(currentPower);
            chassis.robot.driveRF.setPower(currentPower);
/*
            if(objectDetection) {
                blockLoc = vuforiaCom.executeDetection();
                if (blockLoc != null) {
                    break;
                }
            }
 */
        }

        // Stop all motion;
        chassis.robot.driveLF.setPower(0);
        chassis.robot.driveRF.setPower(0);
        chassis.robot.driveLR.setPower(0);
        chassis.robot.driveRR.setPower(0);


        // Turn off RUN_TO_POSITION
        chassis.robot.driveLF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        chassis.robot.driveRF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        chassis.robot.driveLR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        chassis.robot.driveRR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //     CameraDevice.getInstance().setFlashTorchMode(false);

        return blockLoc;
    }


    public double rampUpDown(double maxPower, double curPower, double minPower, int curPosition, int decelStartPosition) {

        double returnPower;

        if (Math.abs(curPosition) < Math.abs(decelStartPosition) && curPower < maxPower) {
            returnPower = curPower + .01;
        } else if (Math.abs(curPosition) >= Math.abs(decelStartPosition) && curPower > minPower) {
            returnPower = curPower - .01;
        } else {
            returnPower = curPower;
        }

        return returnPower;
    }


    public VectorF strafeToDistance(double slideSlowPower, double timeoutS, double distance,
                                    DistanceSensor sensor, boolean objectDetection) {

        chassis.rotation = chassis.getAngle();        // reset angle tracking on new heading.
        chassis.resetAngle();

        double powerRightRear;
        double powerLeftRear;
        double powerLeftFront;
        double powerRightFront;

        double correction = 0;

        runtime.reset();

        VectorF blockLoc = null;

        if (objectDetection) {
            CameraDevice.getInstance().setFlashTorchMode(true);
        }

        while (curOpMode.opModeIsActive() &&
                (runtime.seconds() < timeoutS)) {


            if ((slideSlowPower > 0 && sensor.getDistance(DistanceUnit.CM) > distance) ||
                    (slideSlowPower < 0 && sensor.getDistance(DistanceUnit.CM) < distance)) {
                break;
            }


            correction = 0;//chassis.pidDrive.performPID(chassis.getAngle());
            //Front Motors
            powerLeftFront = -slideSlowPower - correction;
            powerRightFront = slideSlowPower + correction;

            //Rear Motors
            powerRightRear = -slideSlowPower + correction;
            powerLeftRear = slideSlowPower - correction;

            chassis.robot.driveRR.setPower(powerRightRear);
            chassis.robot.driveLR.setPower(powerLeftRear);
            chassis.robot.driveLF.setPower(powerLeftFront);
            chassis.robot.driveRF.setPower(powerRightFront);
/*
           if(objectDetection) {
               blockLoc = vuforiaCom.executeDetection();
               if (blockLoc != null) {
                   break;
               }
           }
 */
        }

        if (objectDetection) {
            CameraDevice.getInstance().setFlashTorchMode(false);
        }

        chassis.robot.driveLF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        chassis.robot.driveRF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        chassis.robot.driveLR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        chassis.robot.driveRR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Stop all motion;
        chassis.robot.driveLF.setPower(0);
        chassis.robot.driveRF.setPower(0);
        chassis.robot.driveLR.setPower(0);
        chassis.robot.driveRR.setPower(0);


        // Turn off RUN_TO_POSITION
        chassis.robot.driveLF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        chassis.robot.driveRF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        chassis.robot.driveLR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        chassis.robot.driveRR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        return blockLoc;
    }

    public VectorF strafeAwayDistance(double slideSlowPower, double timeoutS, double distance,
                                      DistanceSensor sensor, boolean objectDetection) {

        chassis.rotation = chassis.getAngle();        // reset angle tracking on new heading.
        chassis.resetAngle();

        double powerRightRear;
        double powerLeftRear;
        double powerLeftFront;
        double powerRightFront;

        double correction = 0;

        runtime.reset();

        VectorF blockLoc = null;

        CameraDevice.getInstance().setFlashTorchMode(true);

        while (curOpMode.opModeIsActive() &&
                (runtime.seconds() < timeoutS) && (sensor.getDistance(DistanceUnit.CM) < distance)) {

            correction = 0;//chassis.pidDrive.performPID(chassis.getAngle());
            //Front Motors
            powerLeftFront = -slideSlowPower - correction;
            powerRightFront = slideSlowPower + correction;

            //Rear Motors
            powerRightRear = -slideSlowPower + correction;
            powerLeftRear = slideSlowPower - correction;

            chassis.robot.driveRR.setPower(powerRightRear);
            chassis.robot.driveLR.setPower(powerLeftRear);
            chassis.robot.driveLF.setPower(powerLeftFront);
            chassis.robot.driveRF.setPower(powerRightFront);
/*
            if(objectDetection) {
                blockLoc = vuforiaCom.executeDetection();
                if (blockLoc != null) {
                    break;
                }
            }
 */
        }

        CameraDevice.getInstance().setFlashTorchMode(false);

        chassis.robot.driveLF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        chassis.robot.driveRF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        chassis.robot.driveLR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        chassis.robot.driveRR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Stop all motion;
        chassis.robot.driveLF.setPower(0);
        chassis.robot.driveRF.setPower(0);
        chassis.robot.driveLR.setPower(0);
        chassis.robot.driveRR.setPower(0);


        // Turn off RUN_TO_POSITIONssss
        chassis.robot.driveLF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        chassis.robot.driveRF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        chassis.robot.driveLR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        chassis.robot.driveRR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        return blockLoc;
    }

    public void moveSpinner(double speed, double timeoutS){
        boolean red = spinner.red;
        if (curOpMode.opModeIsActive()){
            runtime.reset();
            while (curOpMode.opModeIsActive() && runtime.seconds() < timeoutS){
                if(red){
                    spinner.robot.spinnerMotorRed.setPower(speed);
                } else {
                    spinner.robot.spinnerMotorBlue.setPower(speed);
                }
            }
            spinner.robot.spinnerMotorRed.setPower(0);
            spinner.robot.spinnerMotorBlue.setPower(0);
        }
    }

    public int getDistFromHub(int pos, boolean red){
        if(red){
            if(pos == 3){
                return 1240;
            } else if (pos == 2){
                return 1160;
            } else if (pos == 1){
                return 980;
            } else{
                return 1240;
            }
        } else {
            if(pos == 3){
                return 1270;
            } else if (pos == 2){
                return 1190;
            } else if (pos == 1){
                return 1100;
            } else{
                return 1240;
            }
        }

    }

    public int getPos(boolean blue){
        int collector = conveyor.spawnpoint();

        int returnVal;

        if (blue){
            if(collector == 1){
                returnVal = 2;
            } else if (collector == 2){
                returnVal = 3;
            } else if (collector == 3){
                returnVal = 1;
            } else {
                returnVal = 3;
            }
        } else {
            returnVal = collector;
        }

        curOpMode.telemetry.addData("Position", returnVal);
        curOpMode.telemetry.update();

        return returnVal;
    }

    public void checkDistances(){
        curOpMode.telemetry.addData("Sensor 1", conveyor.robot.ds1.getDistance(DistanceUnit.INCH));
        curOpMode.telemetry.addData("Sensor 2", conveyor.robot.ds2.getDistance(DistanceUnit.INCH));

        curOpMode.telemetry.update();

    }
}