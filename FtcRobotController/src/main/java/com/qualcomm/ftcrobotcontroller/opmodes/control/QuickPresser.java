package com.qualcomm.ftcrobotcontroller.opmodes.control;

import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by tdoylend on 2016-01-14.
 *
 * This control implements a 'quick presser'.
 * It requires someone to push a button a certain number of
 * times.
 */
public class QuickPresser {

    public int counter=0;
    public int threshold=3;
    public boolean event = false;
    public boolean toggle=false;
    boolean btnEnabled=true;
    public ElapsedTime timer = new ElapsedTime();

    public void update(boolean btnState) {
        if (this.btnEnabled) {
            if (btnState) {
                this.btnEnabled=false;
                if (this.timer.time()<.75) this.counter++;
                else this.counter=0;
                this.timer.reset();
                if (this.counter>=this.threshold) {
                    this.event=true;
                    this.counter=0;
                    this.toggle=!this.toggle;
                }
            }
        } else {
            if (!btnState) this.btnEnabled=true;
        }}

        public boolean isEvent() {
            boolean temp=this.event;
            this.event=false;
            return temp;
        }
}
