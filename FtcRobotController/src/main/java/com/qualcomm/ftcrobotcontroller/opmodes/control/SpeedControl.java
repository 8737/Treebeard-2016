package com.qualcomm.ftcrobotcontroller.opmodes.control;

import com.qualcomm.ftcrobotcontroller.opmodes.DriveMath;

/**
 * Created by tdoylend on 2016-01-30.
 */
public class SpeedControl {
    double actual;
    double desired;
    double factor = 0.1;
    double cPower = 0;

    double hFactor = 0;
    double dFactor = 1;
    double eFactor = 0;

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
        double powerChange;
        powerChange = delta * factor;
        this.cPower = (this.cPower * hFactor) + (this.desired * dFactor) + (delta*eFactor);
        this.cPower = DriveMath.limit(this.cPower,-1,1);
    }

    public double getPower() {
        return this.cPower;
    }
}
