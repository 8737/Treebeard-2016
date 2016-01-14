package com.qualcomm.ftcrobotcontroller.opmodes.red;

import com.qualcomm.ftcrobotcontroller.opmodes.PacmanBotHardwareBase3;
import com.qualcomm.ftcrobotcontroller.opmodes.control.AutoDriveController;

/**
 * Created by sathk_000 on 1/14/2016.
 */
public class RedBeaconRoute2Encoders extends PacmanBotHardwareBase3{
    AutoDriveController autoDriveController;
    int thing=1000;
    int s=-1;
    @Override
    public void init() {
        setupHardware();
        autoDriveController = new AutoDriveController(rearLeft, rearRight);
        frontLeft.setPowerFloat();
        frontRight.setPowerFloat();
    }
    @Override
    public void loop() {
        autoDriveController.check();
        if(autoDriveController.getStep()!=-1) {
            s=autoDriveController.getStep();
        }
        switch (autoDriveController.getStep()) {//steps :)
            case 0:
                autoDriveController.encoderDrive(thing, thing, 1);
                break;
            case 1:
                autoDriveController.encoderDrive(0, thing, 1);
                break;
            case 2:
                autoDriveController.encoderDrive(thing, thing, 1);
                break;
            case 3:
                autoDriveController.delay(10.0);
                break;
            case 4:
                autoDriveController.encoderDrive(thing, 0, 1);
                break;
            case 5:
                autoDriveController.encoderDrive(-2*thing, -2*thing, 1);
                break;
            case 6:
                autoDriveController.encoderDrive(-thing, thing, 1);
                break;
            default:
        }
        telemetry.addData("Step",s);
    }
}
