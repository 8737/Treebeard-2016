package com.qualcomm.ftcrobotcontroller.opmodes.red;

import com.qualcomm.ftcrobotcontroller.opmodes.DriveMath;
import com.qualcomm.ftcrobotcontroller.opmodes.PacmanBotHardwareBase3;
import com.qualcomm.ftcrobotcontroller.opmodes.control.QuickPresser;
import com.qualcomm.ftcrobotcontroller.opmodes.drive.Drive;
import com.qualcomm.ftcrobotcontroller.opmodes.ui.ToggleButton;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by tdoylend on 2015-12-20.
 *
 * This is the red-team manual drive opmode.
 *
 * Robot Type: PacmanBot
 * Config File: Final2
 */

public class RedManualDrive extends PacmanBotHardwareBase3 {

    public String version = "1.0.0";

    ElapsedTime timer = new ElapsedTime();

    public ToggleButton climberTripperToggle = new ToggleButton();
    public ToggleButton mtnModeToggle = new ToggleButton();
    public ToggleButton hookReleaseToggle = new ToggleButton();
    public ToggleButton spareTireToggle = new ToggleButton();
    public ToggleButton doorToggle = new ToggleButton();
    public ToggleButton handWaveToggle = new ToggleButton();

    public QuickPresser handWavePresser = new QuickPresser();

    @Override
    public void init() {
        telemetry.addData("Program", "Manual Drive");
        telemetry.addData("Robot Type", "PacmanBot");
        telemetry.addData("Config File","Final2");
        telemetry.addData("Opmode Version",version);
        telemetry.addData("HWB Version",hwbVersion);

        side = false;

        setupHardware();
    }

    @Override
    public void loop() {
        //Gamepad Y values are -1 at TOP and 1 at BOTTOM. Therefore, Y values from them
        //need to be negated before they go into any of the drive routines.

        double driveRate = -gamepad1.left_stick_y; //Negate the Y as mentioned above
        double turnRate  = gamepad1.right_stick_x;

        mtnModeToggle.update(gamepad1.left_stick_button);

        if (mtnModeToggle.getState()) {
            drive.driveMtn(driveRate,turnRate); //Drive as desired!
        } else {
            drive.driveStd(driveRate, turnRate);
        }

        climberTripperToggle.update(gamepad1.x);
        if (climberTripperToggle.isEvent()) {
            climberTripper.set(climberTripperToggle.getState());
        }

        setBasketPower(DriveMath.threeWay(gamepad1.dpad_left, gamepad1.dpad_right));
        setBrushPower(DriveMath.threeWay(gamepad1.left_trigger > .5, gamepad1.left_bumper));

        handWavePresser.update(gamepad1.a);
        telemetry.addData("DEBUG-HWT", handWavePresser.counter);
        if (!handWavePresser.toggle) {
            climberBucket.set(gamepad1.a);
        } else {
            climberBucket.setRaw(0.5 + (Math.sin(timer.time()*4)/4));
        }

        doorToggle.update(gamepad1.b);
        basketDoor.set(doorToggle.getState() || gamepad1.left_bumper);

        hookReleaseToggle.update(gamepad1.start);
        //telemetry.addData("DEBUG-HRT",hookReleaseToggle.getState());
        if (hookReleaseToggle.isEvent()) {
            hookRelease.set(hookReleaseToggle.getState());
        }

        setWinchPower(DriveMath.threeWay(gamepad1.right_bumper, gamepad1.right_trigger > .5));

        spareTireToggle.update(gamepad1.y);
        if (spareTireToggle.isEvent()){
            setSpareTire(spareTireToggle.getState());
        }
    }
}
