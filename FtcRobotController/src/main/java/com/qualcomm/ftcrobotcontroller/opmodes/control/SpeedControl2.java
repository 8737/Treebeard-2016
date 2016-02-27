package com.qualcomm.ftcrobotcontroller.opmodes.control;

import com.qualcomm.ftcrobotcontroller.opmodes.PacmanBotHardwareBase3;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by tdoylend on 2016-02-25.
 */
public class SpeedControl2 {
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

    ElapsedTime timer;

    public SpeedControl2() {
        this.timer = new ElapsedTime();
    }

    public void boot(double ePos) {
        this.encoderPosition = ePos;
        this.nextTimerEvent = this.timer.time() + this.T;
    }

    public double compute(double C, double cMeasure) {
        if (timer.time()>this.nextTimerEvent) {
            this.nextTimerEvent += this.T;
            nextTimerEvent += T;
            double B = cMeasure - this.encoderPosition;
            this.encoderPosition = cMeasure;
            double S = B / this.Sm;
            double E = C - S;
            this.Ei = this.Ei + E;
            this.Ed = (E - this.Ep) / this.T;
            this.Ep = E;
            this.P = this.P + this.Kp * E + this.Ki * this.Ei + this.Kd * this.Ed;
        }
        return this.P;
    }
}
