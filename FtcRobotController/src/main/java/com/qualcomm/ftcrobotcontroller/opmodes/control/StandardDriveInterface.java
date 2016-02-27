package com.qualcomm.ftcrobotcontroller.opmodes.control;

import com.qualcomm.ftcrobotcontroller.opmodes.DriveMath;

/**
 * Created by tdoylend on 2016-02-25.
 */
public class StandardDriveInterface {
    public static double motorSpeed(double drive, double turn, double coeff) {
        return DriveMath.limit( drive - (turn*coeff), -1, 1);
    }
}
