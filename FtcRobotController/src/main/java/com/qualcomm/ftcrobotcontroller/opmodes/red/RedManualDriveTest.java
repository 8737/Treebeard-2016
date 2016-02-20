package com.qualcomm.ftcrobotcontroller.opmodes.red;

import com.qualcomm.ftcrobotcontroller.opmodes.DriveMath;
import com.qualcomm.ftcrobotcontroller.opmodes.PacmanBotHardwareBase3;
import com.qualcomm.ftcrobotcontroller.opmodes.control.SpeedControl;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by tdoylend on 2016-01-30.
 *
 *
 */
public class RedManualDriveTest extends PacmanBotHardwareBase3 {
    SpeedControl leftcs = new SpeedControl();
    SpeedControl rightcs = new SpeedControl();

    ElapsedTime timer = new ElapsedTime();

    boolean encoderTick=false;
    int ticks= 0;
    double nextTime = 0;
    boolean startup = true;

    public void init() {
        telemetry.addData("Hello","RMDT- is alive");
        timer.reset();

        setupHardware();
    }

    public void loop() {
        if (startup) {
            startup=false;
            nextTime = timer.time();
        }
        double dLeft = -gamepad1.left_stick_y + gamepad1.right_stick_x;
        double dRight= -gamepad1.left_stick_y - gamepad1.right_stick_x;

        leftcs.updateDesired(dLeft);
        rightcs.updateDesired(dRight);

        if (timer.time()>=nextTime) {
            nextTime += .02;
            double lTicks = drive.getLeftE();
            double rTicks = drive.getRightE();
            leftcs.updateActual(DriveMath.limit(lTicks / 160.0,-1,1));
            rightcs.updateActual(DriveMath.limit(rTicks / 160.0,-1,1));
            leftcs.compute();
            rightcs.compute();
            drive.setE(DcMotorController.RunMode.RESET_ENCODERS);
            //timer.reset();
            ticks++;
            telemetry.addData("10 : Ticks ", ticks);
            telemetry.addData("11 : cPower ", leftcs.cPower);
            telemetry.addData("12 : desired ", leftcs.desired);
            telemetry.addData("13 : actual ", leftcs.actual);
            encoderTick = true;
        } else {
            if (encoderTick) {
                drive.setE(DcMotorController.RunMode.RUN_USING_ENCODERS);
                encoderTick=false;
            } else {
                drive.driveRaw(leftcs.getPower(), rightcs.getPower());
            }
        }
    }
}
