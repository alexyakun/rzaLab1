package org.example.iec61850.lodicalNodes.measurement;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.Vector;
import org.example.iec61850.datatypes.measuredVal.SAV;
import org.example.iec61850.datatypes.measuredVal.WYE;
import org.example.iec61850.lodicalNodes.common.LN;
import org.example.iec61850.utils.CompCal;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class RMXU extends LN {

    private SAV AmpLocPhsA = new SAV();
    private SAV AmpLocPhsB = new SAV();
    private SAV AmpLocPhsC = new SAV();
    private SAV AmpLocResA = new SAV();
    private SAV AmpLocResB = new SAV();
    private SAV AmpLocResC = new SAV();
    //input
    public List<MMXU> mmxuList = new ArrayList<>();
    @Override
    public void process() {
        double tormCurrentA = 0;
        Vector diffCurrentA = new Vector(0.0,0.0);
        double tormCurrentB = 0;
        Vector diffCurrentB = new Vector(0.0,0.0);;
        double tormCurrentC = 0;
        Vector diffCurrentC = new Vector(0.0,0.0);;
        for (MMXU mmxu : mmxuList) {
            tormCurrentA+=mmxu.getA().getPhsA().getInstCVal().getMag().getF().getValue();
            tormCurrentB+=mmxu.getA().getPhsB().getInstCVal().getMag().getF().getValue();
            tormCurrentC+=mmxu.getA().getPhsC().getInstCVal().getMag().getF().getValue();
            diffCurrentA = CompCal.sum(diffCurrentA, mmxu.getA().getPhsA().getInstCVal());
            diffCurrentB = CompCal.sum(diffCurrentB, mmxu.getA().getPhsB().getInstCVal());
            diffCurrentC = CompCal.sum(diffCurrentC, mmxu.getA().getPhsC().getInstCVal());
        }
        this.AmpLocPhsA.getInstMag().getF().setValue(diffCurrentA.getMag().getF().getValue());
        this.AmpLocPhsB.getInstMag().getF().setValue(diffCurrentB.getMag().getF().getValue());
        this.AmpLocPhsC.getInstMag().getF().setValue(diffCurrentC.getMag().getF().getValue());
        this.AmpLocResA.getInstMag().getF().setValue(tormCurrentA);
        this.AmpLocResB.getInstMag().getF().setValue(tormCurrentB);
        this.AmpLocResC.getInstMag().getF().setValue(tormCurrentC);
    }
}
