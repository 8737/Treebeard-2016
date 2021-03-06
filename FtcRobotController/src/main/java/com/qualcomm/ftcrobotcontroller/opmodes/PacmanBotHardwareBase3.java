package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.opmodes.control.RunToPositionController;
import com.qualcomm.ftcrobotcontroller.opmodes.control.TwoPositionServo;
import com.qualcomm.ftcrobotcontroller.opmodes.drive.Tesla;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by tdoylend on 2015-12-20.
 *
 * This is the hardware base for use with PacmanBot.
 *
 * Robot Type: PacmanBot
 * Robot Version: 3000.0
 * Config File: Final2
 *
 * Change log:
 * 1.0.0 - First version.
 */

public class PacmanBotHardwareBase3 extends OpMode {

    public String hwbVersion = "1.0.0";

    @Override
    public void loop() {}
    @Override
    public void init() {}

    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor rearLeft;
    public DcMotor rearRight;

    public DcMotor basket;
    DcMotor winch;

    DcMotor brush;

    DcMotor spareTire;
    RunToPositionController spareTireController;

    public TwoPositionServo climberTripper;
    public TwoPositionServo hookRelease;
    public TwoPositionServo basketDoor;
    public TwoPositionServo climberBucket;
    public TwoPositionServo hookAim;

    public Tesla drive;

    public boolean side=false; //False = red; True = blue

    public void setupHardware() {
        //Everyone's old friend, setupHardware!

        frontLeft = hardwareMap.dcMotor.get("front_left");
        frontRight = hardwareMap.dcMotor.get("front_right");
        rearLeft = hardwareMap.dcMotor.get("rear_left");
        rearRight = hardwareMap.dcMotor.get("rear_right");

        drive = new Tesla(frontLeft, frontRight, rearLeft, rearRight); //Create a new Tesla car - err, drive ;)

        if (side) climberTripper = new TwoPositionServo(hardwareMap.servo.get("finger"), 1, .55);
        else       climberTripper = new TwoPositionServo(hardwareMap.servo.get("finger"),.55,0);

        basket = hardwareMap.dcMotor.get("basket");
        brush = hardwareMap.dcMotor.get("collector");

        climberBucket = new TwoPositionServo(hardwareMap.servo.get("thrower"), 1, 0);
        basketDoor = new TwoPositionServo(hardwareMap.servo.get("door"), 0.65, 0.05);
        hookRelease = new TwoPositionServo(hardwareMap.servo.get("release"), .1, 1);

        winch = hardwareMap.dcMotor.get("winch");

        spareTire = hardwareMap.dcMotor.get("tire");
        spareTireController = new RunToPositionController(spareTire, false);

        spareTireController.goTo(0, .5);

        hookAim = new TwoPositionServo(hardwareMap.servo.get("hookaim"), 1, .5);
        //hookAim = new TwoPositionServo(hardwareMap.servo.get("hookaim"),1,.5);
        basket.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        basket.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }
    public void setBasketPower(double power) {
        basket.setPower(-power);
    }

    public void setBrushPower(double power) {
        brush.setPower(-power);
    }

    public void setWinchPower(double power) {winch.setPower(power);}

    public void setSpareTire(boolean pos) {
        if (pos) {
            spareTireController.goTo(230,.5);
        } else {
            spareTireController.goTo(10,.25);
        }
    }
}
