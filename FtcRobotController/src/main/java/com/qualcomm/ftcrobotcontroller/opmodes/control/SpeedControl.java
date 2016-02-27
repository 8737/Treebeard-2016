package com.qualcomm.ftcrobotcontroller.opmodes.control;

import com.qualcomm.ftcrobotcontroller.opmodes.DriveMath;

/**
 * Created by tdoylend on 2016-01-30.
 */
public class SpeedControl {
    public double actual;
    public double desired;
    public double cPower = 0;

    final static double hFactor = 1;
    final static double dFactor = 0;
    final static double eFactor = .25;

    public SpeedControl() {
        this.actual = 0;
        this.desired = 0;
    }

    public void updateActual(double speed) {
        this.actual = speed;
    }

    public void updateDesired(double speed) {
        this.desired = speed;
    }

    public void compute() {
        double delta;
        delta = desired - actual;
        this.cPower = (this.cPower * hFactor) + (this.desired * dFactor) + (delta*eFactor);
        this.cPower = DriveMath.limit(this.cPower,-1,1);
    }

    public double getPower() {
        return this.cPower;
    }
}
