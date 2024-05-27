package org.example.iec61850.frequency;

import org.example.iec61850.datatypes.common.Attribute;
import org.example.iec61850.datatypes.measuredVal.MV;

public class ZeroCrossingMethod implements FrequencyMeter{
    private boolean isCrossed;
    private int counter;
    private double freq;
    private double current;
    private double prev;
    private final double timeMultiplier;
    private double prevTimeZeroCrossing;

    public ZeroCrossingMethod(double timeMultiplier){
        this.timeMultiplier  = timeMultiplier;
        this.isCrossed = false;
        this.counter = 0;
        this.freq = 50;
        this.prevTimeZeroCrossing = -1;
    }
    @Override
    public void process(MV inValue, Attribute<Double> frequency) {
        if(this.counter == 0){
            this.prev = inValue.getInstMag().getF().getValue();

        } else {
            this.current = inValue.getInstMag().getF().getValue();
            if (this.current*this.prev <= 0){
                double currentTimeZeroCrossing = calculateZeroCrossingTime();
                if(this.prevTimeZeroCrossing < 0){
                    this.prevTimeZeroCrossing = currentTimeZeroCrossing;
                } else {
                    this.freq = 1/Math.abs(currentTimeZeroCrossing - this.prevTimeZeroCrossing)/2;
                    this.prevTimeZeroCrossing = currentTimeZeroCrossing;
                    System.out.println(this.freq);
                    System.out.println(counter);
                }
            }
            this.prev = this.current;
        }
        this.counter++;
        frequency.setValue(freq);

    }
    private double calculateZeroCrossingTime(){
        double prevTime = this.counter*this.timeMultiplier;
        double currentTime = (this.counter + 1) * this.timeMultiplier;
        double timeZeroCrossing = prevTime +
                Math.abs(this.prev)*Math.abs(currentTime - prevTime)/Math.abs(this.current - this.prev);
        return timeZeroCrossing;
    }
}
