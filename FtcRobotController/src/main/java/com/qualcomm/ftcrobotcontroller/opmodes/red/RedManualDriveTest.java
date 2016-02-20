package com.qualcomm.ftcrobotcontroller.opmodes.red;

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

    public void init() {
        telemetry.addData("Hello","RMDT- is alive");
        timer.reset();

        setupHardware();
    }

    public void loop() {
        double dLeft = -gamepad1.left_stick_y + gamepad1.right_stick_x;
        double dRight= -gamepad1.left_stick_y - gamepad1.right_stick_x;

        leftcs.updateDesired(dLeft);
        rightcs.updateDesired(dRight);

        if (timer.time()>1.0) {
            double lSpeed = (drive.getLeftE() / timer.time()) / 280.0;
            double rSpeed = (drive.getRightE() / timer.time()) / 280.0;
            leftcs.updateActual(lSpeed);
            rightcs.updateActual(rSpeed);
            leftcs.compute();
            rightcs.compute();
            drive.setE(DcMotorController.RunMode.RESET_ENCODERS);
            timer.reset();
            ticks++;
            telemetry.addData("10 : Ticks ",ticks);
            encoderTick = true;
        } else {
            if (encoderTick) {
                drive.setE(DcMotorController.RunMode.RUN_USING_ENCODERS);
                encoderTick=false;
            }
            drive.driveRaw(leftcs.getPower(), rightcs.getPower());
        }
    }
}
