package com.qualcomm.ftcrobotcontroller.opmodes.red;

import com.qualcomm.ftcrobotcontroller.opmodes.PacmanBotHardwareBase3;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by tdoylend on 2016-01-30.
 */
public class TestWheel extends PacmanBotHardwareBase3 {
    public void init() {
        setupHardware();
        drive.setE(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }

    public void loop() {
        telemetry.addData("LEFT",drive.getLeftE());
        telemetry.addData("RIGHT",drive.getRightE());
    }
}
