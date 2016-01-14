package com.qualcomm.ftcrobotcontroller.opmodes.control;

import com.qualcomm.ftcrobotcontroller.opmodes.imports.*;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

public class AutoDriveController {
    int mStep=0;
    double mTimeSec;
    boolean mRunning=false;
    boolean mDelaying=false;
    ElapsedTime timer;
    com.qualcomm.ftcrobotcontroller.opmodes.imports.RunToPositionController mRearRightController;
    com.qualcomm.ftcrobotcontroller.opmodes.imports.RunToPositionController mRearLeftController;

    public AutoDriveController(DcMotor rearLeft, DcMotor rearRight) {
        timer = new ElapsedTime();
        mRearLeftController=new com.qualcomm.ftcrobotcontroller.opmodes.imports.RunToPositionController(rearLeft,true);
        mRearRightController=new com.qualcomm.ftcrobotcontroller.opmodes.imports.RunToPositionController(rearRight,true);
    }
    public int getStep() {
        return ((mRunning || mDelaying) ? -1 : mStep);
    }
    public boolean isBusy() {
        return mRearLeftController.isBusy() || mRearRightController.isBusy();
    }
    public void encoderDrive(int leftPos, int rightPos, double power) {
        if(leftPos!=0) {
            double p=(Math.abs(leftPos)>=Math.abs(rightPos) ? power : power * (Math.abs(leftPos/rightPos)));
            mRearLeftController.goTo(leftPos,p);
        }
        if(rightPos!=0) {
            double p=(Math.abs(leftPos)<=Math.abs(rightPos) ? power : power * (Math.abs(rightPos/leftPos)));
            mRearRightController.goTo(rightPos,p);
        }
        mRunning=true;
    }
    public void delay(double timeSec) {
        timer.reset();
        mTimeSec=timeSec;
        mDelaying=true;
    }
    public void check()
    {
        mRearLeftController.check();
        mRearRightController.check();
        if(mRunning && !isBusy()) {
            mStep++;
            mRunning=false;
        }
        else if(mDelaying && timer.time()>=mTimeSec) {
            mStep++;
            mDelaying=false;
        }
    }
}

