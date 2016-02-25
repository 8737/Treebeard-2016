package com.qualcomm.ftcrobotcontroller.opmodes.red;

import com.qualcomm.ftcrobotcontroller.opmodes.PacmanBotHardwareBase3;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by tdoylend on 2016-02-25.
 */

public class MinimalSpeedControl extends PacmanBotHardwareBase3 {

    final static double T = 0.05;
    final static double Br = 3200;
    final static double Sm = Br * T;
    final static double Kp = .35;
    final static double Ki = 0;
    final static double Kd = 0;//.25;
    double P = 0;
    double Ei = 0;
    double Ed = 0;
    double Ep = 0;

    double encoderPosition = 0;
    double nextTimerEvent  = 0;


    boolean boot = true;

    ElapsedTime timer = new ElapsedTime();

    @Override
    public void init() {
        telemetry.addData("MinimalSpeedControl","v1.0.0");

        setupHardware();

        drive.setE(DcMotorController.RunMode.RESET_ENCODERS);
    }

    @Override
    public void loop() {
        if (boot) {
            drive.setE(DcMotorController.RunMode.RUN_USING_ENCODERS);
            nextTimerEvent = timer.time() + T;
            boot = false;
        } else {
            if (timer.time() >= nextTimerEvent) {
                telemetry.addData("01: Time",timer.time());
                nextTimerEvent += T;
                double C = -gamepad1.left_stick_y / 4;
                telemetry.addData("02: C",C);
                double cMeasure = drive.getLeftE();
                double B = cMeasure - encoderPosition;
                telemetry.addData("03: B",B);
                encoderPosition = cMeasure;
                double S = B / Sm;
                telemetry.addData("04: S",S);
                double E = C - S;
                telemetry.addData("05: E",E);
                Ei = Ei + E;
                telemetry.addData("06: Ei",Ei);
                Ed = (E - Ep) / T;
                telemetry.addData("07: Ed",Ed);
                Ep = E;
                telemetry.addData("08: Ep",Ep);
                P = P + Kp * E + Ki * Ei + Kd * Ed;
                telemetry.addData("09: P",P);
                drive.driveRaw(P, 0);
            }
        }
    }
}
