package com.qualcomm.ftcrobotcontroller.opmodes.blue;

import com.qualcomm.ftcrobotcontroller.opmodes.DriveMath;
import com.qualcomm.ftcrobotcontroller.opmodes.PacmanBotHardwareBase3;
import com.qualcomm.ftcrobotcontroller.opmodes.control.QuickPresser;
import com.qualcomm.ftcrobotcontroller.opmodes.control.SpeedControl;
import com.qualcomm.ftcrobotcontroller.opmodes.control.SpeedControl2;
import com.qualcomm.ftcrobotcontroller.opmodes.control.StandardDriveInterface;
import com.qualcomm.ftcrobotcontroller.opmodes.ui.ToggleButton;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by tdoylend on 2016-02-25.
 */
public class BlueSkynet extends PacmanBotHardwareBase3 {

    ElapsedTime timer = new ElapsedTime();

    public ToggleButton climberTripperToggle = new ToggleButton();
    public ToggleButton mtnModeToggle = new ToggleButton();
    public ToggleButton csModeToggle  = new ToggleButton();
    public ToggleButton hookReleaseToggle = new ToggleButton();
    public ToggleButton spareTireToggle = new ToggleButton();
    public ToggleButton doorToggle = new ToggleButton();
    public ToggleButton hookAimToggle = new ToggleButton();

    public QuickPresser handWavePresser = new QuickPresser();

    //boolean mountainMode = false;
    //boolean csMode = false;
    boolean bootCycle = true;

    SpeedControl2 lsc = new SpeedControl2();
    SpeedControl2 rsc = new SpeedControl2();
    SpeedControl2 bsc = new SpeedControl2();

    boolean csBoot = true;

    @Override
    public void init() {
        telemetry.addData("Program","Skynet Drive");
        telemetry.addData("Version","1.0.0");
        telemetry.addData("Color","Blue");

        side = true;

        setupHardware();
        drive.setE(DcMotorController.RunMode.RESET_ENCODERS);
        setBasketMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }

    @Override
    public void loop() {
        if (bootCycle) {
            drive.setE(DcMotorController.RunMode.RUN_USING_ENCODERS);
            bsc.boot(getBasketPos());
            bootCycle=false;
            return;
        }

        double d = -gamepad1.left_stick_y;
        double t  =  gamepad1.right_stick_x;

        double lTarget = StandardDriveInterface.motorSpeed(d,t,-1);
        double rTarget = StandardDriveInterface.motorSpeed(d,t,1);
        //telemetry.addData("lTarget",lTarget);
        //telemetry.addData("rTarget",rTarget);
        telemetry.addData("MTN",mtnModeToggle.getState());
        telemetry.addData("CSM",csModeToggle.getState());

        if (mtnModeToggle.getState()) {
            drive.driveMtn(d,t);
        }
        else {
            if (!csModeToggle.getState()) {
                drive.driveRaw(lTarget,rTarget);
                csBoot = true;
            } else {
                if (csBoot) {
                    lsc.boot(drive.getLeftE());
                    rsc.boot(drive.getRightE());
                    csBoot = false;
                } else {
                    drive.driveRaw(
                            lsc.compute(lTarget / 2, drive.getLeftE()),
                            rsc.compute(rTarget / 2, drive.getRightE())
                    );
                }

            }
        }

        if(side) {
            if (basket.getCurrentPosition()<=0 && gamepad1.dpad_left) {
                setBasketPower(0.1);
            }
            else if (basket.getCurrentPosition()>=-255 && gamepad1.dpad_right) {
                setBasketPower(-0.1);
            }
            else {
                setBasketPower(0);
            }
        }
        else {
            if (basket.getCurrentPosition()<=185 && gamepad1.dpad_left) {
                setBasketPower(0.1);
            }
            else if (basket.getCurrentPosition()>=0 && gamepad1.dpad_right) {
                setBasketPower(-0.1);
            }
            else {
                setBasketPower(0);
            }
        }

        //Update toggles

        climberTripperToggle.update(gamepad1.x);
        mtnModeToggle.update(gamepad1.left_stick_button);
        csModeToggle.update(gamepad1.right_stick_button);
        hookReleaseToggle.update(gamepad1.start);
        spareTireToggle.update(gamepad1.y);
        doorToggle.update(gamepad1.b);
        hookAimToggle.update(gamepad1.a);

        if (climberTripperToggle.isEvent()) climberTripper.set(climberTripperToggle.getState());
        if (hookReleaseToggle.isEvent()) hookRelease.set(hookReleaseToggle.getState());
        if (spareTireToggle.isEvent()) setSpareTire(spareTireToggle.getState());
        if (doorToggle.isEvent()) basketDoor.set(doorToggle.getState());
        if (hookAimToggle.isEvent()) hookAim.set(hookAimToggle.getState());

        //Do miscellanous updates
        setWinchPower(DriveMath.threeWay(gamepad1.right_bumper, gamepad1.right_trigger > .5));

        handWavePresser.update(gamepad1.a);
        if (!handWavePresser.toggle) climberBucket.set(gamepad1.a);
        else climberBucket.setRaw(0.5 + (Math.sin(timer.time()*4)/4));

    }
}
