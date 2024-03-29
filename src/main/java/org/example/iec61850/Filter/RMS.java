package org.example.iec61850.Filter;

import org.example.iec61850.datatypes.common.Attribute;
import org.example.iec61850.datatypes.measuredVal.CMV;
import org.example.iec61850.datatypes.measuredVal.MV;

public class RMS extends Filter {
    private Attribute<Integer> buffSize = new Attribute<>();
    private final MV[] buffer;
    public Attribute<Integer> buffCnt = new Attribute<>();
    public Attribute<Double> summ = new Attribute<>();
    public Attribute<Double> k = new Attribute<>();

    public RMS(int buffsize) {
        this.buffSize.setValue(buffsize);
        this.buffer = new MV[buffsize];
        this.buffCnt.setValue(0);
        this.summ.setValue(0.0);// хранение суммы RMS
        this.k.setValue(1.0 / buffsize);
        for (int i = 0; i < buffsize; i++) {
            MV val = new MV();
            val.getInstMag().getF().setValue(0.0);
            buffer[i] = val;
        }
    }

    @Override
    public void process(MV inValue, CMV outValue) {
        double newVal = inValue.getInstMag().getF().getValue();
        double oldVal = buffer[buffCnt.getValue()].getInstMag().getF().getValue();
        summ.setValue(summ.getValue()+newVal*newVal-oldVal*oldVal);
        outValue.getCVal().getMag().getF().setValue(Math.sqrt(this.k.getValue()*this.summ.getValue()));
        buffer[buffCnt.getValue()].getInstMag().getF().setValue(newVal);
        buffCnt.setValue(buffCnt.getValue() + 1);
        if (buffCnt.getValue().equals(buffSize.getValue())) {
            buffCnt.setValue(0);
        }
    }
}
